package com.example.ezberprogram;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.File;
import java.io.FileOutputStream;

public class ListeOlustur extends AppCompatActivity {
    Button btn,btn1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_liste_olustur);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.listeOlustur), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        tanimlama();

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                kelimeDialog();
            }
        });

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gorselDialog();
            }
        });


    }

    public void kelimeDialog(){
        LayoutInflater layoutInflater = getLayoutInflater();
        View kelimeview = layoutInflater.inflate(R.layout.kelime_liste_adi,null);
        EditText kelimeListAd = kelimeview.findViewById(R.id.editTextText6);
        Button klmKydBtn = kelimeview.findViewById(R.id.button8);
        Button klmIptBtn = kelimeview.findViewById(R.id.button9);

        AlertDialog.Builder builder = new AlertDialog.Builder(ListeOlustur.this);
        builder.setView(kelimeview);
        builder.setCancelable(true);

        AlertDialog dialog = builder.create();

        klmKydBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Liste Adı Yollama
                final String klmListeAd = kelimeListAd.getText().toString();

                Intent KelimeListeOlustur = new Intent(getApplicationContext(),ListeOlustur_Kelime.class);
                KelimeListeOlustur.putExtra("klm_list_adi",klmListeAd);
                startActivity(KelimeListeOlustur);
            }
        });

        klmIptBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });

        dialog.show();

    }



    public void gorselDialog(){
        LayoutInflater layoutInflater = getLayoutInflater();
        View gorselview = layoutInflater.inflate(R.layout.gorsel_liste_adi,null);
        EditText gorselListAd = gorselview.findViewById(R.id.editTextText4);
        Button glaKydBtn = gorselview.findViewById(R.id.button4);
        Button glaIptBtn = gorselview.findViewById(R.id.button7);

        AlertDialog.Builder builder = new AlertDialog.Builder(ListeOlustur.this);
        builder.setView(gorselview);
        builder.setCancelable(true);

        AlertDialog dialog = builder.create();

        glaKydBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Liste Adı Yollama
                final String grslListeAd = gorselListAd.getText().toString();

                Intent GorselListeOlustur = new Intent(getApplicationContext(),ListeOlustur_Gorsel.class);
                GorselListeOlustur.putExtra("grsl_list_adi",grslListeAd);

                //JSON Dosyası Oluşturma
                String listJsonAd = gorselListAd.getText().toString() + ".json";
                File file = new File(getFilesDir(), listJsonAd);
                if (file.exists()) {
                    Toast.makeText(ListeOlustur.this, "Dosya zaten mevcut!", Toast.LENGTH_SHORT).show();
                }
                else {
                    try {
                        FileOutputStream fos = openFileOutput(listJsonAd, MODE_PRIVATE);
                        fos.write("{}".getBytes()); // Boş JSON nesnesi yaz
                        fos.close();

                        Toast.makeText(ListeOlustur.this, "Liste oluşturuldu!", Toast.LENGTH_SHORT).show();
                        startActivity(GorselListeOlustur);
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(ListeOlustur.this, "Bir hata oluştu!", Toast.LENGTH_SHORT).show();
                    }


                }

            }
        });

        glaIptBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });

        dialog.show();

    }



    public void tanimlama(){
        btn = findViewById(R.id.KelimeListe_btn);
        btn1 = findViewById(R.id.GorselListe_btn);
    }
}