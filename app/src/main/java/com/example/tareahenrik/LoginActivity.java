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

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText emailField, passwordField;
    private MaterialButton loginButton;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);  // Asegúrate de que el XML correcto se llame así

        // Inicializar Firebase Auth
        auth = FirebaseAuth.getInstance();

        // Inicializar vistas
        emailField = findViewById(R.id.emailInput);  // Cambia a TextInputEditText
        passwordField = findViewById(R.id.passwordInput);  // Cambia a TextInputEditText
        loginButton = findViewById(R.id.loginButton);

        // Configurar el botón de inicio de sesión
        loginButton.setOnClickListener(v -> loginUser());
    }

    private void loginUser() {
        String email = emailField.getText().toString().trim();
        String password = passwordField.getText().toString().trim();

        // Validar que los campos no estén vacíos
        if (TextUtils.isEmpty(email)) {
            emailField.setError("Ingresa tu correo");
            return;
        }
        if (TextUtils.isEmpty(password)) {
            passwordField.setError("Ingresa tu clave");
            return;
        }

        // Autenticar con Firebase
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Inicio de sesión exitoso, redirigir a la actividad lobby
                        FirebaseUser user = auth.getCurrentUser();
                        Intent intent = new Intent(LoginActivity.this, LobbyActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        // Error en el inicio de sesión, mostrar mensaje de error
                        Toast.makeText(LoginActivity.this, "Credenciales incorrectas o no registradas", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}


