package com.ejercicio.springboot.backend.apirest.models.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ejercicio.springboot.backend.apirest.models.dao.IPaqueteDao;
import com.ejercicio.springboot.backend.apirest.models.entity.Paquete;

@Service
public class PaqueteServiceImpl implements IPaqueteService {

	@Autowired
	private IPaqueteDao paqueteDao;
	
	@Override
	@Transactional(readOnly = true)
	public List<Paquete> findAll() {
		return (List<Paquete>) paqueteDao.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public Paquete findById(Long id) {
		return paqueteDao.findById(id).orElse(null);
	}

	@Override
	@Transactional
	public Paquete save(Paquete paquete) {
		return paqueteDao.save(paquete);
	}

	@Override
	@Transactional
	public void delete(Long id) {
		paqueteDao.deleteById(id);
	}

}
