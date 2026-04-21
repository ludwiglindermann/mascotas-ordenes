package com.duoc.mascotasordenes.service;

import com.duoc.mascotasordenes.model.OrdenCompra;
import com.duoc.mascotasordenes.repository.OrdenCompraRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class OrdenCompraService {

    @Autowired
    private OrdenCompraRepository repository;

    public List<OrdenCompra> obtenerTodas() {
        return repository.findAll();
    }

    public Optional<OrdenCompra> obtenerPorId(Long id) {
        return repository.findById(id);
    }

    public List<OrdenCompra> obtenerPorEstado(String estado) {
        return repository.findByEstado(estado.toUpperCase());
    }

    public OrdenCompra crearOrden(OrdenCompra orden) {
        orden.setEstado("PENDIENTE");
        if (orden.getFechaCreacion() == null || orden.getFechaCreacion().isBlank()) {
            orden.setFechaCreacion(LocalDate.now().toString());
        }
        return repository.save(orden);
    }

    public Optional<OrdenCompra> actualizarOrden(Long id, OrdenCompra datos) {
        return repository.findById(id).map(orden -> {
            if (datos.getNombreCliente() != null && !datos.getNombreCliente().isBlank())
                orden.setNombreCliente(datos.getNombreCliente());
            if (datos.getProducto() != null && !datos.getProducto().isBlank())
                orden.setProducto(datos.getProducto());
            if (datos.getCantidad() > 0)
                orden.setCantidad(datos.getCantidad());
            if (datos.getPrecioUnitario() > 0)
                orden.setPrecioUnitario(datos.getPrecioUnitario());
            if (datos.getEstado() != null && !datos.getEstado().isBlank())
                orden.setEstado(datos.getEstado().toUpperCase());
            return repository.save(orden);
        });
    }

    public boolean eliminarOrden(Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return true;
        }
        return false;
    }
}