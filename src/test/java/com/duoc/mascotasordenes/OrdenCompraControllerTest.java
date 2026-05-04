package com.duoc.mascotasordenes;

import com.duoc.mascotasordenes.controller.OrdenCompraController;
import com.duoc.mascotasordenes.dto.OrdenCompraDTO;
import com.duoc.mascotasordenes.service.OrdenCompraService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrdenCompraController.class)
public class OrdenCompraControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrdenCompraService service;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("GET /ordenes debe retornar lista de órdenes con status 200")
    void testObtenerTodas() throws Exception {
        // Arrange
        OrdenCompraDTO dto = new OrdenCompraDTO(
                1L, "María González", "Comida Premium para Perro 5kg",
                2, 15990.0, "CONFIRMADA", "2025-03-01"
        );
        when(service.obtenerTodas()).thenReturn(List.of(dto));

        // Act & Assert
        mockMvc.perform(get("/ordenes")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(service, times(1)).obtenerTodas();
    }

    @Test
    @DisplayName("POST /ordenes debe crear una orden y retornar status 201")
    void testCrearOrden() throws Exception {
        // Arrange
        OrdenCompraDTO dto = new OrdenCompraDTO(
                null, "Diego Muñoz", "Cama para Perro Mediano",
                1, 24990.0, "PENDIENTE", "2025-04-20"
        );
        OrdenCompraDTO dtoCreado = new OrdenCompraDTO(
                6L, "Diego Muñoz", "Cama para Perro Mediano",
                1, 24990.0, "PENDIENTE", "2025-04-20"
        );
        when(service.crearOrden(any(OrdenCompraDTO.class))).thenReturn(dtoCreado);

        // Act & Assert
        mockMvc.perform(post("/ordenes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());

        verify(service, times(1)).crearOrden(any(OrdenCompraDTO.class));
    }
}