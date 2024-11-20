package com.example.tareahenrik;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    private TextInputEditText emailField, passwordField, confirmPasswordField;
    private MaterialButton saveButton;
    private FirebaseAuth auth;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrer);  // Asegúrate de que el XML se llame así

        // Inicializar Firebase Auth y Realtime Database
        auth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("usuarios");

        // Inicializar vistas
        emailField = findViewById(R.id.emailInput);
        passwordField = findViewById(R.id.passwordInput);
        confirmPasswordField = findViewById(R.id.confirmPasswordInput);
        saveButton = findViewById(R.id.saveButton);

        // Configurar el botón de registro
        saveButton.setOnClickListener(v -> registerUser());
    }

    private void registerUser() {
        String email = emailField.getText().toString().trim();
        String password = passwordField.getText().toString().trim();
        String confirmPassword = confirmPasswordField.getText().toString().trim();

        // Validar que los campos no estén vacíos
        if (TextUtils.isEmpty(email)) {
            emailField.setError("Ingresa tu correo");
            return;
        }
        if (TextUtils.isEmpty(password)) {
            passwordField.setError("Ingresa tu clave");
            return;
        }
        if (TextUtils.isEmpty(confirmPassword)) {
            confirmPasswordField.setError("Confirma tu clave");
            return;
        }

        // Validar que las contraseñas coincidan
        if (!password.equals(confirmPassword)) {
            confirmPasswordField.setError("Las contraseñas no coinciden");
            return;
        }

        // Crear un nuevo usuario con Firebase Auth
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Registro exitoso
                        FirebaseUser user = auth.getCurrentUser();
                        Toast.makeText(RegisterActivity.this, "Registro exitoso", Toast.LENGTH_SHORT).show();

                        // Crear objeto de datos para la tabla de "usuarios"
                        String userId = user.getUid();  // Obtener el ID único del usuario

                        // Crear objeto de datos para la base de datos
                        User newUser = new User(email, password);  // Guardar correo y clave

                        // Guardar los datos en la base de datos
                        databaseReference.child(userId).setValue(newUser)
                                .addOnCompleteListener(task1 -> {
                                    if (task1.isSuccessful()) {
                                        // Redirigir a la actividad de inicio de sesión
                                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        // Error al guardar datos en la base de datos
                                        Toast.makeText(RegisterActivity.this, "Error al guardar los datos: " + task1.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });

                    } else {
                        // Error en el registro
                        Toast.makeText(RegisterActivity.this, "Error en el registro: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // Clase para representar un usuario
    public static class User {
        public String email;
        public String password;

        // Constructor para guardar correo y clave
        public User(String email, String password) {
            this.email = email;
            this.password = password;
        }
    }
}









