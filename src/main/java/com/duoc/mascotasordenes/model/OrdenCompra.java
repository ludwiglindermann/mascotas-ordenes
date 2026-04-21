package com.duoc.mascotasordenes.model;

import jakarta.persistence.*;

@Entity
@Table(name = "ORDENES_COMPRA")
public class OrdenCompra {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_orden")
    @SequenceGenerator(name = "seq_orden", sequenceName = "SEQ_ORDENES_COMPRA", allocationSize = 1)
    private Long id;

    @Column(name = "NOMBRE_CLIENTE", nullable = false, length = 100)
    private String nombreCliente;

    @Column(name = "PRODUCTO", nullable = false, length = 200)
    private String producto;

    @Column(name = "CANTIDAD", nullable = false)
    private int cantidad;

    @Column(name = "PRECIO_UNITARIO", nullable = false)
    private double precioUnitario;

    @Column(name = "ESTADO", nullable = false, length = 20)
    private String estado;

    @Column(name = "FECHA_CREACION", length = 10)
    private String fechaCreacion;

    public OrdenCompra() {}

    public OrdenCompra(String nombreCliente, String producto, int cantidad,
                       double precioUnitario, String estado, String fechaCreacion) {
        this.nombreCliente = nombreCliente;
        this.producto = producto;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.estado = estado;
        this.fechaCreacion = fechaCreacion;
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

    public double getTotalOrden() { return cantidad * precioUnitario; }
}