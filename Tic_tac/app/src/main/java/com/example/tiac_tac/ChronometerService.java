package com.example.tiac_tac;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.SystemClock;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ChronometerService extends Service {

    public static final String BROADCAST_ACTION = "com.example.tiac_tac.UPDATE_TIME";
    private Handler handler = new Handler();
    private long startTime = 0L;
    private boolean isRunning = false;

    @Override
    public void onCreate() {
        super.onCreate();
        Toast.makeText(this, "Chronometer Service Started", Toast.LENGTH_SHORT).show();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null && intent.getAction() != null) {
            switch (intent.getAction()) {
                case "START":
                    if (!isRunning) {
                        startTime = SystemClock.elapsedRealtime();
                        handler.postDelayed(updateTimerThread, 0);
                        isRunning = true;
                        saveStartTimeToDatabase();
                    }
                    break;
                case "STOP":
                    handler.removeCallbacks(updateTimerThread);
                    isRunning = false;
                    stopSelf();
                    clearStartTimeFromDatabase();
                    break;
            }
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(updateTimerThread);
        Toast.makeText(this, "Chronometer Service Stopped", Toast.LENGTH_SHORT).show();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private Runnable updateTimerThread = new Runnable() {
        public void run() {
            long timeInMillis = SystemClock.elapsedRealtime() - startTime;
            int seconds = (int) (timeInMillis / 1000);
            int minutes = seconds / 60;
            int hours = minutes / 60;
            seconds = seconds % 60;
            minutes = minutes % 60;

            // Envia una emissió amb el temps actualitzat
            Intent intent = new Intent(BROADCAST_ACTION);
            intent.putExtra("hours", hours);
            intent.putExtra("minutes", minutes);
            intent.putExtra("seconds", seconds);
            sendBroadcast(intent);

            handler.postDelayed(this, 1000);
        }
    };

    private void saveStartTimeToDatabase() {
        String url = Ip.FITXAR_URL;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Gestió de la resposta del servidor
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Gestió de l'error
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
                String startTimeString = sdf.format(startTime);
                params.put("startTime", startTimeString);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void clearStartTimeFromDatabase() {
        String url = Ip.FITXAR_URL;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Gestió de la resposta del servidor
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Gestió de l'error
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("clearStartTime", "true");
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}