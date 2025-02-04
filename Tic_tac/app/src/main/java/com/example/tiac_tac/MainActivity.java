package com.example.tiac_tac;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.TextView;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONObject;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private TextView tvNomCognoms, tvCronometre;
    private View cronometreCircle;
    private Button btnIniciar, btnParar, btnReiniciar;

    private Handler handler = new Handler();
    private long startTime = 0L;
    private boolean isRunning = false;
    private String userData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Enllaça les vistes
        tvNomCognoms = findViewById(R.id.tvNomCognoms);
        tvCronometre = findViewById(R.id.tvCronometre);
        cronometreCircle = findViewById(R.id.cronometreCircle);
        btnIniciar = findViewById(R.id.btnIniciar);
        btnParar = findViewById(R.id.btnParar);
        btnReiniciar = findViewById(R.id.btnReiniciar);

        // Obtenir les dades de l'usuari de l'intent
        Intent intent = getIntent();
        userData = intent.getStringExtra("user_data");

        // Mostrar les dades de l'usuari (exemple)
        try {
            JSONObject jsonResponse = new JSONObject(userData);
            String nom = jsonResponse.getString("nom");
            String cognoms = jsonResponse.getString("cognoms");
            tvNomCognoms.setText(nom + " " + cognoms);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Configura la barra de navegació inferior
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_cronometre) {
                // Manté l'activitat principal
                return true;
            } else if (id == R.id.nav_profile) {
                // Redirigeix a l'activitat de perfil
                Intent intentProfile = new Intent(MainActivity.this, Perfil.class);
                intentProfile.putExtra("user_data", userData);
                startActivity(intentProfile);
                return true;
            } else if (id == R.id.nav_horari) {
                // Redirigeix a l'activitat d'horari
                Intent intentHorari = new Intent(MainActivity.this, HorariActivity.class);
                intentHorari.putExtra("user_data", userData);
                startActivity(intentHorari);
                return true;
            } else if (id == R.id.nav_incidencia) {
                // Redirigeix a l'activitat d'incidència
                Intent intentIncidencia = new Intent(MainActivity.this, Incidencia.class);
                intentIncidencia.putExtra("user_data", userData);
                startActivity(intentIncidencia);
                return true;
            } else {
                return false;
            }
        });

        // Configura els botons del cronòmetre
        btnIniciar.setOnClickListener(v -> iniciarCronometre());
        btnParar.setOnClickListener(v -> pararCronometre());
        btnReiniciar.setOnClickListener(v -> reiniciarCronometre());
    }

    private void iniciarCronometre() {
        if (!isRunning) {
            startTime = System.currentTimeMillis();
            handler.postDelayed(updateTimerThread, 0);
            isRunning = true;
        }
    }

    private void pararCronometre() {
        if (isRunning) {
            handler.removeCallbacks(updateTimerThread);
            isRunning = false;
        }
    }

    private void reiniciarCronometre() {
        pararCronometre();
        tvCronometre.setText("00:00:00");
    }

    private Runnable updateTimerThread = new Runnable() {
        public void run() {
            long timeInMillis = System.currentTimeMillis() - startTime;
            int secs = (int) (timeInMillis / 1000);
            int mins = secs / 60;
            int hrs = mins / 60;
            secs = secs % 60;
            mins = mins % 60;
            tvCronometre.setText(String.format("%02d:%02d:%02d", hrs, mins, secs));
            handler.postDelayed(this, 1000);
        }
    };

    private void showSettings() {
        // Implementa accions
    }
}