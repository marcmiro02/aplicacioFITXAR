package com.example.tiac_tac;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import com.example.tiac_tac.models.Usuari; // Importa la classe Usuari

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private EditText loginNom, loginPassword;
    private Button buttonLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Assignar les vistes
        loginNom = findViewById(R.id.login_nom);
        loginPassword = findViewById(R.id.login_password);
        buttonLogin = findViewById(R.id.buttonLogin);

        // Lògica de login
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nom = loginNom.getText().toString();
                String password = loginPassword.getText().toString();

                if (nom.isEmpty() || password.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Nom d'usuari i contrasenya són necessaris", Toast.LENGTH_SHORT).show();
                } else {
                    executarServei("http://192.160.160.22:8080/Tic_tac/usuaris/login.php", nom, password);
                }
            }
        });
    }

    // Funció per gestionar el servei de login
    private void executarServei(String URL, final String nom, final String password) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Si la resposta del servidor és positiva
                        if (response.isEmpty()) {
                            Toast.makeText(LoginActivity.this, "Nom d'usuari o contrasenya incorrectes", Toast.LENGTH_SHORT).show();
                        } else {
                            // Exemple de resposta: "Nom Cognoms"
                            String[] dades = response.trim().split(" "); // Ajusta segons el format de resposta
                            String nomUsuari = dades[0]; // Nom
                            String cognoms = dades[1]; // Cognoms
                            String email = dades[2]; // Email (o el que sigui, segons la resposta)
                            String nomUsuariDB = dades[3]; // Nom d'usuari

                            // Crear l'objecte Usuari
                            Usuari usuari = new Usuari(nomUsuari, cognoms, email, nomUsuariDB);

                            // Crear l'Intent per a MainActivity
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            intent.putExtra("usuari", usuari); // Passa l'objecte Usuari
                            startActivity(intent);
                            finish(); // Tanca LoginActivity
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(LoginActivity.this, "Error en la connexió", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("nom", nom);
                params.put("password", password);
                return params;
            }
        };

        // Afegir la petició a la cua de sol·licituds
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}
