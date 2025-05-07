package com.example.ezberprogram;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class ListeCalisma extends AppCompatActivity {
    Button snrBtn, kntrlBtn;
    TextView anlamKutu, anlamGirKutu, listeAdi;
    ImageView resimKutu;
    private Map<String, String> dataMap = new HashMap<>();
    private List<String> keysList = new ArrayList<>();
    private Random random = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liste_calisma);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }

        tanimlama();

        // Başlangıçta anlamKutu'yu görünmez yap
        anlamKutu.setVisibility(View.INVISIBLE);

        // Intent'ten liste adını al
        String selectedListName = getIntent().getStringExtra("SELECTED_LIST_NAME");
        if (selectedListName != null && !selectedListName.isEmpty()) {
            listeAdi.setText(selectedListName);
            loadJsonFromFile();
        } else {
            listeAdi.setText("Liste adı bulunamadı");
        }

        snrBtn.setOnClickListener(v -> showRandomData());

        // Kontrol butonu işlevi
        kntrlBtn.setOnClickListener(v -> {
            String girilenCevap = anlamGirKutu.getText().toString().trim();
            String dogruCevap = anlamKutu.getText().toString().trim();

            if (girilenCevap.equals(dogruCevap)) {
                anlamKutu.setVisibility(View.VISIBLE);
                showAlert("Doğru Cevap!");
            } else {
                showAlert("Yanlış Cevap!");
            }
        });

        showRandomData();
    }

    private void showAlert(String message) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("Tamam", null)
                .show();
    }

    // Diğer metodlar aynı şekilde kalacak...
    private void loadJsonFromFile() {
        try {
            File file = new File(getFilesDir(), listeAdi.getText().toString() + ".json");
            Log.d("JSON_DOSYA", "Dosya yolu: " + file.getAbsolutePath());

            if (!file.exists()) {
                Log.e("JSON_HATA", "Dosya bulunamadı!");
                runOnUiThread(() -> Toast.makeText(this, "Liste dosyası bulunamadı!", Toast.LENGTH_LONG).show());
                return;
            }

            FileInputStream fis = new FileInputStream(file);
            byte[] buffer = new byte[(int) file.length()];
            fis.read(buffer);
            fis.close();

            String jsonStr = new String(buffer, StandardCharsets.UTF_8);
            JSONObject jsonObject = new JSONObject(jsonStr);

            dataMap.clear();
            keysList.clear();

            Iterator<String> keys = jsonObject.keys();
            while (keys.hasNext()) {
                String key = keys.next();
                String value = jsonObject.getString(key);
                dataMap.put(key, value);
                keysList.add(key);
                Log.d("JSON_VERI", "Key: " + key + ", Value: " + value);
            }

        } catch (Exception e) {
            Log.e("JSON_HATA", "loadJsonFromFile: " + e.getMessage());
            e.printStackTrace();
            runOnUiThread(() -> Toast.makeText(this, "Liste yüklenirken hata: " + e.getMessage(), Toast.LENGTH_LONG).show());
        }
    }

    private void showRandomData() {
        if (keysList.isEmpty()) {
            Log.e("HATA", "keysList boş!");
            return;
        }

        new Thread(() -> {
            try {
                int index = random.nextInt(keysList.size());
                String imagePathUri = keysList.get(index);
                String value = dataMap.get(imagePathUri);

                runOnUiThread(() -> {
                    anlamKutu.setText(value);
                    anlamKutu.setVisibility(View.INVISIBLE); // Yeni soruda tekrar gizle
                    anlamGirKutu.setText(""); // Giriş kutusunu temizle
                    loadImage(imagePathUri);
                });

            } catch (Exception e) {
                Log.e("HATA", "showRandomData: " + e.getMessage());
                runOnUiThread(() -> Toast.makeText(this, "Veri yüklenirken hata: " + e.getMessage(), Toast.LENGTH_SHORT).show());
            }
        }).start();
    }

    private void loadImage(String imagePathUri) {
        try {
            String cleanedPath = imagePathUri.replace("file:///", "/");
            File imgFile = new File(cleanedPath);

            if (imgFile.exists()) {
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(imgFile.getAbsolutePath(), options);

                options.inSampleSize = calculateInSampleSize(options, 100, 100);
                options.inJustDecodeBounds = false;

                Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath(), options);
                if (bitmap != null) {
                    resimKutu.setImageBitmap(bitmap);
                } else {
                    resimKutu.setImageResource(android.R.drawable.ic_menu_report_image);
                }
            } else {
                resimKutu.setImageResource(android.R.drawable.ic_menu_report_image);
            }
        } catch (Exception e) {
            Log.e("RESIM_HATA", "loadImage: " + e.getMessage());
            resimKutu.setImageResource(android.R.drawable.ic_menu_report_image);
        }
    }

    public int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            while ((halfHeight / inSampleSize) > reqHeight && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        Log.d("RESİM YÜKLEME", "inSampleSize: " + inSampleSize);
        return inSampleSize;
    }

    public void tanimlama() {
        anlamKutu = findViewById(R.id.textView5);
        anlamGirKutu = findViewById(R.id.editTextText3);
        kntrlBtn = findViewById(R.id.button12);
        snrBtn = findViewById(R.id.button10);
        listeAdi = findViewById(R.id.textView6);
        resimKutu = findViewById(R.id.imageView2);
    }
}