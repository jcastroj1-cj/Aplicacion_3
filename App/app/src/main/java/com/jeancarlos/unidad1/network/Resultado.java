package com.jeancarlos.unidad1.network;

public interface Resultado<T> {

    void ok(T datos);

    /** codigo = código HTTP, o 0 si no hubo conexión con el servidor. */
    void error(int codigo, String mensaje);
}
