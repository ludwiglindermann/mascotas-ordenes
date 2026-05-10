package com.duoc.mascotasordenes;

import com.duoc.mascotasordenes.dto.OrdenCompraDTO;
import com.duoc.mascotasordenes.model.OrdenCompra;
import com.duoc.mascotasordenes.repository.OrdenCompraRepository;
import com.duoc.mascotasordenes.service.OrdenCompraService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrdenCompraServiceTest {

    @Mock
    private OrdenCompraRepository repository;

    @InjectMocks
    private OrdenCompraService service;

    private OrdenCompra ordenMock;

    @BeforeEach
    void setUp() {
        ordenMock = new OrdenCompra();
        ordenMock.setId(1L);
        ordenMock.setNombreCliente("María González");
        ordenMock.setProducto("Comida Premium para Perro 5kg");
        ordenMock.setCantidad(2);
        ordenMock.setPrecioUnitario(15990.0);
        ordenMock.setEstado("CONFIRMADA");
        ordenMock.setFechaCreacion("2025-03-01");
    }

    @Test
    @DisplayName("Debe retornar todas las órdenes correctamente")
    void testObtenerTodas() {
        // Arrange
        when(repository.findAll()).thenReturn(List.of(ordenMock));

        // Act
        List<OrdenCompraDTO> resultado = service.obtenerTodas();

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("María González", resultado.get(0).getNombreCliente());
        verify(repository, times(1)).findAll();
    }

    @Test
    @DisplayName("Debe retornar una orden por ID correctamente")
    void testObtenerPorId() {
        // Arrange
        when(repository.findById(1L)).thenReturn(Optional.of(ordenMock));

        // Act
        Optional<OrdenCompraDTO> resultado = service.obtenerPorId(1L);

        // Assert
        assertTrue(resultado.isPresent());
        assertEquals("María González", resultado.get().getNombreCliente());
        assertEquals("CONFIRMADA", resultado.get().getEstado());
        verify(repository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Debe retornar lista vacía cuando no hay órdenes por estado")
    void testObtenerPorEstadoVacio() {
        // Arrange
        when(repository.findByEstado("CANCELADA")).thenReturn(List.of());

        // Act
        List<OrdenCompraDTO> resultado = service.obtenerPorEstado("CANCELADA");

        // Assert
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        verify(repository, times(1)).findByEstado("CANCELADA");
    }

    @Test
    @DisplayName("Debe crear una orden correctamente con estado PENDIENTE")
    void testCrearOrden() {
        // Arrange
        OrdenCompraDTO dto = new OrdenCompraDTO(
                null, "Diego Muñoz", "Cama para Perro",
                1, 24990.0, null, null
        );
        OrdenCompra ordenGuardada = new OrdenCompra();
        ordenGuardada.setId(6L);
        ordenGuardada.setNombreCliente("Diego Muñoz");
        ordenGuardada.setProducto("Cama para Perro");
        ordenGuardada.setCantidad(1);
        ordenGuardada.setPrecioUnitario(24990.0);
        ordenGuardada.setEstado("PENDIENTE");
        ordenGuardada.setFechaCreacion("2025-05-01");

        when(repository.save(any(OrdenCompra.class))).thenReturn(ordenGuardada);

        // Act
        OrdenCompraDTO resultado = service.crearOrden(dto);

        // Assert
        assertNotNull(resultado);
        assertEquals("PENDIENTE", resultado.getEstado());
        assertEquals("Diego Muñoz", resultado.getNombreCliente());
        verify(repository, times(1)).save(any(OrdenCompra.class));
    }
}