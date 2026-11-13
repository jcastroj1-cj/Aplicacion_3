package com.jeancarlos.unidad1.network;

import java.io.IOException;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/** Ejecuta una llamada de Retrofit y entrega el resultado en el hilo principal. */
public final class Api {

    private Api() {
    }

    public static <T> void ejecutar(Call<T> llamada, final Resultado<T> resultado) {
        llamada.enqueue(new Callback<T>() {
            @Override
            public void onResponse(Call<T> call, Response<T> response) {
                if (response.isSuccessful()) {
                    resultado.ok(response.body());
                } else {
                    String detalle = "";
                    try {
                        if (response.errorBody() != null) {
                            detalle = response.errorBody().string();
                        }
                    } catch (IOException e) {
                        detalle = "";
                    }
                    String mensaje = "Error " + response.code();
                    if (!detalle.isEmpty()) {
                        mensaje += ": " + detalle;
                    }
                    resultado.error(response.code(), mensaje);
                }
            }

            @Override
            public void onFailure(Call<T> call, Throwable t) {
                resultado.error(0, "Sin conexión con el servidor: " + t.getMessage());
            }
        });
    }
}
