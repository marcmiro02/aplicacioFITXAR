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
}
