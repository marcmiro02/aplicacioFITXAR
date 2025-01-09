package com.example.projecte_fitxar;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.DialogFragment;

import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

public class AddUserDialogFragment extends DialogFragment {

    private TextInputEditText nameET;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflem el disseny XML per al diàleg
        View rootView = inflater.inflate(R.layout.add_user_dialog, container, false);

        // Inicialitzar els camps del formulari
        nameET = rootView.findViewById(R.id.nameET);
        TextInputEditText surnameET = rootView.findViewById(R.id.surnameET);
        TextInputEditText emailET = rootView.findViewById(R.id.emailET);

        // Comprovar si es passa algun valor per actualitzar els camps (per editar)
        if (getArguments() != null) {
            nameET.setText(getArguments().getString("userName"));
            surnameET.setText(getArguments().getString("userSurname"));
            emailET.setText(getArguments().getString("userEmail"));
        }

        return rootView;
    }

    // Perquè l'activitat que crida pugui obtenir les dades modificades
    public String getName() {
        return Objects.requireNonNull(nameET.getText()).toString();
    }
}
