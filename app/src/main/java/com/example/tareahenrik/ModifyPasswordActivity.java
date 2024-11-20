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

public class ModifyPasswordActivity extends AppCompatActivity {

    // Declarar las vistas
    private TextInputEditText originModifyInput, oldKeyInput, newKeyInput; // Solo 3 campos
    private MaterialButton saveButton;
    private FirebaseAuth auth;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_password);

        // Inicializar las vistas usando findViewById
        originModifyInput = findViewById(R.id.originModifyInput);
        oldKeyInput = findViewById(R.id.oldKeyInput);
        newKeyInput = findViewById(R.id.newKeyInput);
        saveButton = findViewById(R.id.saveButton);

        // Inicializar Firebase Auth y Realtime Database
        auth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("usuarios");

        // Configurar el listener del botón de guardar
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modifyPassword();
            }
        });
    }

    private void modifyPassword() {
        // Obtener los valores de los campos de entrada
        String origin = originModifyInput.getText().toString().trim();
        String oldKey = oldKeyInput.getText().toString().trim();
        String newKey = newKeyInput.getText().toString().trim();

        // Validar que los campos no estén vacíos
        if (origin.isEmpty() || oldKey.isEmpty() || newKey.isEmpty()) {
            Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        // Obtener el UID del usuario autenticado
        String userId = auth.getCurrentUser().getUid();

        // Crear un mapa para los datos de la nueva clave
        Map<String, String> newPasswordData = new HashMap<>();
        newPasswordData.put("origen", origin);
        newPasswordData.put("clave", newKey);

        // Obtener la referencia de usuario y claves
        DatabaseReference userClavesRef = databaseReference.child(userId).child("claves");

        // Obtener las claves actuales
        userClavesRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // Si ya existen claves, agregar a la lista
                List<Map<String, String>> clavesList = new ArrayList<>();
                boolean found = false; // Bandera para verificar si encontramos la clave vieja
                if (task.getResult().exists()) {
                    // Si ya existen claves, obtenemos la lista actual
                    for (DataSnapshot snapshot : task.getResult().getChildren()) {
                        Map<String, String> currentData = (Map<String, String>) snapshot.getValue();
                        // Verificamos si la clave vieja coincide
                        if (currentData != null && currentData.get("clave").equals(oldKey)) {
                            // Si encontramos la clave vieja, la reemplazamos por la nueva
                            currentData.put("clave", newKey);
                            clavesList.add(currentData);
                            found = true; // Hemos encontrado la clave vieja
                        } else {
                            clavesList.add(currentData); // Añadimos las claves existentes sin cambios
                        }
                    }
                }

                if (found) {
                    // Si encontramos la clave vieja y la actualizamos
                    userClavesRef.setValue(clavesList).addOnCompleteListener(saveTask -> {
                        if (saveTask.isSuccessful()) {
                            Toast.makeText(ModifyPasswordActivity.this, "Contraseña modificada correctamente", Toast.LENGTH_SHORT).show();

                            // Redirigir a la actividad LobbyActivity (o la que corresponda)
                            Intent intent = new Intent(ModifyPasswordActivity.this, LobbyActivity.class);
                            startActivity(intent);
                            finish(); // Finalizar la actividad actual para que no quede en el back stack
                        } else {
                            Toast.makeText(ModifyPasswordActivity.this, "Error al guardar los datos", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(ModifyPasswordActivity.this, "La clave antigua no coincide con la registrada", Toast.LENGTH_SHORT).show();
                }

            } else {
                Toast.makeText(ModifyPasswordActivity.this, "Error al obtener las claves", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
