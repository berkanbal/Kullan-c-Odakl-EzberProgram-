package com.example.ezberprogram;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Listeler extends AppCompatActivity {

    ListView listeAdlari ;
    List<String> jsonFiles;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.listeler);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        tanimlama();
        jsonFiles = new ArrayList<>();  // jsonFiles listesini başlat

        File dir = getFilesDir();
        File[] files = dir.listFiles();

        if (files != null) {
            for (File file : files) {
                if (file.getName().endsWith(".json")) {  // Sadece JSON dosyalarını ekle
                    String fileNameWithoutExtension = file.getName().replace(".json", "");
                    jsonFiles.add(fileNameWithoutExtension);
                }
            }
        }

        if (jsonFiles.isEmpty()) {
            Toast.makeText(this, "Hiç JSON dosyası bulunamadı!", Toast.LENGTH_SHORT).show();
        }

        // ListView'e verileri bağla
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, jsonFiles);
        listeAdlari.setAdapter(adapter);
    }



    public void tanimlama(){
        listeAdlari = findViewById(R.id.jsonlist);
    }
}