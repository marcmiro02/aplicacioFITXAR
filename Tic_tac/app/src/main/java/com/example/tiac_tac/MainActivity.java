package com.example.tiac_tac;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
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

    private TextView tvHoraInici;
    private TextView tvHoraSortida;
    private TextView tvHores;
    private Button btnIniciar;
    private Button btnParar;

    private boolean isRunning = false;
    private String userData;
    private String horarisData;
    private Location currentLocation;
    private LocationManager locationManager;
    private LocationListener locationListener;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Enllaça les vistes
        TextView tvNomCognoms = findViewById(R.id.tvNomCognoms);
        tvHoraInici = findViewById(R.id.tvHoraInici);
        tvHoraSortida = findViewById(R.id.tvHoraSortida);
        tvHores = findViewById(R.id.tvHores);
        btnIniciar = findViewById(R.id.btnIniciar);
        btnParar = findViewById(R.id.btnParar);

        // Deshabilita el botó d'iniciar fins que es trobi la ubicació
        btnIniciar.setEnabled(false);
        btnParar.setEnabled(false);

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

        // Configura els botons
        btnIniciar.setOnClickListener(v -> iniciarCronometre());
        btnParar.setOnClickListener(v -> pararCronometre());

        // Mostrar les hores que li toquen aquell dia
        mostrarHoresDelDia();

        // Configura la barra de navegació inferior
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.nav_inici);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_inici) {
                Intent intentMain = new Intent(MainActivity.this, MainActivity.class);
                intentMain.putExtra("user_data", userData);
                intentMain.putExtra("horaris_data", horarisData);
                startActivity(intentMain);
                return true;
            } else if (id == R.id.nav_horari) {
                Intent intentHorari = new Intent(MainActivity.this, HorariActivity.class);
                intentHorari.putExtra("user_data", userData);
                intentHorari.putExtra("horaris_data", horarisData);
                startActivity(intentHorari);
                return true;
            } else if (id == R.id.nav_profile) {
                Intent intentProfile = new Intent(MainActivity.this, Perfil.class);
                intentProfile.putExtra("user_data", userData);
                intentProfile.putExtra("horaris_data", horarisData);
                startActivity(intentProfile);
                return true;
            } else if (id == R.id.nav_incidencia) {
                Intent intentIncidencia = new Intent(MainActivity.this, Incidencia.class);
                intentIncidencia.putExtra("user_data", userData);
                intentIncidencia.putExtra("horaris_data", horarisData);
                startActivity(intentIncidencia);
                return true;
            } else {
                return false;
            }
        });
    }

    private void iniciarCronometre() {
        if (!isRunning) {
            isRunning = true;
            btnIniciar.setVisibility(View.GONE);
            btnParar.setVisibility(View.VISIBLE);
            btnParar.setEnabled(true);
            tvHoraInici.setVisibility(View.VISIBLE);

            // Ocultar l'hora de sortida
            tvHoraSortida.setVisibility(View.GONE);

            // Mostrar l'hora d'inici immediatament
            String horaInici = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
            tvHoraInici.setText("Hora d'inici: " + horaInici);

            guardarFitxatge("entrada");
        }
    }

    private void pararCronometre() {
        if (isRunning) {
            isRunning = false;
            btnParar.setVisibility(View.GONE);
            btnIniciar.setVisibility(View.VISIBLE);
            btnIniciar.setEnabled(true);
            tvHoraSortida.setVisibility(View.VISIBLE);

            // Mostrar l'hora de sortida immediatament
            String horaSortida = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
            tvHoraSortida.setText("Hora de sortida: " + horaSortida);

            guardarFitxatge("sortida");
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
                                if (tipus.equals("entrada")) {
                                    // L'hora d'inici ja s'ha mostrat, no cal tornar a establir-la
                                } else {
                                    verificarHoresTreballades();
                                }
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
                        if (tipus.equals("sortida")) {
                            double horesTreballades = calcularHoresTreballades();
                            params.put("hores_treballades", String.valueOf(horesTreballades));
                        }
                    } catch (Exception e) {
                        Log.e("MainActivity", "Error obtenint la ubicació GPS: " + e.getMessage());
                        params.put("gps", "Error GPS");
                    }
                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
            requestQueue.add(stringRequest);
        } catch (Exception e) {
            Log.e("MainActivity", "Error en obtenir les dades de l'usuari: " + e.getMessage());
            Toast.makeText(this, "Error en obtenir les dades de l'usuari", Toast.LENGTH_SHORT).show();
        }
    }

    private double calcularHoresTreballades() {
        double horesTreballades = 0;
        try {
            // Obtenir les hores d'inici i sortida
            String horaInici = tvHoraInici.getText().toString().replace("Hora d'inici: ", "");
            String horaSortida = tvHoraSortida.getText().toString().replace("Hora de sortida: ", "");

            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
            Date dateInici = sdf.parse(horaInici);
            Date dateSortida = sdf.parse(horaSortida);

            // Obtenir les hores esperades des de tvHores
            String[] horesEsperadesArray = tvHores.getText().toString().split("\n");

            for (String horesEsperades : horesEsperadesArray) {
                String[] hores = horesEsperades.split(" - ");
                Date horaIniciEsperada = sdf.parse(hores[0] + ":00");
                Date horaFiEsperada = sdf.parse(hores[1] + ":00");

                // Calcular les hores treballades dins de l'interval esperat
                if (dateInici.before(horaFiEsperada) && dateSortida.after(horaIniciEsperada)) {
                    Date horaIniciTreballada = dateInici.after(horaIniciEsperada) ? dateInici : horaIniciEsperada;
                    Date horaFiTreballada = dateSortida.before(horaFiEsperada) ? dateSortida : horaFiEsperada;
                    long diff = horaFiTreballada.getTime() - horaIniciTreballada.getTime();
                    horesTreballades += (double) diff / (1000 * 60 * 60);
                }
            }
        } catch (Exception e) {
            Log.e("MainActivity", "Error en calcular les hores treballades: " + e.getMessage());
            Toast.makeText(this, "Error en calcular les hores treballades", Toast.LENGTH_SHORT).show();
        }
        return horesTreballades;
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

    private void verificarHoresTreballades() {
        try {
            // Obtenir les hores d'inici i sortida
            String horaInici = tvHoraInici.getText().toString().replace("Hora d'inici: ", "");
            String horaSortida = tvHoraSortida.getText().toString().replace("Hora de sortida: ", "");

            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
            Date dateInici = sdf.parse(horaInici);
            Date dateSortida = sdf.parse(horaSortida);

            // Obtenir les hores esperades des de tvHores
            String[] horesEsperadesArray = tvHores.getText().toString().split("\n");
            double horesTreballades = 0;

            for (String horesEsperades : horesEsperadesArray) {
                String[] hores = horesEsperades.split(" - ");
                Date horaIniciEsperada = sdf.parse(hores[0] + ":00");
                Date horaFiEsperada = sdf.parse(hores[1] + ":00");

                // Calcular les hores treballades dins de l'interval esperat
                if (dateInici.before(horaFiEsperada) && dateSortida.after(horaIniciEsperada)) {
                    Date horaIniciTreballada = dateInici.after(horaIniciEsperada) ? dateInici : horaIniciEsperada;
                    Date horaFiTreballada = dateSortida.before(horaFiEsperada) ? dateSortida : horaFiEsperada;
                    long diff = horaFiTreballada.getTime() - horaIniciTreballada.getTime();
                    horesTreballades += (double) diff / (1000 * 60 * 60);
                }
            }

            // Verificar si les hores treballades coincideixen amb les hores esperades
            if (horesTreballades > 0) {
                Toast.makeText(this, "Hores treballades correctes: " + horesTreballades, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Hores treballades insuficients", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Log.e("MainActivity", "Error en verificar les hores treballades: " + e.getMessage());
            Toast.makeText(this, "Error en verificar les hores treballades", Toast.LENGTH_SHORT).show();
        }
    }
        @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("isRunning", isRunning);
        outState.putString("horaInici", tvHoraInici.getText().toString());
    }
    
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            isRunning = savedInstanceState.getBoolean("isRunning");
            String horaInici = savedInstanceState.getString("horaInici");
            tvHoraInici.setText(horaInici);
    
            if (isRunning) {
                btnIniciar.setVisibility(View.GONE);
                btnParar.setVisibility(View.VISIBLE);
                btnParar.setEnabled(true);
                tvHoraInici.setVisibility(View.VISIBLE);
                tvHoraSortida.setVisibility(View.GONE);
            } else {
                btnIniciar.setVisibility(View.VISIBLE);
                btnParar.setVisibility(View.GONE);
                btnIniciar.setEnabled(true);
                tvHoraSortida.setVisibility(View.VISIBLE);
            }
        }
    }
}