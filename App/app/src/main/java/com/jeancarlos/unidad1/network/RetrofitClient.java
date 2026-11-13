package com.jeancarlos.unidad1.network;

import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public final class RetrofitClient {

    /** URL base de la API en Render (debe terminar con "/"). */
    public static final String BASE_URL = "https://aplicacion-u3.onrender.com/";

    private static ApiService servicio;

    private RetrofitClient() {
    }

    public static synchronized ApiService api() {
        if (servicio == null) {
            HttpLoggingInterceptor log = new HttpLoggingInterceptor();
            log.setLevel(HttpLoggingInterceptor.Level.BODY);

            // El plan gratis de Render se duerme: la primera petición puede tardar más de un minuto.
            OkHttpClient cliente = new OkHttpClient.Builder()
                    .connectTimeout(120, TimeUnit.SECONDS)
                    .readTimeout(120, TimeUnit.SECONDS)
                    .writeTimeout(120, TimeUnit.SECONDS)
                    .addInterceptor(log)
                    .build();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(cliente)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            servicio = retrofit.create(ApiService.class);
        }
        return servicio;
    }
}
