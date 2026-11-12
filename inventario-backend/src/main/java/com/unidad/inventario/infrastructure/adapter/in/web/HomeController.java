package com.unidad.inventario.infrastructure.adapter.in.web;

import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Al abrir la dirección base del servicio se muestra la documentación (Swagger UI).
 */
@Hidden
@Controller
public class HomeController {

    @GetMapping("/")
    public String inicio() {
        return "redirect:/swagger-ui/index.html";
    }
}
