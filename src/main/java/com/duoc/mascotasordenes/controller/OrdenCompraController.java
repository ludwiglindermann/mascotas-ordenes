package com.duoc.mascotasordenes.controller;

import com.duoc.mascotasordenes.model.OrdenCompra;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/ordenes")
public class OrdenCompraController {

    private List<OrdenCompra> ordenes = new ArrayList<>(List.of(
        new OrdenCompra(1L, "María González", "Comida Premium para Perro 5kg", 2, 15990.0, "CONFIRMADA", "2025-03-01"),
        new OrdenCompra(2L, "Juan Pérez",     "Arena para Gato 10kg",          1,  8990.0, "PENDIENTE",  "2025-03-05"),
        new OrdenCompra(3L, "Ana Rodríguez",  "Collar Antipulgas para Gato",   3,  4500.0, "CANCELADA",  "2025-03-08"),
        new OrdenCompra(4L, "Carlos López",   "Juguete Hueso Masticable",      5,  2990.0, "CONFIRMADA", "2025-03-10"),
        new OrdenCompra(5L, "Sofía Martínez", "Vitaminas para Perro Adulto",   2, 12500.0, "PENDIENTE",  "2025-03-12")
    ));

    private long nextId = 6L;

    // GET /ordenes → Listar todas las órdenes
    @GetMapping
    public ResponseEntity<List<OrdenCompra>> obtenerTodas() {
        return ResponseEntity.ok(ordenes);
    }

    // GET /ordenes/{id} → Buscar por ID
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPorId(@PathVariable Long id) {
        Optional<OrdenCompra> orden = ordenes.stream()
                .filter(o -> o.getId().equals(id))
                .findFirst();
        if (orden.isPresent()) {
            return ResponseEntity.ok(orden.get());
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
        List<OrdenCompra> resultado = ordenes.stream()
                .filter(o -> o.getEstado().equalsIgnoreCase(estadoUpper))
                .collect(Collectors.toList());
        return ResponseEntity.ok(resultado);
    }

    // POST /ordenes → Crear nueva orden
    @PostMapping
    public ResponseEntity<?> crearOrden(@RequestBody OrdenCompra nuevaOrden) {
        if (nuevaOrden.getNombreCliente() == null || nuevaOrden.getNombreCliente().isBlank()) {
            return ResponseEntity.badRequest().body("El nombre del cliente es obligatorio.");
        }
        if (nuevaOrden.getProducto() == null || nuevaOrden.getProducto().isBlank()) {
            return ResponseEntity.badRequest().body("El producto es obligatorio.");
        }
        if (nuevaOrden.getCantidad() <= 0) {
            return ResponseEntity.badRequest().body("La cantidad debe ser mayor a 0.");
        }
        if (nuevaOrden.getPrecioUnitario() <= 0) {
            return ResponseEntity.badRequest().body("El precio unitario debe ser mayor a 0.");
        }
        nuevaOrden.setId(nextId++);
        nuevaOrden.setEstado("PENDIENTE");
        if (nuevaOrden.getFechaCreacion() == null || nuevaOrden.getFechaCreacion().isBlank()) {
            nuevaOrden.setFechaCreacion(java.time.LocalDate.now().toString());
        }
        ordenes.add(nuevaOrden);
        return ResponseEntity.status(201).body(nuevaOrden);
    }

    // PUT /ordenes/{id} → Actualizar orden
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarOrden(@PathVariable Long id, @RequestBody OrdenCompra datos) {
        Optional<OrdenCompra> ordenExistente = ordenes.stream()
                .filter(o -> o.getId().equals(id))
                .findFirst();
        if (ordenExistente.isEmpty()) {
            return ResponseEntity.status(404).body("Orden con ID " + id + " no encontrada.");
        }
        OrdenCompra orden = ordenExistente.get();
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
        return ResponseEntity.ok(orden);
    }

    // DELETE /ordenes/{id} → Eliminar orden
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarOrden(@PathVariable Long id) {
        boolean eliminada = ordenes.removeIf(o -> o.getId().equals(id));
        if (eliminada) {
            return ResponseEntity.ok("Orden con ID " + id + " eliminada correctamente.");
        }
        return ResponseEntity.status(404).body("Orden con ID " + id + " no encontrada.");
    }
}