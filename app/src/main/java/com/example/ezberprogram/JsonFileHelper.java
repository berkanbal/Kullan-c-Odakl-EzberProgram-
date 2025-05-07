package com.example.ezberprogram;

import android.content.Context;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class JsonFileHelper {
    private final String fileName;

    public JsonFileHelper(String listName) {
        this.fileName = listName + ".json";
    }

    public void saveData(Context context, String imagePath, String description) {
        try {
            JSONObject jsonObject = readData(context); // Önce mevcut verileri oku

            // Yeni veriyi ekle (file:// ön eki eklenmemeli, Uri zaten tam yolu verir)
            jsonObject.put(imagePath, description);

            // Dosyaya tüm içeriği yeniden yaz
            FileOutputStream fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            fos.write(jsonObject.toString().getBytes());
            fos.close();
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
    }

    public JSONObject readData(Context context) {
        JSONObject jsonObject = new JSONObject();
        try {
            FileInputStream fis = context.openFileInput(fileName);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fis));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }
            jsonObject = new JSONObject(sb.toString());
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public String getData(Context context, String key) {
        try {
            JSONObject jsonObject = readData(context);
            return jsonObject.getString(key);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}