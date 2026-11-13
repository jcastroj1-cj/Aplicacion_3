package com.jeancarlos.unidad1.ui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.google.firebase.auth.FirebaseAuth;
import com.jeancarlos.unidad1.R;
import com.jeancarlos.unidad1.model.Computador;
import com.jeancarlos.unidad1.model.Usuario;
import com.jeancarlos.unidad1.network.Api;
import com.jeancarlos.unidad1.network.Resultado;
import com.jeancarlos.unidad1.network.RetrofitClient;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Pantalla principal: registrar, editar y eliminar computadores del usuario.
 * Sensores usados: GPS (LocationManager) y sensor de luz (SensorManager).
 */
public class MainActivity extends AppCompatActivity {

    private static final int CODIGO_PERMISO_UBICACION = 100;
    private static final String OPCION_NUEVO = "(nuevo computador)";

    private String uid;

    // Datos cargados desde la API: codigo -> computador
    private final Map<String, Computador> computadores = new LinkedHashMap<>();
    private final List<String> opciones = new ArrayList<>();
    private ArrayAdapter<String> adaptadorOpciones;

    // Vistas
    private TextView txtSaludo;
    private TextView txtSensores;
    private Spinner spinnerComputadores;
    private ProgressBar progreso;
    private EditText txtCodigo;
    private EditText txtMarca;
    private EditText txtCategoria;
    private EditText txtMarcaCpu;
    private EditText txtVelocidadCpu;
    private EditText txtTecnologiaRam;
    private EditText txtCapacidadRam;
    private EditText txtCapacidadDisco;
    private EditText txtTecnologiaDisco;
    private EditText txtNumPuertosUsb;
    private EditText txtNumPuertosHdmi;
    private EditText txtMarcaMonitor;
    private EditText txtPulgadas;
    private EditText txtPrecio;

    // Sensor 1: luz
    private SensorManager sensorManager;
    private Sensor sensorLuz;
    private Float luzActual;

    // Sensor 2: GPS
    private LocationManager locationManager;
    private Location ubicacionActual;

