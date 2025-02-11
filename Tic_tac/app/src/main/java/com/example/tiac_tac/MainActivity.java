package com.example.tiac_tac;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private TextView tvCronometre;
    private TextView tvHores;
    private View cronometreCircle;
    private Button btnIniciar;
    private Button btnPausar;
    private Button btnParar;

    private final Handler handler = new Handler();
    private boolean isRunning = false;
    private boolean isPaused = false;
    private long startTime = 0L;
    private String userData;
    private String horarisData;

    private LocationManager locationManager;
    private LocationListener locationListener;
    private Location currentLocation;

    private BroadcastReceiver timeUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (ChronometerService.BROADCAST_ACTION.equals(intent.getAction())) {
                int hours = intent.getIntExtra("hours", 0);
                int minutes = intent.getIntExtra("minutes", 0);
                int seconds = intent.getIntExtra("seconds", 0);
                tvCronometre.setText(String.format("%02d:%02d:%02d", hours, minutes, seconds));
            }
        }
    };

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Enllaça les vistes
        TextView tvNomCognoms = findViewById(R.id.tvNomCognoms);
        tvCronometre = findViewById(R.id.tvCronometre);
        tvHores = findViewById(R.id.tvHores);
        cronometreCircle = findViewById(R.id.cronometreCircle);
        btnIniciar = findViewById(R.id.btnIniciar);
        btnPausar = findViewById(R.id.btnPausar);
        btnParar = findViewById(R.id.btnParar);

        // Deshabilita el botó d'iniciar fins que es trobi la ubicació
        btnIniciar.setEnabled(false);

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
            public void onLocationChanged(@NonNull Location location) {
                currentLocation = location;
                Log.d("MainActivity", "Location updated: " + location.getLatitude() + ", " + location.getLongitude());
                // Habilita el botó d'iniciar quan es trobi la ubicació
                btnIniciar.setEnabled(true);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {}

            @Override
            public void onProviderEnabled(@NonNull String provider) {}

            @Override
            public void onProviderDisabled(@NonNull String provider) {}
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

        // Mostrar les hores que li toquen aquell dia
        mostrarHoresDelDia();

        // Configura la barra de navegació inferior
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.nav_inici); // Selecciona l'element d'incidència
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_inici) {
                // Redirigeix a l'activitat principal
                Intent intentMain = new Intent(MainActivity.this, MainActivity.class);
                intentMain.putExtra("user_data", userData);
                intentMain.putExtra("horaris_data", horarisData);
                startActivity(intentMain);
                return true;
            } else if (id == R.id.nav_horari) {
                // Redirigeix a l'activitat d'horari
                Intent intentHorari = new Intent(MainActivity.this, HorariActivity.class);
                intentHorari.putExtra("user_data", userData);
                intentHorari.putExtra("horaris_data", horarisData);
                startActivity(intentHorari);
                return true;
            } else if (id == R.id.nav_profile) {
                // Redirigeix a l'activitat de perfil
                Intent intentProfile = new Intent(MainActivity.this, Perfil.class);
                intentProfile.putExtra("user_data", userData);
                intentProfile.putExtra("horaris_data", horarisData);
                startActivity(intentProfile);
                return true;
            } else if (id == R.id.nav_incidencia) {
                // Redirigeix a l'activitat d'incidència
                Intent intentIncidencia = new Intent(MainActivity.this, Incidencia.class);
                intentIncidencia.putExtra("user_data", userData);
                intentIncidencia.putExtra("horaris_data", horarisData);
                startActivity(intentIncidencia);
                return true;
            } else {
                return false;
            }
        });

        // Registra el receptor per actualitzar el cronòmetre
        registerReceiver(timeUpdateReceiver, new IntentFilter(ChronometerService.BROADCAST_ACTION));

        // Restaurar l'estat del cronòmetre
        restoreChronometerState();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Obtenir les dades de l'usuari de l'intent
        Intent intent = getIntent();
        userData = intent.getStringExtra("user_data");
        horarisData = intent.getStringExtra("horaris_data");

        // Mostrar les hores que li toquen aquell dia
        mostrarHoresDelDia();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Desregistra el receptor per evitar fuites de memòria
        unregisterReceiver(timeUpdateReceiver);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
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

    private void mostrarHoresDelDia() {
        try {
            JSONArray horarisArray = new JSONArray(horarisData);
            StringBuilder horesDelDia = new StringBuilder();

            // Obtenir el dia de la setmana actual
            Calendar calendar = Calendar.getInstance();
            int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
            String diaSetmana = convertirDiaCatala(dayOfWeek);

            // Iterar sobre les dades de l'horari
            for (int i = 0; i < horarisArray.length(); i++) {
                JSONObject horari = horarisArray.getJSONObject(i);
                String dia = horari.getString("dia");
                String horaInicio = horari.getString("hora_inicio").substring(0, 5); // Format HH:MM
                String horaFin = horari.getString("hora_fin").substring(0, 5); // Format HH:MM

                // Comparar el dia de la setmana
                if (dia.equals(diaSetmana)) {
                    horesDelDia.append(horaInicio).append(" - ").append(horaFin).append("\n");
                }
            }

            tvHores.setText(horesDelDia.toString());
        } catch (Exception e) {
            Log.e("MainActivity", "Error en el format de les dades de l'horari: " + e.getMessage());
            Toast.makeText(this, "Error en el format de les dades de l'horari", Toast.LENGTH_SHORT).show();
        }
    }

    private String convertirDiaCatala(int dayOfWeek) {
        switch (dayOfWeek) {
            case Calendar.MONDAY:
                return "DILLUNS";
            case Calendar.TUESDAY:
                return "DIMARTS";
            case Calendar.WEDNESDAY:
                return "DIMECRES";
            case Calendar.THURSDAY:
                return "DIJOUS";
            case Calendar.FRIDAY:
                return "DIVENDRES";
            case Calendar.SATURDAY:
                return "DISSABTE";
            case Calendar.SUNDAY:
                return "DIUMENGE";
            default:
                return "";
        }
    }

    private void iniciarCronometre() {
        if (!isRunning) {
            isRunning = true;
            isPaused = false;
            cronometreCircle.setBackgroundResource(R.drawable.circle_running);
            guardarFitxatge("entrada");

            // Start the ChronometerService
            Intent serviceIntent = new Intent(this, ChronometerService.class);
            serviceIntent.setAction("START");
            startService(serviceIntent);
        }
    }

    private void pausarCronometre() {
        if (isRunning && !isPaused) {
            isPaused = true;
            cronometreCircle.setBackgroundResource(R.drawable.circle_paused);
        } else if (isRunning) {
            isPaused = false;
            cronometreCircle.setBackgroundResource(R.drawable.circle_running);
        }
    }

    private void pararCronometre() {
        if (isRunning) {
            isRunning = false;
            isPaused = false;
            cronometreCircle.setBackgroundResource(R.drawable.circle_stopped);
            guardarFitxatge("sortida");

            // Stop the ChronometerService
            Intent serviceIntent = new Intent(this, ChronometerService.class);
            serviceIntent.setAction("STOP");
            startService(serviceIntent);
        }
    }

    private void restoreChronometerState() {
        // Recupera l'hora d'inici del cronòmetre des de la base de dades
        String url = Ip.FITXAR_URL;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            if (jsonResponse.getBoolean("isRunning")) {
                                String horaInici = jsonResponse.getString("hora_inici");
                                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
                                Date date = sdf.parse(horaInici);
                                startTime = date.getTime();
                                long elapsedTime = SystemClock.elapsedRealtime() - startTime;
                                int seconds = (int) (elapsedTime / 1000);
                                int minutes = seconds / 60;
                                int hours = minutes / 60;
                                seconds = seconds % 60;
                                minutes = minutes % 60;
                                tvCronometre.setText(String.format("%02d:%02d:%02d", hours, minutes, seconds));
                                cronometreCircle.setBackgroundResource(R.drawable.circle_running);
                                isRunning = true;
                                handler.postDelayed(updateTimerThread, 1000);
                            } else {
                                cronometreCircle.setBackgroundResource(R.drawable.circle_stopped);
                                isRunning = false;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("MainActivity", "Error en la connexió: " + error.getMessage());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("action", "restore");
                params.put("usuari_id", getUserIdFromUserData());
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private String getUserIdFromUserData() {
        try {
            JSONObject userJson = new JSONObject(userData);
            return userJson.getString("id_usuari");
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

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

    private final Runnable updateTimerThread = new Runnable() {
        public void run() {
            long timeInMillis = SystemClock.elapsedRealtime() - startTime;
            int seconds = (int) (timeInMillis / 1000);
            int minutes = seconds / 60;
            int hours = minutes / 60;
            seconds = seconds % 60;
            minutes = minutes % 60;
            tvCronometre.setText(String.format("%02d:%02d:%02d", hours, minutes, seconds));
            handler.postDelayed(this, 1000);
        }
    };
}