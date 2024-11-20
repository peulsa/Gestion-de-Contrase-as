package com.example.tareahenrik;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CreatePasswordActivity extends AppCompatActivity {

    // Declarar las vistas
    private TextInputEditText originInput, keyInput, userNameInput; // Añadimos el campo userName
    private MaterialButton saveButton;
    private FirebaseAuth auth;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_password);

        // Inicializar las vistas usando findViewById
        originInput = findViewById(R.id.originInput);
        keyInput = findViewById(R.id.keyInput);
        userNameInput = findViewById(R.id.userNameInput); // Inicializamos el nuevo campo
        saveButton = findViewById(R.id.saveButton);

        // Inicializar Firebase Auth y Realtime Database
        auth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("usuarios");

        // Configurar el listener del botón de guardar
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData();
            }
        });
    }

    private void saveData() {
        // Obtener los valores de los campos de entrada
        String origin = originInput.getText().toString().trim();
        String key = keyInput.getText().toString().trim();
        String userName = userNameInput.getText().toString().trim(); // Obtener el valor de userName

        // Validar que los campos no estén vacíos
        if (origin.isEmpty() || key.isEmpty() || userName.isEmpty()) {
            Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        // Obtener el UID del usuario autenticado
        String userId = auth.getCurrentUser().getUid();

        // Crear un mapa para los datos
        Map<String, String> data = new HashMap<>();
        data.put("origen", origin);
        data.put("clave", key);
        data.put("userName", userName); // Añadir el userName a los datos

        // Obtener la referencia de usuario y claves
        DatabaseReference userClavesRef = databaseReference.child(userId).child("claves");

        // Obtener las claves actuales
        userClavesRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // Si ya existen claves, agregar a la lista
                List<Map<String, String>> clavesList = new ArrayList<>();
                if (task.getResult().exists()) {
                    // Si ya existen claves, obtenemos la lista actual
                    for (DataSnapshot snapshot : task.getResult().getChildren()) {
                        clavesList.add((Map<String, String>) snapshot.getValue());
                    }
                }

                // Agregar la nueva clave a la lista
                clavesList.add(data);

                // Guardar todas las claves (nueva y anteriores)
                userClavesRef.setValue(clavesList).addOnCompleteListener(saveTask -> {
                    if (saveTask.isSuccessful()) {
                        Toast.makeText(CreatePasswordActivity.this, "Datos guardados correctamente", Toast.LENGTH_SHORT).show();

                        // Redirigir a la actividad LobbyActivity
                        Intent intent = new Intent(CreatePasswordActivity.this, LobbyActivity.class);
                        startActivity(intent);
                        finish(); // Finalizar la actividad actual para que no quede en el back stack
                    } else {
                        Toast.makeText(CreatePasswordActivity.this, "Error al guardar los datos", Toast.LENGTH_SHORT).show();
                    }
                });

            } else {
                Toast.makeText(CreatePasswordActivity.this, "Error al obtener las claves", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
