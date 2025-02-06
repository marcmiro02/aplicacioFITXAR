package com.example.tiac_tac;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Incidencia extends AppCompatActivity {

    private String userData;
    private String horarisData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incidencia);

        // Obtenir les dades de l'usuari de l'intent
        Intent intent = getIntent();
        userData = intent.getStringExtra("user_data");
        horarisData = intent.getStringExtra("horaris_data");

        // Configura la barra de navegació inferior
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.nav_incidencia); // Selecciona l'element d'incidència
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_inici) {
                // Redirigeix a l'activitat principal
                Intent intentMain = new Intent(Incidencia.this, MainActivity.class);
                intentMain.putExtra("user_data", userData);
                intentMain.putExtra("horaris_data", horarisData);
                startActivity(intentMain);
                return true;
            } else if (id == R.id.nav_horari) {
                // Redirigeix a l'activitat d'horari
                Intent intentHorari = new Intent(Incidencia.this, HorariActivity.class);
                intentHorari.putExtra("user_data", userData);
                intentHorari.putExtra("horaris_data", horarisData);
                startActivity(intentHorari);
                return true;
            } else if (id == R.id.nav_profile) {
                // Redirigeix a l'activitat de perfil
                Intent intentProfile = new Intent(Incidencia.this, Perfil.class);
                intentProfile.putExtra("user_data", userData);
                intentProfile.putExtra("horaris_data", horarisData);
                startActivity(intentProfile);
                return true;
            } else if (id == R.id.nav_incidencia) {
                // Redirigeix a l'activitat d'incidència
                Intent intentIncidencia = new Intent(Incidencia.this, Incidencia.class);
                intentIncidencia.putExtra("user_data", userData);
                intentIncidencia.putExtra("horaris_data", horarisData);
                startActivity(intentIncidencia);
                return true;
            } else {
                return false;
            }
        });

        // Configura el botó per afegir una nova incidència
        Button btnAfegirIncidencia = findViewById(R.id.btnAfegirIncidencia);
        btnAfegirIncidencia.setOnClickListener(v -> {
            // Implementa la lògica per afegir una nova incidència
        });

        // Configura l'historial d'incidències (exemple)
        ListView lvIncidencies = findViewById(R.id.lvIncidencies);
        // Implementa l'adaptador per mostrar les incidències
    }
}