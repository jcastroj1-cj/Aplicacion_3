package com.jeancarlos.unidad1.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.jeancarlos.unidad1.R;
import com.jeancarlos.unidad1.model.Usuario;
import com.jeancarlos.unidad1.network.Api;
import com.jeancarlos.unidad1.network.Resultado;
import com.jeancarlos.unidad1.network.RetrofitClient;

public class RegistroActivity extends AppCompatActivity {

    private EditText txtNombre;
    private EditText txtCorreo;
    private EditText txtClave;
    private Button btnRegistrar;
    private Button btnVolver;
    private ProgressBar progreso;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        auth = FirebaseAuth.getInstance();
        txtNombre = findViewById(R.id.txtNombre);
        txtCorreo = findViewById(R.id.txtCorreo);
        txtClave = findViewById(R.id.txtClave);
        btnRegistrar = findViewById(R.id.btnRegistrar);
        btnVolver = findViewById(R.id.btnVolver);
        progreso = findViewById(R.id.progreso);

        btnRegistrar.setOnClickListener(v -> registrar());
        btnVolver.setOnClickListener(v -> finish());
    }

    private void registrar() {
        final String nombre = txtNombre.getText().toString().trim();
        final String correo = txtCorreo.getText().toString().trim();
        String clave = txtClave.getText().toString();

        if (nombre.isEmpty() || correo.isEmpty() || clave.isEmpty()) {
            Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }
        if (clave.length() < 6) {
            Toast.makeText(this, "La contraseña debe tener al menos 6 caracteres", Toast.LENGTH_SHORT).show();
            return;
        }

        mostrarCarga(true);
        auth.createUserWithEmailAndPassword(correo, clave).addOnCompleteListener(task -> {
            FirebaseUser usuario = auth.getCurrentUser();
            if (task.isSuccessful() && usuario != null) {
                guardarUsuarioEnApi(usuario.getUid(), nombre, correo);
            } else {
                mostrarCarga(false);
                Toast.makeText(RegistroActivity.this,
                        "No se pudo registrar. Revisa el correo o usa otro", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void guardarUsuarioEnApi(String uid, String nombre, String correo) {
        Api.ejecutar(RetrofitClient.api().crearUsuario(new Usuario(uid, nombre, correo)),
                new Resultado<Usuario>() {
                    @Override
                    public void ok(Usuario datos) {
                        Toast.makeText(RegistroActivity.this, "Registro exitoso", Toast.LENGTH_LONG).show();
                        auth.signOut();
                        finish();
                    }

                    @Override
                    public void error(int codigo, String mensaje) {
                        mostrarCarga(false);
                        auth.signOut();
                        Toast.makeText(RegistroActivity.this,
                                "Cuenta creada, pero no se pudo guardar en el servidor (" + mensaje
                                        + "). Se completará al iniciar sesión.", Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void mostrarCarga(boolean mostrar) {
        progreso.setVisibility(mostrar ? View.VISIBLE : View.GONE);
        btnRegistrar.setEnabled(!mostrar);
        btnVolver.setEnabled(!mostrar);
    }
}
