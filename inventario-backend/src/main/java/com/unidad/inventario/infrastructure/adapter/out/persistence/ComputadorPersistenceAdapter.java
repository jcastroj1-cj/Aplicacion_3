package com.unidad.inventario.infrastructure.adapter.out.persistence;

import com.unidad.inventario.application.port.out.ComputadorRepositoryPort;
import com.unidad.inventario.domain.model.Computador;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Component;

@Component
public class ComputadorPersistenceAdapter implements ComputadorRepositoryPort {

    private final SpringDataComputadorRepository repository;

    public ComputadorPersistenceAdapter(SpringDataComputadorRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Computador> findByUsuarioId(String usuarioId) {
        return repository.findByUsuarioId(usuarioId).stream().map(this::toDomain).toList();
    }

    @Override
    public Optional<Computador> findByIdAndUsuarioId(Long id, String usuarioId) {
        return repository.findByIdAndUsuarioId(id, usuarioId).map(this::toDomain);
    }

    @Override
    public boolean existsByUsuarioIdAndCodigo(String usuarioId, String codigo) {
        return repository.existsByUsuarioIdAndCodigo(usuarioId, codigo);
    }

    @Override
    public Computador save(Computador c) {
        return toDomain(repository.save(toEntity(c)));
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    private ComputadorEntity toEntity(Computador c) {
        ComputadorEntity e = new ComputadorEntity();
        e.setId(c.id());
        e.setUsuarioId(c.usuarioId());
        e.setCodigo(c.codigo());
        e.setMarca(c.marca());
        e.setCategoria(c.categoria());
        e.setMarcaCpu(c.marcaCpu());
        e.setVelocidadCpu(c.velocidadCpu());
        e.setTecnologiaRam(c.tecnologiaRam());
        e.setCapacidadRam(c.capacidadRam());
        e.setCapacidadDisco(c.capacidadDisco());
        e.setTecnologiaDisco(c.tecnologiaDisco());
        e.setNumPuertosUsb(c.numPuertosUsb());
        e.setNumPuertosHdmi(c.numPuertosHdmi());
        e.setMarcaMonitor(c.marcaMonitor());
        e.setPulgadas(c.pulgadas());
        e.setPrecio(c.precio());
        e.setLatitud(c.latitud());
        e.setLongitud(c.longitud());
        e.setLuz(c.luz());
        e.setFecha(c.fecha());
        return e;
    }

    private Computador toDomain(ComputadorEntity e) {
        return new Computador(e.getId(), e.getUsuarioId(), e.getCodigo(), e.getMarca(),
                e.getCategoria(), e.getMarcaCpu(), e.getVelocidadCpu(), e.getTecnologiaRam(),
                e.getCapacidadRam(), e.getCapacidadDisco(), e.getTecnologiaDisco(),
                e.getNumPuertosUsb(), e.getNumPuertosHdmi(), e.getMarcaMonitor(),
                e.getPulgadas(), e.getPrecio(), e.getLatitud(), e.getLongitud(),
                e.getLuz(), e.getFecha());
    }
}
