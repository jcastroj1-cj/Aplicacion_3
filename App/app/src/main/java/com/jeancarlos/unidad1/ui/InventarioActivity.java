package com.jeancarlos.unidad1.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.jeancarlos.unidad1.R;
import com.jeancarlos.unidad1.model.Computador;
import com.jeancarlos.unidad1.model.Usuario;
import com.jeancarlos.unidad1.network.Api;
import com.jeancarlos.unidad1.network.Resultado;
import com.jeancarlos.unidad1.network.RetrofitClient;
import java.util.ArrayList;
import java.util.List;

/** Inventario: se elige un usuario y se listan sus computadores. */
public class InventarioActivity extends AppCompatActivity {

    private static final String PLACEHOLDER = "Seleccione un usuario";

    private Spinner spinnerUsuarios;
    private ListView listaComputadores;
    private TextView txtTotal;
    private ProgressBar progreso;

    private final List<Usuario> usuarios = new ArrayList<>();
    private final List<String> opcionesUsuarios = new ArrayList<>();
    private final List<String> filas = new ArrayList<>();
    private ArrayAdapter<String> adaptadorUsuarios;
    private ArrayAdapter<String> adaptadorFilas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventario);

        spinnerUsuarios = findViewById(R.id.spinnerUsuarios);
        listaComputadores = findViewById(R.id.listaComputadores);
        txtTotal = findViewById(R.id.txtTotal);
        progreso = findViewById(R.id.progreso);
        Button btnRegresar = findViewById(R.id.btnRegresar);
        btnRegresar.setOnClickListener(v -> finish());

        opcionesUsuarios.add(PLACEHOLDER);
        adaptadorUsuarios = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, opcionesUsuarios);
        spinnerUsuarios.setAdapter(adaptadorUsuarios);

        adaptadorFilas = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, filas);
        listaComputadores.setAdapter(adaptadorFilas);

        spinnerUsuarios.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    cargarComputadores(usuarios.get(position - 1).id);
                } else {
                    mostrarComputadores(new ArrayList<Computador>());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        cargarUsuarios();
    }

    private void cargarUsuarios() {
        progreso.setVisibility(View.VISIBLE);
        Api.ejecutar(RetrofitClient.api().listarUsuarios(), new Resultado<List<Usuario>>() {
            @Override
            public void ok(List<Usuario> datos) {
                progreso.setVisibility(View.GONE);
                usuarios.clear();
                opcionesUsuarios.clear();
                opcionesUsuarios.add(PLACEHOLDER);
                if (datos != null) {
                    for (Usuario u : datos) {
                        usuarios.add(u);
                        opcionesUsuarios.add(u.nombre + " (" + u.correo + ")");
                    }
                }
                adaptadorUsuarios.notifyDataSetChanged();
            }

            @Override
            public void error(int codigo, String mensaje) {
                progreso.setVisibility(View.GONE);
                Toast.makeText(InventarioActivity.this,
                        "Error al cargar usuarios: " + mensaje, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void cargarComputadores(String uid) {
        progreso.setVisibility(View.VISIBLE);
        Api.ejecutar(RetrofitClient.api().listarComputadores(uid), new Resultado<List<Computador>>() {
            @Override
            public void ok(List<Computador> datos) {
                progreso.setVisibility(View.GONE);
                mostrarComputadores(datos == null ? new ArrayList<Computador>() : datos);
            }

            @Override
            public void error(int codigo, String mensaje) {
                progreso.setVisibility(View.GONE);
                Toast.makeText(InventarioActivity.this,
                        "Error al cargar: " + mensaje, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void mostrarComputadores(List<Computador> lista) {
        filas.clear();
        for (Computador c : lista) {
            String marca = c.marca == null ? "" : c.marca;
            String categoria = c.categoria == null ? "" : c.categoria;
            filas.add(c.codigo + "  -  " + marca + " " + categoria);
        }
        adaptadorFilas.notifyDataSetChanged();
        txtTotal.setText("Total de equipos: " + lista.size());
    }
}