    private final SensorEventListener listenerLuz = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            luzActual = event.values[0];
            actualizarTextoSensores();
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    };

    private final LocationListener listenerUbicacion = new LocationListener() {
        @Override
        public void onLocationChanged(@NonNull Location location) {
            ubicacionActual = location;
            actualizarTextoSensores();
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

        @Override
        public void onProviderEnabled(@NonNull String provider) {
        }

        @Override
        public void onProviderDisabled(@NonNull String provider) {
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        uid = getIntent().getStringExtra("uid");
        if (uid == null) {
            irAlLogin();
            return;
        }

        enlazarVistas();
        configurarSpinner();
        configurarBotones();

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensorLuz = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        pedirPermisoUbicacion();
        actualizarTextoSensores();
        cargarSaludo();
        cargarComputadores();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (sensorManager != null && sensorLuz != null) {
            sensorManager.registerListener(listenerLuz, sensorLuz, SensorManager.SENSOR_DELAY_NORMAL);
        }
        iniciarUbicacion();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (sensorManager != null) {
            sensorManager.unregisterListener(listenerLuz);
        }
        if (locationManager != null) {
            locationManager.removeUpdates(listenerUbicacion);
        }
    }

    // ------------------------------------------------------------------ vistas

    private void enlazarVistas() {
        txtSaludo = findViewById(R.id.txtSaludo);
        txtSensores = findViewById(R.id.txtSensores);
        spinnerComputadores = findViewById(R.id.spinnerComputadores);
        progreso = findViewById(R.id.progreso);
        txtCodigo = findViewById(R.id.txtCodigo);
        txtMarca = findViewById(R.id.txtMarca);
        txtCategoria = findViewById(R.id.txtCategoria);
        txtMarcaCpu = findViewById(R.id.txtMarcaCpu);
        txtVelocidadCpu = findViewById(R.id.txtVelocidadCpu);
        txtTecnologiaRam = findViewById(R.id.txtTecnologiaRam);
        txtCapacidadRam = findViewById(R.id.txtCapacidadRam);
        txtCapacidadDisco = findViewById(R.id.txtCapacidadDisco);
        txtTecnologiaDisco = findViewById(R.id.txtTecnologiaDisco);
        txtNumPuertosUsb = findViewById(R.id.txtNumPuertosUsb);
        txtNumPuertosHdmi = findViewById(R.id.txtNumPuertosHdmi);
        txtMarcaMonitor = findViewById(R.id.txtMarcaMonitor);
        txtPulgadas = findViewById(R.id.txtPulgadas);
        txtPrecio = findViewById(R.id.txtPrecio);
    }

    private void configurarSpinner() {
        opciones.add(OPCION_NUEVO);
        adaptadorOpciones = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, opciones);
        spinnerComputadores.setAdapter(adaptadorOpciones);
        spinnerComputadores.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    Computador elegido = computadores.get(opciones.get(position));
                    if (elegido != null) {
                        llenarFormulario(elegido);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void configurarBotones() {
        Button btnGuardar = findViewById(R.id.btnGuardar);
        Button btnEliminar = findViewById(R.id.btnEliminar);
        Button btnLimpiar = findViewById(R.id.btnLimpiar);
        Button btnInventario = findViewById(R.id.btnInventario);
        Button btnSalir = findViewById(R.id.btnSalir);

        btnGuardar.setOnClickListener(v -> guardarComputador());
        btnEliminar.setOnClickListener(v -> eliminarComputador());
        btnLimpiar.setOnClickListener(v -> limpiarFormulario());
        btnInventario.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, InventarioActivity.class)));
        btnSalir.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            irAlLogin();
        });
    }

    private void irAlLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void mostrarCarga(boolean mostrar) {
        progreso.setVisibility(mostrar ? View.VISIBLE : View.GONE);
    }

    // ----------------------------------------------------------------- sensores

    private void pedirPermisoUbicacion() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION},
                    CODIGO_PERMISO_UBICACION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CODIGO_PERMISO_UBICACION) {
            iniciarUbicacion();
        }
    }

    @SuppressLint("MissingPermission")
    private void iniciarUbicacion() {
        if (locationManager == null) {
            return;
        }
        boolean permiso = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED;
        if (!permiso) {
            return;
        }
        try {
            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 0, listenerUbicacion);
                Location ultima = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if (ultima != null) {
                    ubicacionActual = ultima;
                }
            }
            if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 0, listenerUbicacion);
                if (ubicacionActual == null) {
                    ubicacionActual = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                }
            }
        } catch (Exception e) {
            // Sin proveedor disponible: se guarda sin ubicación
        }
        actualizarTextoSensores();
    }

    private void actualizarTextoSensores() {
        String gps = "sin señal";
        if (ubicacionActual != null) {
            gps = String.format(Locale.US, "%.5f, %.5f",
                    ubicacionActual.getLatitude(), ubicacionActual.getLongitude());
        }
        String luz = "sin datos";
        if (luzActual != null) {
            luz = String.format(Locale.US, "%.1f lx", luzActual);
        } else if (sensorLuz == null) {
            luz = "no disponible";
        }
        txtSensores.setText("GPS: " + gps + "   |   Luz: " + luz);
    }

    // --------------------------------------------------------------- API REST

    private void cargarSaludo() {
        Api.ejecutar(RetrofitClient.api().obtenerUsuario(uid), new Resultado<Usuario>() {
            @Override
            public void ok(Usuario datos) {
                txtSaludo.setText("Hola, " + (datos == null ? "usuario" : datos.nombre));
            }

            @Override
            public void error(int codigo, String mensaje) {
                txtSaludo.setText("Hola");
                Toast.makeText(MainActivity.this, mensaje, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void cargarComputadores() {
        mostrarCarga(true);
        Api.ejecutar(RetrofitClient.api().listarComputadores(uid), new Resultado<List<Computador>>() {
            @Override
            public void ok(List<Computador> datos) {
                mostrarCarga(false);
                computadores.clear();
                opciones.clear();
                opciones.add(OPCION_NUEVO);
                if (datos != null) {
                    for (Computador c : datos) {
                        computadores.put(c.codigo, c);
                        opciones.add(c.codigo);
                    }
                }
                adaptadorOpciones.notifyDataSetChanged();
                spinnerComputadores.setSelection(0, false);
            }

            @Override
            public void error(int codigo, String mensaje) {
                mostrarCarga(false);
                Toast.makeText(MainActivity.this, "Error al cargar computadores: " + mensaje,
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    private void guardarComputador() {
        String codigo = txtCodigo.getText().toString().trim();
        if (codigo.isEmpty()) {
            Toast.makeText(this, "Escribe el Id del computador", Toast.LENGTH_SHORT).show();
            return;
        }

        Computador datos = leerFormulario(codigo);
        Computador existente = computadores.get(codigo);

        mostrarCarga(true);
        Resultado<Computador> resultado = new Resultado<Computador>() {
            @Override
            public void ok(Computador guardado) {
                mostrarCarga(false);
                Toast.makeText(MainActivity.this, "Computador guardado correctamente", Toast.LENGTH_SHORT).show();
                limpiarFormulario();
                cargarComputadores();
            }

            @Override
            public void error(int codigo, String mensaje) {
                mostrarCarga(false);
                Toast.makeText(MainActivity.this, "Error al guardar: " + mensaje, Toast.LENGTH_LONG).show();
            }
        };

        if (existente != null) {
            Api.ejecutar(RetrofitClient.api().actualizarComputador(uid, existente.id, datos), resultado);
        } else {
            Api.ejecutar(RetrofitClient.api().crearComputador(uid, datos), resultado);
        }
    }

    private void eliminarComputador() {
        String codigo = txtCodigo.getText().toString().trim();
        Computador existente = computadores.get(codigo);
        if (existente == null) {
            Toast.makeText(this, "Selecciona un computador primero", Toast.LENGTH_SHORT).show();
            return;
        }

        mostrarCarga(true);
        Api.ejecutar(RetrofitClient.api().eliminarComputador(uid, existente.id), new Resultado<Void>() {
            @Override
            public void ok(Void datos) {
                mostrarCarga(false);
                Toast.makeText(MainActivity.this, "Computador eliminado", Toast.LENGTH_SHORT).show();
                limpiarFormulario();
                cargarComputadores();
            }

            @Override
            public void error(int codigo, String mensaje) {
                mostrarCarga(false);
                Toast.makeText(MainActivity.this, "Error al eliminar: " + mensaje, Toast.LENGTH_LONG).show();
            }
        });
    }

    // -------------------------------------------------------------- formulario

    private String texto(EditText campo) {
        return campo.getText().toString().trim();
    }

    private Computador leerFormulario(String codigo) {
        Computador c = new Computador();
        c.codigo = codigo;
        c.marca = texto(txtMarca);
        c.categoria = texto(txtCategoria);
        c.marcaCpu = texto(txtMarcaCpu);
        c.velocidadCpu = texto(txtVelocidadCpu);
        c.tecnologiaRam = texto(txtTecnologiaRam);
        c.capacidadRam = texto(txtCapacidadRam);
        c.capacidadDisco = texto(txtCapacidadDisco);
        c.tecnologiaDisco = texto(txtTecnologiaDisco);
        c.numPuertosUsb = texto(txtNumPuertosUsb);
        c.numPuertosHdmi = texto(txtNumPuertosHdmi);
        c.marcaMonitor = texto(txtMarcaMonitor);
        c.pulgadas = texto(txtPulgadas);
        c.precio = texto(txtPrecio);

        // Datos de los sensores
        if (ubicacionActual != null) {
            c.latitud = ubicacionActual.getLatitude();
            c.longitud = ubicacionActual.getLongitude();
        }
        if (luzActual != null) {
            c.luz = (double) luzActual;
        }
        return c;
    }

    private String vacioSiNulo(String valor) {
        return valor == null ? "" : valor;
    }

    private void llenarFormulario(Computador c) {
        txtCodigo.setText(vacioSiNulo(c.codigo));
        txtMarca.setText(vacioSiNulo(c.marca));
        txtCategoria.setText(vacioSiNulo(c.categoria));
        txtMarcaCpu.setText(vacioSiNulo(c.marcaCpu));
        txtVelocidadCpu.setText(vacioSiNulo(c.velocidadCpu));
        txtTecnologiaRam.setText(vacioSiNulo(c.tecnologiaRam));
        txtCapacidadRam.setText(vacioSiNulo(c.capacidadRam));
        txtCapacidadDisco.setText(vacioSiNulo(c.capacidadDisco));
        txtTecnologiaDisco.setText(vacioSiNulo(c.tecnologiaDisco));
        txtNumPuertosUsb.setText(vacioSiNulo(c.numPuertosUsb));
        txtNumPuertosHdmi.setText(vacioSiNulo(c.numPuertosHdmi));
        txtMarcaMonitor.setText(vacioSiNulo(c.marcaMonitor));
        txtPulgadas.setText(vacioSiNulo(c.pulgadas));
        txtPrecio.setText(vacioSiNulo(c.precio));
    }

    private void limpiarFormulario() {
        txtCodigo.setText("");
        txtMarca.setText("");
        txtCategoria.setText("");
        txtMarcaCpu.setText("");
        txtVelocidadCpu.setText("");
        txtTecnologiaRam.setText("");
        txtCapacidadRam.setText("");
        txtCapacidadDisco.setText("");
        txtTecnologiaDisco.setText("");
        txtNumPuertosUsb.setText("");
        txtNumPuertosHdmi.setText("");
        txtMarcaMonitor.setText("");
        txtPulgadas.setText("");
        txtPrecio.setText("");
        spinnerComputadores.setSelection(0, false);
    }
}
