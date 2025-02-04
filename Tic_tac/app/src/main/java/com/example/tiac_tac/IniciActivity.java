package com.example.tiac_tac;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;

public class IniciActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inici);
    }


    // Infla el menú a la part superior de la pantalla
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.bottom_navigation_menu, menu); // Usa el fitxer res/menu/menu.xml
        return true;
    }

    // Gestiona les opcions del menú
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = null;
        if (item.getItemId() == R.id.nav_cronometre) {
            intent = new Intent(this, IniciActivity.class);
        } else if (item.getItemId() == R.id.nav_settings) {
            intent = new Intent(this, ActualActivity.class);
        } else if (item.getItemId() == R.id.nav_profile) {
            intent = new Intent(this, LoginActivity.class);
        }

        if (intent != null) {
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
