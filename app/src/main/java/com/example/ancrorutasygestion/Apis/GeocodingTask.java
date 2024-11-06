package com.example.ancrorutasygestion.Apis;
import android.os.AsyncTask;
import android.util.Log;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
public class GeocodingTask extends AsyncTask<String, Void, String> {

private static final String TAG = "GeocodingTask";

private OnTaskCompleted listener;

public interface OnTaskCompleted {
    void onTaskCompleted(String result);
}

    public GeocodingTask(OnTaskCompleted listener) {
        this.listener = listener;
    }

    @Override
    protected String doInBackground(String... params) {
        String latitude = params[0];
        String longitude = params[1];

        StringBuilder result = new StringBuilder();
        try {
            URL url = new URL("https://nominatim.openstreetmap.org/reverse?format=json&lat=" + latitude + "&lon=" + longitude);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);

            }

        } catch (IOException e) {
            Log.e(TAG, "Error al realizar la solicitud HTTP: " + e.getMessage());
        }
        return result.toString();
    }

    @Override
    protected void onPostExecute(String result) {
        try {
            JSONObject json = new JSONObject(result);
            String address = json.getString("display_name");
            listener.onTaskCompleted(address);
        } catch (JSONException e) {
            Log.e(TAG, "Error al parsear el JSON: " + e.getMessage());
            listener.onTaskCompleted("No se pudo obtener la dirección");
        }
    }
}