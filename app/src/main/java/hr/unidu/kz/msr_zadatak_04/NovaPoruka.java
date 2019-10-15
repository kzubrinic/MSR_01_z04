package hr.unidu.kz.msr_zadatak_04;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Scanner;

// Pregled korisnika koji se dohvaćaju iz web servisa
public class NovaPoruka  {
    private Context con;
    private ArrayAdapter<String> adapter = null;
    private String ime = "";
    private Poruka p;
    private IzborActivity aktivnost;
    private String wsUrl = "https://api.meditor.com.hr/msr/dodaj.php";

    public NovaPoruka(Poruka p, IzborActivity aktivnost) throws Exception {
        this.ime = ime;
        this.aktivnost = aktivnost;
        this.p = p;
        new WSPregledHelper(aktivnost, encodeParams()).execute(wsUrl);
    }
    // privatni razred - jednostavnosti radi da bi mogao bez problema ažurirati ekranska polja aktivnosti
    // Ova obrada se odrađuje u pozadini - u drugom procesu da ne blokira korisničko sučelje.
    // Po završetku obrade izvodi se metoda onPostExecute koja ažurira korisničko sučelje
    // doInBackground prima parametar tipa String (odnosno polje Stringova)
    // onProgressUpdate metoda se ne koristi (tip Void)
    // onPostExecute prima parametar tipa User[]
    private static class WSPregledHelper extends AsyncTask<String, Void, Integer> {
        // How to use a static inner AsyncTask class
        //
        // To prevent leaks, you can make the inner class static. The problem
        // with that, though, is that you no longer have access to the Activity's
        // UI views or member variables. You can pass in a reference to the
        // Context but then you run the same risk of a memory leak. (Android can't
        // garbage collect the Activity after it closes if the AsyncTask class has
        // a strong reference to it.)
        // The solution is to make a weak reference to the Activity (or whatever
        // Context you need).
        // https://stackoverflow.com/questions/44309241/warning-this-asynctask-class-should-be-static-or-leaks-might-occur#answer-46166223
        private WeakReference<IzborActivity> activityReference;
        private String pars;
        // only retain a weak reference to the activity
        WSPregledHelper(IzborActivity context, String pars) {
            activityReference = new WeakReference<>(context);
            this.pars = pars;
        }

        @Override
        protected Integer doInBackground(String... urls) {
            int br = urls.length;
            // šaljemo samo 1 parametar (1 URL), iako metoda može primiti polje parametara
            HttpURLConnection conn = null;
            try {
                // povezujemo se sa zadanim URL-om pomoću GET metode
                conn = (HttpURLConnection)new URL(urls[0]).openConnection();
                // postavljamo kodnu stranicu da bi se znakovi prikazali ispravno
                conn.setRequestProperty("Accept-Charset", "UTF-8");
                // koristimo HTTP GET metodu za dohvat
                conn.setRequestMethod("POST");

                conn.setReadTimeout(20000);
                conn.setConnectTimeout(20000);
                conn.setDoInput(true);
                conn.setDoOutput(true);

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter( new OutputStreamWriter(os, "UTF-8"));
                writer.write(pars);
                writer.flush();
                writer.close();
                os.close();

                int responseCode=conn.getResponseCode();



                // dohvaćamo podatke u obliku ulaznog niza
                // ako su podaci uredno dohvaćeni (HTTP kod 200)
                if (conn.getResponseCode() == 200) {
                    // pretvaramo ulazni InputStream u String
                    String res = inputStreamToString(conn.getInputStream());
                    // parsiramo podatke JSON formatu u objekt tipa Users
                    Gson gson = new Gson();
                    int id = gson.fromJson(res, int.class);
                    // metodi onPostExecute šalje se polje objekata tipa User kako bi se
                    // lista popunila podacima pročitanih korisnika
                    return id;
                }else {
                    // Inače se vratila greška, pa dohvati poruku greške i pretvori ju u String
                    // Koristi se ErrorStream, ane InputStream koji vraća web servis i pretvaramo ga u JSON String
                    String res = inputStreamToString(conn.getErrorStream());
                    // parsiramo podatke JSON formata u objekt tipa Greska
                    //Gson gson = new Gson();
                    // Ažuriramo informaciju o grešci, ako se dogodila
                    //activityReference.get().err = gson.fromJson(res, Greska.class);
                    return null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch(Exception e){
                e.printStackTrace();
            } finally{
                if (conn != null)
                    conn.disconnect();
            }
            return null;
        }

        /*
    Pomoćna metoda koja dohvaća String iz primljenog input ili error streama
        */
        private String inputStreamToString(InputStream is){
            Scanner s = new Scanner(is).useDelimiter("\\A");
            String res = s.hasNext() ? s.next() : "";
            s.close();
            return res;
        }



        @Override
        // metoda prima polje objekata tipa User
        protected void onPostExecute(Integer rez){

            // get a reference to the activity if it is still there
            IzborActivity activity = activityReference.get();
            if (activity == null || activity.isFinishing()) return;
            // Dogodila se greška kod dohvata
            if (rez == null){
                Toast.makeText(activity, "Greška dohvata!", Toast.LENGTH_LONG).show();
                return;
            }
            // Inače ažuriraj listu
            //activity.kor = rez;
            // nakon što dohvati podatke, stvara se adapter za pregled
                Toast.makeText(activity, "Slanje poruke uspješno! " + rez,Toast.LENGTH_LONG).show();
            }
    }
    private String encodeParams() throws Exception{
        StringBuilder sb = new StringBuilder();
        sb.append(URLEncoder.encode("kome", "UTF-8"));
        sb.append("=");
        sb.append(URLEncoder.encode(p.getKome(), "UTF-8"));
        sb.append("&");
        sb.append(URLEncoder.encode("odkoga", "UTF-8"));
        sb.append("=");
        sb.append(URLEncoder.encode(p.getOdkoga(), "UTF-8"));
        sb.append("&");
        sb.append(URLEncoder.encode("poruka", "UTF-8"));
        sb.append("=");
        sb.append(URLEncoder.encode(p.getPoruka(), "UTF-8"));
        return sb.toString();
    }
}
