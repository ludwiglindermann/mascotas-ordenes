package com.duoc.mascotasordenes.service;

import com.duoc.mascotasordenes.dto.OrdenCompraDTO;
import com.duoc.mascotasordenes.model.OrdenCompra;
import com.duoc.mascotasordenes.repository.OrdenCompraRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrdenCompraService {

    @Autowired
    private OrdenCompraRepository repository;

    // Convierte Entity → DTO
    private OrdenCompraDTO convertirADTO(OrdenCompra orden) {
        return new OrdenCompraDTO(
                orden.getId(),
                orden.getNombreCliente(),
                orden.getProducto(),
                orden.getCantidad(),
                orden.getPrecioUnitario(),
                orden.getEstado(),
                orden.getFechaCreacion()
        );
    }

    // Convierte DTO → Entity
    private OrdenCompra convertirAEntity(OrdenCompraDTO dto) {
        OrdenCompra orden = new OrdenCompra();
        orden.setNombreCliente(dto.getNombreCliente());
        orden.setProducto(dto.getProducto());
        orden.setCantidad(dto.getCantidad());
        orden.setPrecioUnitario(dto.getPrecioUnitario());
        orden.setEstado(dto.getEstado());
        orden.setFechaCreacion(dto.getFechaCreacion());
        return orden;
    }

    public List<OrdenCompraDTO> obtenerTodas() {
        return repository.findAll().stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    public Optional<OrdenCompraDTO> obtenerPorId(Long id) {
        return repository.findById(id).map(this::convertirADTO);
    }

    public List<OrdenCompraDTO> obtenerPorEstado(String estado) {
        return repository.findByEstado(estado.toUpperCase()).stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    public OrdenCompraDTO crearOrden(OrdenCompraDTO dto) {
        OrdenCompra orden = convertirAEntity(dto);
        orden.setEstado("PENDIENTE");
        if (orden.getFechaCreacion() == null || orden.getFechaCreacion().isBlank()) {
            orden.setFechaCreacion(LocalDate.now().toString());
        }
        return convertirADTO(repository.save(orden));
    }

    public Optional<OrdenCompraDTO> actualizarOrden(Long id, OrdenCompraDTO dto) {
        return repository.findById(id).map(orden -> {
            if (dto.getNombreCliente() != null && !dto.getNombreCliente().isBlank())
                orden.setNombreCliente(dto.getNombreCliente());
            if (dto.getProducto() != null && !dto.getProducto().isBlank())
                orden.setProducto(dto.getProducto());
            if (dto.getCantidad() > 0)
                orden.setCantidad(dto.getCantidad());
            if (dto.getPrecioUnitario() > 0)
                orden.setPrecioUnitario(dto.getPrecioUnitario());
            if (dto.getEstado() != null && !dto.getEstado().isBlank())
                orden.setEstado(dto.getEstado().toUpperCase());
            return convertirADTO(repository.save(orden));
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