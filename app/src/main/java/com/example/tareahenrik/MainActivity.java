package com.example.tareahenrik;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.google.android.material.button.MaterialButton;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            // Temporalmente deshabilitado para pruebas
            // EdgeToEdge.enable(this);

            setContentView(R.layout.activity_main);

            // Configurar padding para sistema de barras si es necesario
            ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });

            // Agregar listeners a los botones
            MaterialButton loginButton = findViewById(R.id.inicioSesionButton);
            MaterialButton registerButton = findViewById(R.id.registroButton);

            // Verificar si los botones no son null antes de configurar los listeners
            if (loginButton != null) {
                loginButton.setOnClickListener(v -> {
                    Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(loginIntent);
                });
            } else {
                Log.e("MainActivity", "El botón de inicio de sesión no se ha encontrado.");
            }

            if (registerButton != null) {
                registerButton.setOnClickListener(v -> {
                    Intent registerIntent = new Intent(MainActivity.this, RegisterActivity.class);
                    startActivity(registerIntent);
                });
            } else {
                Log.e("MainActivity", "El botón de registro no se ha encontrado.");
            }

        } catch (Exception e) {
            Log.e("MainActivity", "Error en onCreate: ", e);
        }
    }
}


