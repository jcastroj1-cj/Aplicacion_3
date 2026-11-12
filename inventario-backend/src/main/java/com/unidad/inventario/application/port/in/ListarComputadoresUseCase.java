package com.unidad.inventario.application.port.in;

import com.unidad.inventario.domain.model.Computador;
import java.util.List;

public interface ListarComputadoresUseCase {
    List<Computador> listarPorUsuario(String usuarioId);
}
