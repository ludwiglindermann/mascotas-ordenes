package com.duoc.mascotasordenes.controller;

import com.duoc.mascotasordenes.dto.OrdenCompraDTO;
import com.duoc.mascotasordenes.service.OrdenCompraService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/ordenes")
public class OrdenCompraController {

    @Autowired
    private OrdenCompraService service;

    // GET /ordenes → Listar todas las órdenes
    @GetMapping
    public ResponseEntity<CollectionModel<OrdenCompraDTO>> obtenerTodas() {
        List<OrdenCompraDTO> ordenes = service.obtenerTodas();
        ordenes.forEach(dto -> {
            dto.add(linkTo(methodOn(OrdenCompraController.class).obtenerPorId(dto.getId())).withSelfRel());
            dto.add(linkTo(methodOn(OrdenCompraController.class).obtenerTodas()).withRel("todas-las-ordenes"));
        });
        CollectionModel<OrdenCompraDTO> response = CollectionModel.of(ordenes,
                linkTo(methodOn(OrdenCompraController.class).obtenerTodas()).withSelfRel());
        return ResponseEntity.ok(response);
    }

    // GET /ordenes/{id} → Buscar por ID
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPorId(@PathVariable Long id) {
        Optional<OrdenCompraDTO> resultado = service.obtenerPorId(id);
        if (resultado.isPresent()) {
            OrdenCompraDTO dto = resultado.get();
            dto.add(linkTo(methodOn(OrdenCompraController.class).obtenerPorId(id)).withSelfRel());
            dto.add(linkTo(methodOn(OrdenCompraController.class).obtenerTodas()).withRel("todas-las-ordenes"));
            dto.add(Link.of("/ordenes/" + id).withRel("eliminar"));
            return ResponseEntity.ok(dto);
        }
        return ResponseEntity.status(404).body("Orden con ID " + id + " no encontrada.");
    }

    // GET /ordenes/estado/{estado} → Filtrar por estado
    @GetMapping("/estado/{estado}")
    public ResponseEntity<?> obtenerPorEstado(@PathVariable String estado) {
        String estadoUpper = estado.toUpperCase();
        if (!estadoUpper.equals("PENDIENTE") && !estadoUpper.equals("CONFIRMADA") && !estadoUpper.equals("CANCELADA")) {
            return ResponseEntity.badRequest().body("Estado inválido. Use: PENDIENTE, CONFIRMADA o CANCELADA.");
        }
        List<OrdenCompraDTO> resultado = service.obtenerPorEstado(estadoUpper);
        resultado.forEach(dto ->
            dto.add(linkTo(methodOn(OrdenCompraController.class).obtenerPorId(dto.getId())).withSelfRel())
        );
        if (resultado.isEmpty()) {
            return ResponseEntity.ok("No hay órdenes con estado: " + estadoUpper);
        }
        return ResponseEntity.ok(resultado);
    }

    // POST /ordenes → Crear nueva orden
    @PostMapping
    public ResponseEntity<?> crearOrden(@RequestBody OrdenCompraDTO dto) {
        if (dto.getNombreCliente() == null || dto.getNombreCliente().isBlank()) {
            return ResponseEntity.badRequest().body("El nombre del cliente es obligatorio.");
        }
        if (dto.getProducto() == null || dto.getProducto().isBlank()) {
            return ResponseEntity.badRequest().body("El producto es obligatorio.");
        }
        if (dto.getCantidad() <= 0) {
            return ResponseEntity.badRequest().body("La cantidad debe ser mayor a 0.");
        }
        if (dto.getPrecioUnitario() <= 0) {
            return ResponseEntity.badRequest().body("El precio unitario debe ser mayor a 0.");
        }
        OrdenCompraDTO nueva = service.crearOrden(dto);
        nueva.add(linkTo(methodOn(OrdenCompraController.class).obtenerPorId(nueva.getId())).withSelfRel());
        nueva.add(linkTo(methodOn(OrdenCompraController.class).obtenerTodas()).withRel("todas-las-ordenes"));
        return ResponseEntity.status(201).body(nueva);
    }

    // PUT /ordenes/{id} → Actualizar orden
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarOrden(@PathVariable Long id, @RequestBody OrdenCompraDTO dto) {
        if (dto.getEstado() != null) {
            String upper = dto.getEstado().toUpperCase();
            if (!upper.equals("PENDIENTE") && !upper.equals("CONFIRMADA") && !upper.equals("CANCELADA")) {
                return ResponseEntity.badRequest().body("Estado inválido. Use: PENDIENTE, CONFIRMADA o CANCELADA.");
            }
        }
        Optional<OrdenCompraDTO> resultado = service.actualizarOrden(id, dto);
        if (resultado.isPresent()) {
            OrdenCompraDTO actualizado = resultado.get();
            actualizado.add(linkTo(methodOn(OrdenCompraController.class).obtenerPorId(id)).withSelfRel());
            actualizado.add(linkTo(methodOn(OrdenCompraController.class).obtenerTodas()).withRel("todas-las-ordenes"));
            return ResponseEntity.ok(actualizado);
        }
        return ResponseEntity.status(404).body("Orden con ID " + id + " no encontrada.");
    }

    // DELETE /ordenes/{id} → Eliminar orden
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarOrden(@PathVariable Long id) {
        boolean eliminada = service.eliminarOrden(id);
        if (eliminada) {
            return ResponseEntity.ok("Orden con ID " + id + " eliminada correctamente.");
        }
        return ResponseEntity.status(404).body("Orden con ID " + id + " no encontrada.");
    }
}