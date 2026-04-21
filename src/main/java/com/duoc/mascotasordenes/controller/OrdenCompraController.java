package com.duoc.mascotasordenes.controller;

import com.duoc.mascotasordenes.model.OrdenCompra;
import com.duoc.mascotasordenes.service.OrdenCompraService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ordenes")
public class OrdenCompraController {

    @Autowired
    private OrdenCompraService service;

    // GET /ordenes → Listar todas las órdenes
    @GetMapping
    public ResponseEntity<List<OrdenCompra>> obtenerTodas() {
        return ResponseEntity.ok(service.obtenerTodas());
    }

    // GET /ordenes/{id} → Buscar por ID
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPorId(@PathVariable Long id) {
        return service.obtenerPorId(id)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(404).body("Orden con ID " + id + " no encontrada."));
    }

    // GET /ordenes/estado/{estado} → Filtrar por estado
    @GetMapping("/estado/{estado}")
    public ResponseEntity<?> obtenerPorEstado(@PathVariable String estado) {
        String estadoUpper = estado.toUpperCase();
        if (!estadoUpper.equals("PENDIENTE") && !estadoUpper.equals("CONFIRMADA") && !estadoUpper.equals("CANCELADA")) {
            return ResponseEntity.badRequest().body("Estado inválido. Use: PENDIENTE, CONFIRMADA o CANCELADA.");
        }
        List<OrdenCompra> resultado = service.obtenerPorEstado(estadoUpper);
        if (resultado.isEmpty()) {
            return ResponseEntity.ok("No hay órdenes con estado: " + estadoUpper);
        }
        return ResponseEntity.ok(resultado);
    }

    // POST /ordenes → Crear nueva orden
    @PostMapping
    public ResponseEntity<?> crearOrden(@RequestBody OrdenCompra orden) {
        if (orden.getNombreCliente() == null || orden.getNombreCliente().isBlank()) {
            return ResponseEntity.badRequest().body("El nombre del cliente es obligatorio.");
        }
        if (orden.getProducto() == null || orden.getProducto().isBlank()) {
            return ResponseEntity.badRequest().body("El producto es obligatorio.");
        }
        if (orden.getCantidad() <= 0) {
            return ResponseEntity.badRequest().body("La cantidad debe ser mayor a 0.");
        }
        if (orden.getPrecioUnitario() <= 0) {
            return ResponseEntity.badRequest().body("El precio unitario debe ser mayor a 0.");
        }
        return ResponseEntity.status(201).body(service.crearOrden(orden));
    }

    // PUT /ordenes/{id} → Actualizar orden
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarOrden(@PathVariable Long id, @RequestBody OrdenCompra datos) {
        if (datos.getEstado() != null) {
            String upper = datos.getEstado().toUpperCase();
            if (!upper.equals("PENDIENTE") && !upper.equals("CONFIRMADA") && !upper.equals("CANCELADA")) {
                return ResponseEntity.badRequest().body("Estado inválido. Use: PENDIENTE, CONFIRMADA o CANCELADA.");
            }
        }
        return service.actualizarOrden(id, datos)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(404).body("Orden con ID " + id + " no encontrada."));
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