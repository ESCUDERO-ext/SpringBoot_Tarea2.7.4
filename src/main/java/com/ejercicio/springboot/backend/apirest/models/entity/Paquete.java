package com.ejercicio.springboot.backend.apirest.models.entity;

import java.io.Serializable;
import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

@Entity
@Table(name="paquetes")
public class Paquete implements Serializable {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@NotEmpty(message = "No puede estar vacío")
	@Size(min=4, message = "El tamaño tiene que ser mínimo de 4")
	@Column(nullable= false)
	private String pedido;
	
	@NotEmpty(message = "No puede estar vacío")
	@Column(nullable= false)
	private double precio;
	
	@NotEmpty(message = "No puede estar vacío")
	@Column(nullable= false)
	@Size(min=4, message = "El tamaño tiene que ser mínimo de 4")
	private String destino;
	
	@Temporal(TemporalType.DATE)
	private Date fecha;

	@PrePersist
	public void prePersist() {
		fecha = new Date();
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getPedido() {
		return pedido;
	}

	public void setPedido(String pedido) {
		this.pedido = pedido;
	}

	public double getPrecio() {
		return precio;
	}

	public void setPrecio(double precio) {
		this.precio = precio;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public String getDestino() {
		return destino;
	}

	public void setDestino(String destino) {
		this.destino = destino;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
}
