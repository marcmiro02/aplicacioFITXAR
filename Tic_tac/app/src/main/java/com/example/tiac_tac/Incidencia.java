package com.example.tiac_tac;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Incidencia extends AppCompatActivity {

    private String userData;
    private String horarisData;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> incidenciesList;

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

        // Configura l'historial d'incidències
        ListView lvIncidencies = findViewById(R.id.lvIncidencies);
        incidenciesList = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, incidenciesList);
        lvIncidencies.setAdapter(adapter);

        // Obtenir les incidències del servidor
        obtenirIncidencies();
    }

    private void obtenirIncidencies() {
        try {
            JSONObject userJson = new JSONObject(userData);
            int usuariId = userJson.getInt("id_usuari");

            StringRequest stringRequest = new StringRequest(Request.Method.POST, Ip.INCIDENCIA_MOSTRAR_URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("Incidencia", "Resposta del servidor: " + response);
                            try {
                                // Eliminar el text addicional abans del JSON
                                String jsonResponseString = response.substring(response.indexOf("["));

                                JSONArray jsonArray = new JSONArray(jsonResponseString);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    String data = jsonObject.getString("data_incidencia");
                                    String descripcio = jsonObject.getString("descripcio");
                                    incidenciesList.add(data + ": " + descripcio);
                                }
                                adapter.notifyDataSetChanged();
                            } catch (Exception e) {
                                Log.e("Incidencia", "Error en el format de les dades de les incidències: " + e.getMessage());
                                Toast.makeText(Incidencia.this, "Error en el format de les dades de les incidències", Toast.LENGTH_SHORT).show();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(Incidencia.this, "Error en la connexió", Toast.LENGTH_SHORT).show();
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("usuari_id", String.valueOf(usuariId));
                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);
        } catch (Exception e) {
            Log.e("Incidencia", "Error en obtenir les dades de l'usuari: " + e.getMessage());
            Toast.makeText(this, "Error en obtenir les dades de l'usuari", Toast.LENGTH_SHORT).show();
        }
    }
}