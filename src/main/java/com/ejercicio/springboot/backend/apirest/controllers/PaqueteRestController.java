package com.ejercicio.springboot.backend.apirest.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.ejercicio.springboot.backend.apirest.models.entity.Paquete;
import com.ejercicio.springboot.backend.apirest.models.services.IPaqueteService;

import jakarta.validation.Valid;

@CrossOrigin(origins= {"http://localhost:4200"})
@RestController
@RequestMapping("/api")
public class PaqueteRestController {

	@Autowired
	private IPaqueteService paqueteService;
	
	@GetMapping("/paquetes")
	public List<Paquete> index() {
		return paqueteService.findAll();
	}
	
	@GetMapping("/paquetes/page/{page}")
	public Page<Paquete> index(@PathVariable Integer page) {
		Pageable pageable = PageRequest.of(page, 4);
		return paqueteService.findAll(pageable);
	}
	
	@GetMapping("/paquetes/{id}")
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<?> show(@PathVariable Long id) {
		
		Paquete paquete = null;
		Map<String, Object> response = new HashMap<>();
		
		try {
			paquete = paqueteService.findById(id);
		} catch(DataAccessException e) {
			response.put("mensaje", "¡Error al realizar la consulta en la base de datos!");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		if(paquete == null) {
			response.put("mensaje", "¡El paquete ID: ".concat(id.toString().concat(" no existe en la base de datos!")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity<Paquete>(paquete, HttpStatus.OK);
	}
	
	@PostMapping("/paquetes")
	public ResponseEntity<?> create(@Valid @RequestBody Paquete paquete, BindingResult result) {
		
		Paquete paqueteNew = null;
		Map<String, Object> response = new HashMap<>();
		
		if(result.hasErrors()) {
			
			/*List<String> errors = new ArrayList<>();
			
			for (FieldError err: result.getFieldErrors()) {
				errors.add("El campo '" + err.getField() + "' " + err.getDefaultMessage());
			}*/
			List<String> errors = result.getFieldErrors()
					.stream()
					.map(err -> "El campo '" + err.getField() + "' " + err.getDefaultMessage())
					.collect(Collectors.toList());
			
			response.put("errors", errors);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}
		
		try {
			paqueteNew = paqueteService.save(paquete);
		} catch(DataAccessException e) {
			response.put("mensaje", "¡Error al realizar la inserción en la base de datos!");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		response.put("mensaje", "¡El paquete ha sido creado con éxito!");
		response.put("paquete", paqueteNew);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}
	
	@PutMapping("/paquetes/{id}")
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<?> update(@Valid @RequestBody Paquete paquete, BindingResult result, @PathVariable Long id) {
		
		Paquete paqueteActual = paqueteService.findById(id);
		Paquete paqueteUpdated = null;
		
		Map<String, Object> response = new HashMap<>();
		
		if(result.hasErrors()) {
			
			List<String> errors = result.getFieldErrors()
					.stream()
					.map(err -> "El campo '" + err.getField() + "' " + err.getDefaultMessage())
					.collect(Collectors.toList());
			
			response.put("errors", errors);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}
		
		if(paqueteActual == null) {
			response.put("mensaje", "Error: ¡No se pudo editar, el paquete ID: "
					.concat(id.toString().concat(" no existe en la base de datos!")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		
		
		try {
		paqueteActual.setPedido(paquete.getPedido());
		paqueteActual.setPrecio(paquete.getPrecio());
		paqueteActual.setDestino(paquete.getDestino());
		paqueteActual.setFecha(paquete.getFecha());
		
		paqueteUpdated = paqueteService.save(paqueteActual);
		
		} catch(DataAccessException e) {
			response.put("mensaje", "¡Error al actualizar el paquete en la base de datos!");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		response.put("mensaje", "¡El paquete ha sido actualizado con éxito!");
		response.put("paquete", paqueteUpdated);
		
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
		
	}
	
	@DeleteMapping("/paquetes/{id}")
	public ResponseEntity<?> delete(@PathVariable Long id) {
		Map<String, Object> response = new HashMap<>();
		
		try {
			// Verificar si el paquete existe
			Paquete paquete = paqueteService.findById(id);
			if (paquete == null) {
				response.put("mensaje", "¡Error: El paquete ID: ".concat(id.toString().concat(" no existe en la base de datos!")));
				return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
			}
			
			// Eliminar el paquete
			paqueteService.delete(id);
		} catch (DataAccessException e) {
			response.put("mensaje", "¡Error al eliminar el paquete de la base de datos!");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		response.put("mensaje", "¡El paquete ha sido eliminado con éxito!");
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}
	
}
