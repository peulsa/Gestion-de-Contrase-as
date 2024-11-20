package com.example.tareahenrik;

import android.app.Activity;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.example.tareahenrik.Clave;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;

public class ViewPasswordsActivity extends AppCompatActivity {

    private ListView listViewClaves;
    private Button modificarClaveButton;
    private Button eliminarClaveButton;
    private KeyguardManager keyguardManager;
    private static final int REQUEST_CODE_CONFIRM_DEVICE_CREDENTIALS = 1;
    private String selectedClaveId; // ID de la clave seleccionada
    private Runnable pendingAction; // Almacena la acción a realizar tras la autenticación
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_passwords);

        listViewClaves = findViewById(R.id.clavesListView);  // Cambio al ID correcto
        modificarClaveButton = findViewById(R.id.modificarClaveButton);
        eliminarClaveButton = findViewById(R.id.eliminarClaveButton);
        keyguardManager = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);

        // Obtener UID del usuario actual
        String userUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference("usuarios").child(userUid);

        // Cargar claves del usuario
        cargarClaves();

        // Acción del botón de modificar clave
        modificarClaveButton.setOnClickListener(v -> verifyDeviceSecurity(() -> {
            // Si el patrón es correcto, redirige a la actividad de modificación
            Intent intent = new Intent(ViewPasswordsActivity.this, ModifyPasswordActivity.class);
            intent.putExtra("claveId", selectedClaveId);  // Pasar el ID de la clave seleccionada
            startActivity(intent);
        }));

        // Acción del botón de eliminar clave
        eliminarClaveButton.setOnClickListener(v -> verifyDeviceSecurity(this::showDeleteConfirmationDialog));
    }

    // Metodo para cargar las claves del usuario desde Firebase
    private void cargarClaves() {
        databaseReference.child("claves").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<Clave> clavesList = new ArrayList<>();
                ArrayList<String> claveIds = new ArrayList<>(); // Lista separada para IDs

                for (DataSnapshot claveSnapshot : snapshot.getChildren()) {
                    claveIds.add(claveSnapshot.getKey()); // Almacena el ID de la clave
                    String clave = claveSnapshot.child("clave").getValue(String.class);
                    String userName = claveSnapshot.child("userName").getValue(String.class);  // Asegúrate de que este campo esté en tu base de datos
                    clavesList.add(new Clave(userName, clave));  // Guardar el nombre de usuario y la clave
                }

                // Configurar el adaptador para mostrar la lista de claves
                ClavesAdapter adapter = new ClavesAdapter(ViewPasswordsActivity.this, clavesList);
                listViewClaves.setAdapter(adapter);

                // Seleccionar una clave de la lista
                listViewClaves.setOnItemClickListener((parent, view, position, id) -> {
                    selectedClaveId = claveIds.get(position); // Obtiene el ID de la clave seleccionada
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ViewPasswordsActivity.this, "Error al cargar claves", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Metodo para verificar la seguridad del dispositivo antes de realizar una acción
    private void verifyDeviceSecurity(Runnable onSuccess) {
        if (keyguardManager.isKeyguardSecure()) {
            pendingAction = onSuccess; // Guardamos la acción para ejecutarla después de la autenticación
            Intent intent = keyguardManager.createConfirmDeviceCredentialIntent("Autenticación requerida", "Verifique su identidad");
            startActivityForResult(intent, REQUEST_CODE_CONFIRM_DEVICE_CREDENTIALS);
        } else {
            Toast.makeText(this, "La seguridad del dispositivo no está habilitada", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CONFIRM_DEVICE_CREDENTIALS && resultCode == Activity.RESULT_OK) {
            if (pendingAction != null) {
                pendingAction.run(); // Ejecutar la acción guardada
                pendingAction = null; // Limpiar la acción pendiente
            }
        } else {
            Toast.makeText(this, "Autenticación fallida", Toast.LENGTH_SHORT).show();
        }
    }

    // Mostrar cuadro de diálogo de confirmación antes de eliminar la clave
    private void showDeleteConfirmationDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Confirmar eliminación")
                .setMessage("¿Estás seguro de que deseas eliminar esta clave?")
                .setPositiveButton("Sí", (dialog, which) -> deleteClave())
                .setNegativeButton("No", null)
                .show();
    }

    // Eliminar la clave seleccionada en Firebase
    private void deleteClave() {
        DatabaseReference userRef = databaseReference.child("claves").child(selectedClaveId);
        userRef.removeValue()
                .addOnSuccessListener(aVoid -> Toast.makeText(this, "Clave eliminada con éxito", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(this, "Error al eliminar la clave", Toast.LENGTH_SHORT).show());
    }
}
