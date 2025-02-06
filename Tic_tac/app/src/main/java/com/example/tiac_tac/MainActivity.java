package com.example.tiac_tac;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private TextView tvNomCognoms, tvCronometre, tvHores;
    private View cronometreCircle;
    private Button btnIniciar, btnPausar, btnParar;

    private Handler handler = new Handler();
    private long startTime = 0L;
    private boolean isRunning = false;
    private boolean isPaused = false;
    private String userData;
    private String horarisData;

    private LocationManager locationManager;
    private LocationListener locationListener;
    private Location currentLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Enllaça les vistes
        tvNomCognoms = findViewById(R.id.tvNomCognoms);
        tvCronometre = findViewById(R.id.tvCronometre);
        tvHores = findViewById(R.id.tvHores);
        cronometreCircle = findViewById(R.id.cronometreCircle);
        btnIniciar = findViewById(R.id.btnIniciar);
        btnPausar = findViewById(R.id.btnPausar);
        btnParar = findViewById(R.id.btnParar);

        // Obtenir les dades de l'usuari de l'intent
        Intent intent = getIntent();
        userData = intent.getStringExtra("user_data");
        horarisData = intent.getStringExtra("horaris_data");

        // Mostrar les dades de l'usuari (exemple)
        try {
            JSONObject jsonResponse = new JSONObject(userData);
            String nom = jsonResponse.getString("nom");
            String cognoms = jsonResponse.getString("cognoms");
            tvNomCognoms.setText(nom + " " + cognoms);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Configura el LocationManager
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                currentLocation = location;
                Log.d("MainActivity", "Location updated: " + location.getLatitude() + ", " + location.getLongitude());
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {}

            @Override
            public void onProviderEnabled(String provider) {}

            @Override
            public void onProviderDisabled(String provider) {}
        };

        // Comprova els permisos de localització
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.d("MainActivity", "Requesting location permissions");
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        } else {
            Log.d("MainActivity", "Location permissions already granted");
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        }

        // Configura els botons del cronòmetre
        btnIniciar.setOnClickListener(v -> iniciarCronometre());
        btnPausar.setOnClickListener(v -> pausarCronometre());
        btnParar.setOnClickListener(v -> pararCronometre());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d("MainActivity", "Location permissions granted");
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                }
            } else {
                Log.d("MainActivity", "Location permissions denied");
                Toast.makeText(this, "Permís de localització denegat", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void iniciarCronometre() {
        if (!isRunning) {
            startTime = System.currentTimeMillis();
            handler.postDelayed(updateTimerThread, 0);
            isRunning = true;
            isPaused = false;
            cronometreCircle.setBackgroundResource(R.drawable.circle_running);
            guardarFitxatge("entrada");
        }
    }

    private void pausarCronometre() {
        if (isRunning && !isPaused) {
            handler.removeCallbacks(updateTimerThread);
            isPaused = true;
            cronometreCircle.setBackgroundResource(R.drawable.circle_paused);
        } else if (isRunning && isPaused) {
            handler.postDelayed(updateTimerThread, 0);
            isPaused = false;
            cronometreCircle.setBackgroundResource(R.drawable.circle_running);
        }
    }

    private void pararCronometre() {
        if (isRunning) {
            handler.removeCallbacks(updateTimerThread);
            isRunning = false;
            isPaused = false;
            cronometreCircle.setBackgroundResource(R.drawable.circle_stopped);
            guardarFitxatge("sortida");
        }
    }

    private Runnable updateTimerThread = new Runnable() {
        public void run() {
            long timeInMillis = System.currentTimeMillis() - startTime;
            int seconds = (int) (timeInMillis / 1000);
            int minutes = seconds / 60;
            int hours = minutes / 60;
            seconds = seconds % 60;
            minutes = minutes % 60;
            tvCronometre.setText(String.format("%02d:%02d:%02d", hours, minutes, seconds));
            handler.postDelayed(this, 1000);
        }
    };

    private void guardarFitxatge(String tipus) {
        try {
            JSONObject userJson = new JSONObject(userData);
            int usuariId = userJson.getInt("id_usuari");

            StringRequest stringRequest = new StringRequest(Request.Method.POST, Ip.FITXAR_URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("MainActivity", "Resposta del servidor: " + response);
                            if (response.equals("success")) {
                                Toast.makeText(MainActivity.this, "Fitxatge guardat correctament", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(MainActivity.this, "Error en guardar el fitxatge", Toast.LENGTH_SHORT).show();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("MainActivity", "Error en la connexió: " + error.getMessage());
                            Toast.makeText(MainActivity.this, "Error en la connexió", Toast.LENGTH_SHORT).show();
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("usuari_id", String.valueOf(usuariId));
                    params.put("tipus", tipus);
                    try {
                        if (currentLocation != null) {
                            String gps = currentLocation.getLatitude() + "," + currentLocation.getLongitude();
                            params.put("gps", gps);
                            Log.d("MainActivity", "GPS data: " + gps);
                        } else {
                            params.put("gps", "No GPS");
                            Log.d("MainActivity", "No GPS data available");
                        }
                    } catch (Exception e) {
                        Log.e("MainActivity", "Error obtenint la ubicació GPS: " + e.getMessage());
                        params.put("gps", "Error GPS");
                    }
                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);
        } catch (Exception e) {
            Log.e("MainActivity", "Error en obtenir les dades de l'usuari: " + e.getMessage());
            Toast.makeText(this, "Error en obtenir les dades de l'usuari", Toast.LENGTH_SHORT).show();
        }
    }
}