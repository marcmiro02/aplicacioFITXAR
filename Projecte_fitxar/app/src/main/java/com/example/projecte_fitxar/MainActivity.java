package com.example.projecte_fitxar;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseApp.initializeApp(MainActivity.this);
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        FloatingActionButton add = findViewById(R.id.addNote);
        add.setOnClickListener(view -> showUserDialog(database));

        TextView empty = findViewById(R.id.empty);
        RecyclerView recyclerView = findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));

        // Obtenció de dades de Firebase
        database.getReference().child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<User> arrayList = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    User user = dataSnapshot.getValue(User.class);
                    if (user != null) {
                        user.setKey(dataSnapshot.getKey());
                        arrayList.add(user);
                    }
                }

                if (arrayList.isEmpty()) {
                    empty.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                } else {
                    empty.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                }

                UserAdapter adapter = new UserAdapter(MainActivity.this, arrayList, database, user -> {
                    showEditUserDialog(database, user); // Obre el diàleg d'edició
                });
                recyclerView.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, "Failed to load users.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showUserDialog(FirebaseDatabase database) {
        View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.add_user_dialog, null);
        TextInputLayout nameLayout = view.findViewById(R.id.nameLayout);
        TextInputLayout surnameLayout = view.findViewById(R.id.surnameLayout);
        TextInputLayout emailLayout = view.findViewById(R.id.emailLayout);
        TextInputLayout passwordLayout = view.findViewById(R.id.passwordLayout);

        TextInputEditText nameET = view.findViewById(R.id.nameET);
        TextInputEditText surnameET = view.findViewById(R.id.surnameET);
        TextInputEditText emailET = view.findViewById(R.id.emailET);
        TextInputEditText passwordET = view.findViewById(R.id.passwordET);

        AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this)
                .setTitle("Add User")
                .setView(view)
                .setPositiveButton("Save", (dialogInterface, i) -> {
                    String name = Objects.requireNonNull(nameET.getText()).toString().trim();
                    String surname = Objects.requireNonNull(surnameET.getText()).toString().trim();
                    String email = Objects.requireNonNull(emailET.getText()).toString().trim();
                    String password = Objects.requireNonNull(passwordET.getText()).toString().trim();

                    // Verificació de la validació
                    boolean isValid = true;
                    if (name.isEmpty()) {
                        nameLayout.setError("Name is required!");
                        isValid = false;
                    } else {
                        nameLayout.setError(null);
                    }

                    if (surname.isEmpty()) {
                        surnameLayout.setError("Surname is required!");
                        isValid = false;
                    } else {
                        surnameLayout.setError(null);
                    }

                    if (email.isEmpty()) {
                        emailLayout.setError("Email is required!");
                        isValid = false;
                    } else if (!email.contains("@") || !email.contains(".")) {
                        emailLayout.setError("Invalid email format!");
                        isValid = false;
                    } else {
                        emailLayout.setError(null);
                    }

                    if (password.isEmpty()) {
                        passwordLayout.setError("Password is required!");
                        isValid = false;
                    } else {
                        passwordLayout.setError(null);
                    }

                    if (isValid) {
                        // Mostrar la barra de progrés mentre es desa l'usuari
                        AlertDialog progressDialog = showProgressBar();
                        User newUser = new User(null, name, surname, email, password);

                        // Afegir l'usuari a Firebase
                        database.getReference().child("users").push().setValue(newUser)
                                .addOnSuccessListener(unused -> {
                                    progressDialog.dismiss();
                                    Toast.makeText(MainActivity.this, "User Saved Successfully!", Toast.LENGTH_SHORT).show();
                                })
                                .addOnFailureListener(e -> {
                                    progressDialog.dismiss();
                                    Toast.makeText(MainActivity.this, "Error saving user: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                });
                    } else {
                        // Mostrar un Toast d'error si no és vàlid
                        Toast.makeText(MainActivity.this, "Please correct the errors", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", (dialogInterface, i) -> dialogInterface.dismiss())
                .create();
        alertDialog.show();
    }

    void showEditUserDialog(FirebaseDatabase database, User user) {
        View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.add_user_dialog, null);
        TextInputLayout nameLayout = view.findViewById(R.id.nameLayout);
        TextInputLayout surnameLayout = view.findViewById(R.id.surnameLayout);
        TextInputLayout emailLayout = view.findViewById(R.id.emailLayout);
        TextInputLayout passwordLayout = view.findViewById(R.id.passwordLayout);

        TextInputEditText nameET = view.findViewById(R.id.nameET);
        TextInputEditText surnameET = view.findViewById(R.id.surnameET);
        TextInputEditText emailET = view.findViewById(R.id.emailET);
        TextInputEditText passwordET = view.findViewById(R.id.passwordET);

        // Omplir els camps amb les dades existents
        nameET.setText(user.getName());
        surnameET.setText(user.getSurname());
        emailET.setText(user.getEmail());
        passwordET.setText(user.getPassword());

        AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this)
                .setTitle("Edit User")
                .setView(view)
                .setPositiveButton("Save", (dialogInterface, i) -> {
                    String name = Objects.requireNonNull(nameET.getText()).toString().trim();
                    String surname = Objects.requireNonNull(surnameET.getText()).toString().trim();
                    String email = Objects.requireNonNull(emailET.getText()).toString().trim();
                    String password = Objects.requireNonNull(passwordET.getText()).toString().trim();

                    boolean isValid = true;
                    if (name.isEmpty()) {
                        nameLayout.setError("Name is required!");
                        isValid = false;
                    } else {
                        nameLayout.setError(null);
                    }

                    if (surname.isEmpty()) {
                        surnameLayout.setError("Surname is required!");
                        isValid = false;
                    } else {
                        surnameLayout.setError(null);
                    }

                    if (email.isEmpty()) {
                        emailLayout.setError("Email is required!");
                        isValid = false;
                    } else {
                        emailLayout.setError(null);
                    }

                    if (password.isEmpty()) {
                        passwordLayout.setError("Password is required!");
                        isValid = false;
                    } else {
                        passwordLayout.setError(null);
                    }

                    if (isValid) {
                        AlertDialog progressDialog = showProgressBar();
                        user.setName(name);
                        user.setSurname(surname);
                        user.setEmail(email);
                        user.setPassword(password);

                        // Actualitzar l'usuari a Firebase
                        database.getReference().child("users").child(user.getKey()).setValue(user)
                                .addOnSuccessListener(unused -> {
                                    progressDialog.dismiss();
                                    Toast.makeText(MainActivity.this, "User Updated Successfully!", Toast.LENGTH_SHORT).show();
                                })
                                .addOnFailureListener(e -> {
                                    progressDialog.dismiss();
                                    Toast.makeText(MainActivity.this, "Error updating user: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                });
                    }
                })
                .setNegativeButton("Cancel", (dialogInterface, i) -> dialogInterface.dismiss())
                // Afegir un botó "Eliminar" per eliminar l'usuari
                .setNeutralButton("Delete", (dialogInterface, i) -> {
                    // Confirmació abans d'eliminar l'usuari
                    new AlertDialog.Builder(MainActivity.this)
                            .setTitle("Confirm Deletion")
                            .setMessage("Are you sure you want to delete this user?")
                            .setPositiveButton("Yes", (dialog, which) -> {
                                // Eliminar l'usuari de Firebase
                                database.getReference().child("users").child(user.getKey()).removeValue()
                                        .addOnSuccessListener(unused -> {
                                            Toast.makeText(MainActivity.this, "User Deleted Successfully!", Toast.LENGTH_SHORT).show();
                                        })
                                        .addOnFailureListener(e -> {
                                            Toast.makeText(MainActivity.this, "Error deleting user: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                        });
                            })
                            .setNegativeButton("No", null)
                            .show();
                })
                .create();
        alertDialog.show();
    }

    private AlertDialog showProgressBar() {
        View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.progress_dialog, null);
        AlertDialog progressDialog = new AlertDialog.Builder(MainActivity.this)
                .setView(view)
                .setCancelable(false)
                .create();
        progressDialog.show();
        return progressDialog;
    }
}
