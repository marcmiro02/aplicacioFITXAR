package com.example.tiac_tac;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.view.Gravity;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONObject;

public class HorariActivity extends AppCompatActivity {

    private String userData;
    private String horarisData;
    private TableLayout tlHorari;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_horari);

        // Obtenir les dades de l'usuari i els horaris de l'intent
        Intent intent = getIntent();
        userData = intent.getStringExtra("user_data");
        horarisData = intent.getStringExtra("horaris_data");

        Log.d("HorariActivity", "Dades de l'usuari: " + userData);
        Log.d("HorariActivity", "Dades dels horaris: " + horarisData);

        // Configura la barra de navegació inferior
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.nav_horari); // Selecciona l'element d'horari
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_inici) {
                // Redirigeix a l'activitat principal
                Intent intentMain = new Intent(HorariActivity.this, MainActivity.class);
                intentMain.putExtra("user_data", userData);
                intentMain.putExtra("horaris_data", horarisData);
                startActivity(intentMain);
                return true;
            } else if (id == R.id.nav_horari) {
                // Redirigeix a l'activitat d'horari
                Intent intentHorari = new Intent(HorariActivity.this, HorariActivity.class);
                intentHorari.putExtra("user_data", userData);
                intentHorari.putExtra("horaris_data", horarisData);
                startActivity(intentHorari);
                return true;
            } else if (id == R.id.nav_incidencia) {
                // Redirigeix a l'activitat d'incidència
                Intent intentIncidencia = new Intent(HorariActivity.this, Incidencia.class);
                intentIncidencia.putExtra("user_data", userData);
                intentIncidencia.putExtra("horaris_data", horarisData);
                startActivity(intentIncidencia);
                return true;
            } else if (id == R.id.nav_profile) {
                // Redirigeix a l'activitat de perfil
                Intent intentProfile = new Intent(HorariActivity.this, Perfil.class);
                intentProfile.putExtra("user_data", userData);
                intentProfile.putExtra("horaris_data", horarisData);
                startActivity(intentProfile);
                return true;
            } else {
                return false;
            }
        });

        // Assignar la vista de la taula
        tlHorari = findViewById(R.id.tlHorari);

        // Mostrar les dades de l'horari
        mostrarHorari();
    }

    private void mostrarHorari() {
        try {
            JSONObject userJson = new JSONObject(userData);
            JSONArray horarisArray = userJson.getJSONArray("horaris");

            if (horarisArray.length() == 0) {
                mostrarMissatge("No es pot mostrar l'horari");
                return;
            }

            // Iterar sobre les files de la taula
            for (int i = 1; i < tlHorari.getChildCount(); i++) {
                TableRow row = (TableRow) tlHorari.getChildAt(i);
                TextView timeTextView = (TextView) row.getChildAt(0);
                String time = timeTextView.getText().toString();

                // Iterar sobre les dades de l'horari
                for (int j = 0; j < horarisArray.length(); j++) {
                    JSONObject horari = horarisArray.getJSONObject(j);
                    String dia = horari.getString("dia");
                    String horaInicio = horari.getString("hora_inicio").substring(0, 5); // Format HH:MM

                    // Comparar el dia i l'hora d'inici
                    if (time.equals(horaInicio)) {
                        int columnIndex = getColumnIndexForDay(dia);
                        if (columnIndex != -1) {
                            TextView cell = (TextView) row.getChildAt(columnIndex);
                            cell.setText("X");
                        }
                    }
                }
            }
        } catch (Exception e) {
            Log.e("HorariActivity", "Error en el format de les dades de l'horari: " + e.getMessage());
            mostrarMissatge("Error en el format de les dades de l'horari");
        }
    }

    private int getColumnIndexForDay(String dia) {
        switch (dia) {
            case "DILLUNS":
                return 1;
            case "DIMARTS":
                return 2;
            case "DIMECRES":
                return 3;
            case "DIJOUS":
                return 4;
            case "DIVENDRES":
                return 5;
            default:
                return -1;
        }
    }

    private void mostrarMissatge(String missatge) {
        Toast.makeText(this, missatge, Toast.LENGTH_SHORT).show();
    }
}