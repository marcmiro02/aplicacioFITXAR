package com.example.tiac_tac;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.TextView;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tiac_tac.models.Usuari;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private TextView tvNomCognoms, tvCronometre;
    private View cronometreCircle;
    private Button btnIniciar, btnParar, btnReiniciar;

    private Handler handler = new Handler();
    private long startTime = 0L;
    private boolean isRunning = false;

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

        // Recupera l'objecte Usuari passat des de LoginActivity
        Usuari usuari = (Usuari) getIntent().getSerializableExtra("usuari");

        // Mostra el nom i cognoms si l'usuari no és nul
        if (usuari != null) {
            String nomCognoms = usuari.getNom() + " " + usuari.getCognoms();
            tvNomCognoms.setText(nomCognoms);
        }

        // Configura els botons del cronòmetre
        btnIniciar.setOnClickListener(view -> iniciarCronometre());
        btnParar.setOnClickListener(view -> pausarCronometre());
        btnReiniciar.setOnClickListener(view -> reiniciarCronometre());

        // Configura la barra de navegació inferior
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_cronometre) {
                // Manté el cronòmetre
                return true;
            } else if (id == R.id.nav_settings) {
                // Mostra configuració
                showSettings();
                return true;
            } else if (id == R.id.nav_profile) {
                // Mostra el perfil
                showProfile();
                return true;
            } else {
                return false;
            }
        });
    }

    // Mètodes per controlar el cronòmetre
    private void iniciarCronometre() {
        if (!isRunning) {
            startTime = System.currentTimeMillis();
            handler.postDelayed(updateCronometre, 0);
            isRunning = true;
            cronometreCircle.setBackgroundResource(R.drawable.circle_running); // Canvia a verd
        }
    }

    private void pausarCronometre() {
        if (isRunning) {
            handler.removeCallbacks(updateCronometre);
            isRunning = false;
            cronometreCircle.setBackgroundResource(R.drawable.circle_paused); // Canvia a taronja
        }
    }

    private void reiniciarCronometre() {
        handler.removeCallbacks(updateCronometre);
        tvCronometre.setText("00:00:00");
        isRunning = false;
        cronometreCircle.setBackgroundResource(R.drawable.circle_stopped); // Canvia a vermell
    }

    // Runnable per actualitzar el cronòmetre
    private Runnable updateCronometre = new Runnable() {
        @Override
        public void run() {
            long elapsedTime = System.currentTimeMillis() - startTime;
            int seconds = (int) (elapsedTime / 1000) % 60;
            int minutes = (int) (elapsedTime / (1000 * 60)) % 60;
            int hours = (int) (elapsedTime / (1000 * 60 * 60));

            tvCronometre.setText(String.format("%02d:%02d:%02d", hours, minutes, seconds));
            handler.postDelayed(this, 1000);
        }
    };

    // Accions de la barra de navegació inferior
    private void showSettings() {
        // Implementa accions
    }

    private void showProfile() {
        // Implementa accions
    }
}
