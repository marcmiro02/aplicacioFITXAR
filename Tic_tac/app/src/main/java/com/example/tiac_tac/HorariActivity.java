package com.example.tiac_tac;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.view.Gravity;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.InputStream;

public class HorariActivity extends AppCompatActivity {

    private String userData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_horari);

        // Obtenir les dades de l'usuari de l'intent
        Intent intent = getIntent();
        userData = intent.getStringExtra("user_data");

        // Configura la barra de navegació inferior
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.nav_horari); // Selecciona l'element d'horari
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_cronometre) {
                // Redirigeix a l'activitat principal
                Intent intentMain = new Intent(HorariActivity.this, MainActivity.class);
                intentMain.putExtra("user_data", userData);
                startActivity(intentMain);
                return true;
            } else if (id == R.id.nav_profile) {
                // Redirigeix a l'activitat de perfil
                Intent intentProfile = new Intent(HorariActivity.this, Perfil.class);
                intentProfile.putExtra("user_data", userData);
                startActivity(intentProfile);
                return true;
            } else if (id == R.id.nav_horari) {
                // Manté l'activitat d'horari
                return true;
            } else if (id == R.id.nav_incidencia) {
                // Redirigeix a l'activitat d'incidència
                Intent intentIncidencia = new Intent(HorariActivity.this, Incidencia.class);
                intentIncidencia.putExtra("user_data", userData);
                startActivity(intentIncidencia);
                return true;
            } else {
                return false;
            }
        });

        // Llegir el fitxer Excel i mostrar el contingut
        TableLayout tlHorari = findViewById(R.id.tlHorari);
        try {
            InputStream inputStream = getAssets().open("horari.xlsx");
            Workbook workbook = new XSSFWorkbook(inputStream);
            Sheet sheet = workbook.getSheetAt(0);

            // Afegir la fila dels dies de la setmana
            Row headerRow = sheet.getRow(2);
            TableRow headerTableRow = new TableRow(this);
            headerTableRow.setPadding(4, 4, 4, 4);

            for (int j = 1; j < headerRow.getLastCellNum(); j++) { // Començar des de la columna B
                Cell cell = headerRow.getCell(j);
                TextView textView = new TextView(this);
                textView.setPadding(4, 4, 4, 4);
                textView.setTextSize(12); // Ajustar la mida del text per a dispositius mòbils
                textView.setGravity(Gravity.CENTER); // Centrar el text

                if (cell != null) {
                    textView.setText(cell.getStringCellValue());
                } else {
                    textView.setText("");
                }

                textView.setBackgroundColor(Color.parseColor("#FFBB86FC")); // Color per als dies
                headerTableRow.addView(textView);
            }

            tlHorari.addView(headerTableRow);

            for (int i = 3; i < sheet.getPhysicalNumberOfRows(); i++) { // Començar des de la fila 3
                Row row = sheet.getRow(i);
                TableRow tableRow = new TableRow(this);
                tableRow.setPadding(4, 4, 4, 4);

                for (int j = 1; j < row.getLastCellNum(); j++) { // Començar des de la columna B
                    Cell cell = row.getCell(j);
                    TextView textView = new TextView(this);
                    textView.setPadding(4, 4, 4, 4);
                    textView.setTextSize(12); // Ajustar la mida del text per a dispositius mòbils
                    textView.setGravity(Gravity.CENTER); // Centrar el text

                    if (cell != null) {
                        switch (cell.getCellType()) {
                            case STRING:
                                textView.setText(cell.getStringCellValue());
                                break;
                            case NUMERIC:
                                textView.setText(String.valueOf(cell.getNumericCellValue()));
                                break;
                            default:
                                textView.setText("");
                                break;
                        }
                    } else {
                        textView.setText("");
                    }

                    // Aplicar colors de fons
                    if (j == 1) {
                        textView.setBackgroundColor(Color.parseColor("#D3D3D3")); // Color subtil de l'escala de grisos per a les hores
                    } else if (i == 7 || i == 11 || i == 15) {
                        textView.setBackgroundColor(Color.parseColor("#A9A9A9")); // Color gris més fosc per a les files 8, 12 i 16
                    } else {
                        textView.setBackgroundColor(Color.TRANSPARENT); // Fons transparent per a les altres cel·les
                    }

                    tableRow.addView(textView);
                }

                // Centrar el text per a les files 8, 12 i 16
                if (i == 7 || i == 11 || i == 15) {
                    TableRow.LayoutParams params = new TableRow.LayoutParams();
                    params.span = row.getLastCellNum() - 1; // Combinar cel·les
                    TextView textView = new TextView(this);
                    textView.setLayoutParams(params);
                    textView.setGravity(Gravity.CENTER);
                    textView.setText(row.getCell(1).getStringCellValue());
                    textView.setBackgroundColor(Color.parseColor("#A9A9A9")); // Color gris més fosc
                    tableRow.removeAllViews();
                    tableRow.addView(textView);
                }

                tlHorari.addView(tableRow);
            }

            workbook.close();
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}