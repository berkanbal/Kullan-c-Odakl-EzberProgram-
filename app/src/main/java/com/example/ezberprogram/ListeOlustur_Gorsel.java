package com.example.ezberprogram;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

public class ListeOlustur_Gorsel extends AppCompatActivity {

    Button gorselEkleBtn, ekleBtn, kaydetBtn;
    ImageView imageView;
    TextView listAd;
    EditText anlamKutu;

    private Uri savedImageUri;
    private String grslListeAd; // Liste adını sınıf değişkeni olarak tanımladık

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
        grslListeAd = getIntent().getStringExtra("grsl_list_adi");
        listAd.setText(grslListeAd);

        gorselEkleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent GorselSec = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                GorselSec.setType("image/*");
                startActivityForResult(GorselSec, 100);
            }
        });

        ekleBtn.setOnClickListener(view -> {
            if (savedImageUri != null && !anlamKutu.getText().toString().isEmpty()) {
                JsonFileHelper processor = new JsonFileHelper(grslListeAd);
                processor.saveData(ListeOlustur_Gorsel.this, savedImageUri.toString(), anlamKutu.getText().toString());
                imageView.setImageDrawable(null);
                anlamKutu.setText("");
                savedImageUri = null;
                Toast.makeText(ListeOlustur_Gorsel.this, "Öğe eklendi", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(ListeOlustur_Gorsel.this, "Lütfen resim seçin ve anlam girin", Toast.LENGTH_SHORT).show();
            }
        });

        // Kaydet butonu onClick listener'ı
        kaydetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Liste kaydedildi mesajını göster
                Toast.makeText(ListeOlustur_Gorsel.this, "Liste Kaydedildi", Toast.LENGTH_SHORT).show();

                // Ana ekrana dön
                Intent intent = new Intent(ListeOlustur_Gorsel.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // Diğer aktiviteleri temizler
                startActivity(intent);
                finish(); // Bu aktiviteyi kapat
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();
            if (selectedImageUri != null) {
                try {
                    savedImageUri = saveImageToInternalStorage(selectedImageUri);
                    imageView.setImageURI(savedImageUri);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Resim kaydedilirken hata oluştu", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private Uri saveImageToInternalStorage(Uri imageUri) throws Exception {
        InputStream inputStream = getContentResolver().openInputStream(imageUri);
        String fileName = "image_" + System.currentTimeMillis() + ".jpg";
        File file = new File(getFilesDir(), fileName);
        FileOutputStream outputStream = new FileOutputStream(file);

        byte[] buffer = new byte[1024];
        int length;
        while ((length = inputStream.read(buffer)) > 0) {
            outputStream.write(buffer, 0, length);
        }

        outputStream.close();
        inputStream.close();

        return Uri.fromFile(file);
    }

    public void tanimlama(){
        gorselEkleBtn = findViewById(R.id.gorseleklebtn);
        ekleBtn = findViewById(R.id.button5);
        kaydetBtn = findViewById(R.id.button6);
        imageView = findViewById(R.id.imageView);
        listAd = findViewById(R.id.textView3);
        anlamKutu = findViewById(R.id.editTextText5);
    }
}