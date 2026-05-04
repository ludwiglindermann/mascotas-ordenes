package com.duoc.mascotasordenes.dto;

import org.springframework.hateoas.RepresentationModel;

public class OrdenCompraDTO extends RepresentationModel<OrdenCompraDTO> {

    private Long id;
    private String nombreCliente;
    private String producto;
    private int cantidad;
    private double precioUnitario;
    private String estado;
    private String fechaCreacion;
    private double totalOrden;

    public OrdenCompraDTO() {}

    public OrdenCompraDTO(Long id, String nombreCliente, String producto,
                          int cantidad, double precioUnitario,
                          String estado, String fechaCreacion) {
        this.id = id;
        this.nombreCliente = nombreCliente;
        this.producto = producto;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.estado = estado;
        this.fechaCreacion = fechaCreacion;
        this.totalOrden = cantidad * precioUnitario;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombreCliente() { return nombreCliente; }
    public void setNombreCliente(String nombreCliente) { this.nombreCliente = nombreCliente; }

    public String getProducto() { return producto; }
    public void setProducto(String producto) { this.producto = producto; }

    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }

    public double getPrecioUnitario() { return precioUnitario; }
    public void setPrecioUnitario(double precioUnitario) { this.precioUnitario = precioUnitario; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(String fechaCreacion) { this.fechaCreacion = fechaCreacion; }

    public double getTotalOrden() { return totalOrden; }
    public void setTotalOrden(double totalOrden) { this.totalOrden = totalOrden; }
}