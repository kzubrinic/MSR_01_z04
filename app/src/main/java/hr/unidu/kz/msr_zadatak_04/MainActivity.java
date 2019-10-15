package hr.unidu.kz.msr_zadatak_04;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private EditText ime;
    private TextView sve_poruke;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ime = findViewById(R.id.ime);
        sve_poruke = findViewById(R.id.sve_poruke);
    }

    public void dohvati(View view) {
        sve_poruke.setText("");
        if (ime.getText().toString().length() == 0){
            sve_poruke.setText("Morate unijeti vaše ime!");
            return;
        }
        new DohvatPoruka(ime.getText().toString(), this);
    }

    public void pozdravi(View view) {
        Intent intent = new Intent(this, PregledActivity.class);
        intent.putExtra("ime", ime.getText().toString());
        startActivity(intent);
    }
    public void addPoruka(String red){
        if(sve_poruke.getText().length() > 0)
            sve_poruke.append("\n");
        sve_poruke.append(red);

    }
}
