package com.unidad.inventario.infrastructure.adapter.out.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "computadores",
        uniqueConstraints = @UniqueConstraint(columnNames = {"usuario_id", "codigo"}))
public class ComputadorEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "usuario_id", nullable = false)
    private String usuarioId;

    @Column(nullable = false)
    private String codigo;

    private String marca;
    private String categoria;
    private String marcaCpu;
    private String velocidadCpu;
    private String tecnologiaRam;
    private String capacidadRam;
    private String capacidadDisco;
    private String tecnologiaDisco;
    private String numPuertosUsb;
    private String numPuertosHdmi;
    private String marcaMonitor;
    private String pulgadas;
    private String precio;
    private Double latitud;
    private Double longitud;
    private Double luz;
    private String fecha;

    public ComputadorEntity() {
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getUsuarioId() { return usuarioId; }
    public void setUsuarioId(String usuarioId) { this.usuarioId = usuarioId; }
    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }
    public String getMarca() { return marca; }
    public void setMarca(String marca) { this.marca = marca; }
    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }
    public String getMarcaCpu() { return marcaCpu; }
    public void setMarcaCpu(String marcaCpu) { this.marcaCpu = marcaCpu; }
    public String getVelocidadCpu() { return velocidadCpu; }
    public void setVelocidadCpu(String velocidadCpu) { this.velocidadCpu = velocidadCpu; }
    public String getTecnologiaRam() { return tecnologiaRam; }
    public void setTecnologiaRam(String tecnologiaRam) { this.tecnologiaRam = tecnologiaRam; }
    public String getCapacidadRam() { return capacidadRam; }
    public void setCapacidadRam(String capacidadRam) { this.capacidadRam = capacidadRam; }
    public String getCapacidadDisco() { return capacidadDisco; }
    public void setCapacidadDisco(String capacidadDisco) { this.capacidadDisco = capacidadDisco; }
    public String getTecnologiaDisco() { return tecnologiaDisco; }
    public void setTecnologiaDisco(String tecnologiaDisco) { this.tecnologiaDisco = tecnologiaDisco; }
    public String getNumPuertosUsb() { return numPuertosUsb; }
    public void setNumPuertosUsb(String numPuertosUsb) { this.numPuertosUsb = numPuertosUsb; }
    public String getNumPuertosHdmi() { return numPuertosHdmi; }
    public void setNumPuertosHdmi(String numPuertosHdmi) { this.numPuertosHdmi = numPuertosHdmi; }
    public String getMarcaMonitor() { return marcaMonitor; }
    public void setMarcaMonitor(String marcaMonitor) { this.marcaMonitor = marcaMonitor; }
    public String getPulgadas() { return pulgadas; }
    public void setPulgadas(String pulgadas) { this.pulgadas = pulgadas; }
    public String getPrecio() { return precio; }
    public void setPrecio(String precio) { this.precio = precio; }
    public Double getLatitud() { return latitud; }
    public void setLatitud(Double latitud) { this.latitud = latitud; }
    public Double getLongitud() { return longitud; }
    public void setLongitud(Double longitud) { this.longitud = longitud; }
    public Double getLuz() { return luz; }
    public void setLuz(Double luz) { this.luz = luz; }
    public String getFecha() { return fecha; }
    public void setFecha(String fecha) { this.fecha = fecha; }
}
