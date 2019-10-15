package hr.unidu.kz.msr_zadatak_04;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class IzborActivity extends AppCompatActivity {
    private TextView pozdrav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_izbor);
        pozdrav = findViewById(R.id.poruka);
        String kome="", odkoga="";
        Intent intent = getIntent();
        if (intent.hasExtra("kome"))
            kome = intent.getStringExtra("kome");
        if (intent.hasExtra("odkoga"))
            odkoga = intent.getStringExtra("odkoga");
        String poruka = pozdravi(kome);
        Poruka p = new Poruka(0,odkoga,kome, poruka);
        try {
            new NovaPoruka(p, this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public String pozdravi(String ime) {
        TimeZone tz = TimeZone.getDefault();
        Calendar sada = new GregorianCalendar(tz);
        int sat = sada.get(sada.HOUR_OF_DAY);
        String poruka;
        if (sat >= 22 || sat < 6)
            poruka = "Laku noć ";
        else if (sat >= 6 && sat < 12)
            poruka = "Dobro jutro ";
        else if (sat >= 12 && sat < 17)
            poruka = "Dobar dan ";
        else
            poruka = "Dobra večer ";
        return poruka;

    }
}
