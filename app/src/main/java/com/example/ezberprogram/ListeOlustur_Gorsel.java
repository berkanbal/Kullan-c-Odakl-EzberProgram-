package com.example.ezberprogram;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ListeOlustur_Gorsel extends AppCompatActivity {

    Button gorselEkleBtn,ekleBtn,kaydetBtn;
    ImageView imageView;
    TextView listAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_liste_olustur_gorsel);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        tanimlama();

        //Liste Adı Çekme
        String grslListeAd = getIntent().getStringExtra("grsl_list_adi");
        listAd.setText(grslListeAd);

        gorselEkleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent GorselSec = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                GorselSec.setType("image/*");
                startActivityForResult(GorselSec, 100);
            }
        });




    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();
            if (selectedImageUri != null) {
                imageView.setImageURI(selectedImageUri);
                //saveImageLocally(selectedImageUri);
            }
        }
    }



    public void tanimlama(){
        gorselEkleBtn = findViewById(R.id.gorseleklebtn);
        ekleBtn = findViewById(R.id.button5);
        kaydetBtn = findViewById(R.id.button6);
        imageView = findViewById(R.id.imageView);
        listAd = findViewById(R.id.textView3);

    }
}