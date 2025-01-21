package com.example.tiac_tac;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class ActualActivity extends AppCompatActivity {

    private EditText nom_rol, id_rol;
    private TextView resultText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actual);

        // Assignar les vistes
        nom_rol = findViewById(R.id.nom_rol);
        id_rol = findViewById(R.id.id_rol);
        Button buttonCreate = findViewById(R.id.buttonCreate);
        Button buttonSearch = findViewById(R.id.buttonSearch);
        Button buttonUpdate = findViewById(R.id.buttonUpdate);
        Button buttonDelete = findViewById(R.id.buttonDelete);
        resultText = findViewById(R.id.resultText);

        // Botó per crear
        buttonCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                executarServei("http://192.160.167.195/Tic_tac/rols_usuaris/insertar_rol.php", "POST");
            }
        });

        // Botó per cercar per id_rol
        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                executarServei("http://192.160.167.195/Tic_tac/rols_usuaris/buscar_rol.php", "GET");
            }
        });

        // Botó per actualitzar per id_rol
        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                executarServei("rhttp://192.160.167.195/Tic_tac/rols_usuaris/actualitzar_ol.php", "POST");
            }
        });

        // Botó per eliminar per id_rol
        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                executarServei("http://192.160.167.195/Tic_tac/rols_usuaris/eliminar_rol.php", "DELETE");
            }
        });
    }

    // Infla el menú a la part superior de la pantalla
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu); // Usa el fitxer res/menu/menu.xml
        return true;
    }
    // Gestiona les opcions del menú
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.inici) {
            // Navega a la IniciActivity
            startActivity(new Intent(this, IniciActivity.class));
            return true;
        } else if (item.getItemId() == R.id.actual) {
            // Navega a la ActualActivity
            startActivity(new Intent(this, ActualActivity.class));
            return true;
        } else if (item.getItemId() == R.id.login) {
            // Navega a la LoginActivity
            startActivity(new Intent(this, LoginActivity.class));
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }

    }
    private void executarServei(String URL, String method) {
        StringRequest stringRequest;

        if (method.equals("GET") || method.equals("DELETE")) {
            stringRequest = new StringRequest(Request.Method.GET, URL + "?id_rol=" + id_rol.getText().toString(),
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            resultText.setText(response); // Mostrar resposta del servidor
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
                        }
                    });
        } else if (method.equals("POST")) {
            stringRequest = new StringRequest(Request.Method.POST, URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            resultText.setText(response); // Mostrar resposta del servidor
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("nom_rol", nom_rol.getText().toString());
                    params.put("id_rol", id_rol.getText().toString());
                    return params;
                }
            };
        } else {
            return; // No fer res si no és GET, POST ni DELETE
        }

        // Afegir la petició a la cua de sol·licituds
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}
