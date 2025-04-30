package com.ejercicio.springboot.backend.apirest.models.services;

import java.util.List;

import com.ejercicio.springboot.backend.apirest.models.entity.Paquete;

public interface IPaqueteService {

	public List<Paquete> findAll();
	
	public Paquete findById(Long id);
	
	public Paquete save(Paquete paquete);
	
	public void delete(Long id);
	
}
