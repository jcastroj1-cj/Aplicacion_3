package com.jeancarlos.unidad1.ui;

import android.content.Intent;
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

public class LoginActivity extends AppCompatActivity {

    private EditText txtCorreo;
    private EditText txtClave;
    private Button btnIngresar;
    private Button btnRegistro;
    private ProgressBar progreso;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        auth = FirebaseAuth.getInstance();
        txtCorreo = findViewById(R.id.txtCorreo);
        txtClave = findViewById(R.id.txtClave);
        btnIngresar = findViewById(R.id.btnIngresar);
        btnRegistro = findViewById(R.id.btnRegistro);
        progreso = findViewById(R.id.progreso);

        btnIngresar.setOnClickListener(v -> ingresar());
        btnRegistro.setOnClickListener(v ->
                startActivity(new Intent(LoginActivity.this, RegistroActivity.class)));
    }

    private void ingresar() {
        String correo = txtCorreo.getText().toString().trim();
        String clave = txtClave.getText().toString();
        if (correo.isEmpty() || clave.isEmpty()) {
            Toast.makeText(this, "Escribe el correo y la contraseña", Toast.LENGTH_SHORT).show();
            return;
        }
        mostrarCarga(true);
        auth.signInWithEmailAndPassword(correo, clave).addOnCompleteListener(task -> {
            if (task.isSuccessful() && auth.getCurrentUser() != null) {
                asegurarUsuarioEnApi(auth.getCurrentUser());
            } else {
                mostrarCarga(false);
                Toast.makeText(LoginActivity.this,
                        "Correo o contraseña incorrectos", Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * El UID de Firebase es el id del usuario en la API.
     * Si el usuario aún no existe en la API (por ejemplo, se registró antes de usar la API), se crea.
     */
    private void asegurarUsuarioEnApi(final FirebaseUser usuarioFirebase) {
        final String uid = usuarioFirebase.getUid();
        Api.ejecutar(RetrofitClient.api().obtenerUsuario(uid), new Resultado<Usuario>() {
            @Override
            public void ok(Usuario datos) {
                irAlInicio(uid);
            }

            @Override
            public void error(int codigo, String mensaje) {
                if (codigo == 404) {
                    crearUsuarioEnApi(usuarioFirebase);
                } else {
                    mostrarCarga(false);
                    Toast.makeText(LoginActivity.this, mensaje, Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void crearUsuarioEnApi(FirebaseUser usuarioFirebase) {
        final String uid = usuarioFirebase.getUid();
        String correo = usuarioFirebase.getEmail() == null ? "" : usuarioFirebase.getEmail();
        String nombre = correo.contains("@") ? correo.substring(0, correo.indexOf('@')) : "usuario";

        Api.ejecutar(RetrofitClient.api().crearUsuario(new Usuario(uid, nombre, correo)),
                new Resultado<Usuario>() {
                    @Override
                    public void ok(Usuario datos) {
                        irAlInicio(uid);
                    }

                    @Override
                    public void error(int codigo, String mensaje) {
                        mostrarCarga(false);
                        Toast.makeText(LoginActivity.this, mensaje, Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void irAlInicio(String uid) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("uid", uid);
        startActivity(intent);
        finish();
    }

    private void mostrarCarga(boolean mostrar) {
        progreso.setVisibility(mostrar ? View.VISIBLE : View.GONE);
        btnIngresar.setEnabled(!mostrar);
        btnRegistro.setEnabled(!mostrar);
    }
}
