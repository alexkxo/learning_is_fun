package ca.qc.bdeb.c5gm.lab2_kao;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private Button btn_dictaphone;
    private Button btn_galerie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // associations des btns
        btn_dictaphone = findViewById(R.id.btn_dictaphone);
        btn_galerie = findViewById(R.id.btn_galerie_photos);

        // btn listener pour btn dictaphone
        btn_dictaphone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, DictaphoneActivity.class);
                startActivity(intent);
                Toast.makeText(MainActivity.this, "Dictaphone", Toast.LENGTH_SHORT).show();
            }
        });

        // btn listener pour btn galerie
        btn_galerie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, GalerieActivity.class);
                startActivity(intent);
                Toast.makeText(MainActivity.this, "Galerie", Toast.LENGTH_SHORT).show();
            }
        });
    }
}