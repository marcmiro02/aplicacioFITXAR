package com.example.projecte_fitxar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {
    private Context context;
    private ArrayList<User> userList;
    private OnItemClickListener onItemClickListener;
    private FirebaseDatabase database;
    private User user;
    private OnItemClickListener listener;

    public UserAdapter(Context context, ArrayList<User> userList, FirebaseDatabase database, OnItemClickListener listener) {
        this.context = context;
        this.userList = userList;
        this.database = database;
        this.onItemClickListener = listener;
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.user_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User user = userList.get(position);
        holder.name.setText(user.getName());
        holder.surname.setText(user.getSurname());
        holder.email.setText(user.getEmail());

        // Configurar el listener pel clic
        holder.itemView.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(user);
            }
        });
    }


    @Override
    public int getItemCount() {
        return userList != null ? userList.size() : 0; // Return 0 if list is null
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, surname, email;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.list_item_name);
            surname = itemView.findViewById(R.id.list_item_surname);
            email = itemView.findViewById(R.id.list_item_email);
        }
    }

    // Set the item click listener
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    // Interface for handling item clicks
    public interface OnItemClickListener {
        void onItemClick(User user); // Passa l'objecte seleccionat
    }


}
