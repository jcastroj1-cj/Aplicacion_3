package com.unidad.inventario.infrastructure.config;

import com.unidad.inventario.application.port.out.ComputadorRepositoryPort;
import com.unidad.inventario.application.port.out.UsuarioRepositoryPort;
import com.unidad.inventario.application.service.ComputadorService;
import com.unidad.inventario.application.service.UsuarioService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Aquí se "enchufan" los servicios de aplicación (que no conocen Spring)
 * con los adaptadores de salida.
 */
@Configuration
public class BeanConfig {

    @Bean
    public UsuarioService usuarioService(UsuarioRepositoryPort usuarioRepository) {
        return new UsuarioService(usuarioRepository);
    }

    @Bean
    public ComputadorService computadorService(ComputadorRepositoryPort computadorRepository,
                                               UsuarioRepositoryPort usuarioRepository) {
        return new ComputadorService(computadorRepository, usuarioRepository);
    }
}
