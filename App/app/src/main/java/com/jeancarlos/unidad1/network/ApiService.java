package com.jeancarlos.unidad1.network;

import com.jeancarlos.unidad1.model.Computador;
import com.jeancarlos.unidad1.model.Usuario;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Endpoints de la API REST desplegada en Render
 * (la misma que se documenta en /swagger-ui/index.html).
 */
public interface ApiService {

    @GET("usuarios")
    Call<List<Usuario>> listarUsuarios();

    @GET("usuarios/{uid}")
    Call<Usuario> obtenerUsuario(@Path("uid") String uid);

    @POST("usuarios")
    Call<Usuario> crearUsuario(@Body Usuario usuario);

    @GET("usuarios/{uid}/computadores")
    Call<List<Computador>> listarComputadores(@Path("uid") String uid);

    @POST("usuarios/{uid}/computadores")
    Call<Computador> crearComputador(@Path("uid") String uid, @Body Computador computador);

    @PUT("usuarios/{uid}/computadores/{id}")
    Call<Computador> actualizarComputador(@Path("uid") String uid,
                                          @Path("id") long id,
                                          @Body Computador computador);

    @DELETE("usuarios/{uid}/computadores/{id}")
    Call<Void> eliminarComputador(@Path("uid") String uid, @Path("id") long id);
}
