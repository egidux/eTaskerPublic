package org.eTasker.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.eTasker.model.Material;
import org.eTasker.repository.MaterialRepository;
import org.eTasker.tool.JsonBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MateriaImpl implements MaterialService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(MaterialService.class);
	
	@Autowired
	private MaterialRepository materialRepository;

	@Override
	public List<Material> findAll() {
		List<Material> materials = materialRepository.findAll();
		if (materials == null) {
			LOGGER.debug("Failed to retrieve all materials");
		}
		LOGGER.info("Materials: " + materials);
		return materials;
	}
	
	@Override
	public Material findOne(Long id) {
		Material material = materialRepository.findOne(id);
		if (material == null) {
			LOGGER.debug("Not found material with id=" + id);
		}
		LOGGER.info("Found material:" + JsonBuilder.build(material));
		return material;
	}

	@Override
	public Material create(Material material) {
		material.setCreated(new SimpleDateFormat("dd.MM.yyyy:HH.mm.ss").format(new Date()));
		Material newMaterial = materialRepository.save(material);
		if (newMaterial == null) {
			LOGGER.debug("Failed create new material: " + JsonBuilder.build(material));
		}
		LOGGER.debug("Created new material: " + JsonBuilder.build(newMaterial));
		return newMaterial;
	}

	@Override
	public Material update(Material material, Long id) {
		Material materialUpdate = findOne(id);
		if (materialUpdate == null) {
			LOGGER.info("Failed update, material with id=" + id + " not exists");
			return null;
		}
		if (material.getSerial_number()!= null && !material.getSerial_number().isEmpty()) {
			materialUpdate.setSerial_number(material.getSerial_number());
			LOGGER.info("Material with id=" + id + " updated serial number= " + material.getSerial_number());
		}
		if (material.getTitle() != null && !material.getTitle().isEmpty()) {
			materialUpdate.setTitle(material.getTitle());
			LOGGER.info("Material with id=" + id + " updated title= " + material.getTitle());
		}
		materialUpdate.setUpdated(new SimpleDateFormat("dd.MM.yyyy:HH.mm.ss").format(new Date()));
		LOGGER.info("Material with id=" + id + " updated");
		return materialRepository.save(materialUpdate);
	}

	@Override
	public void delete(Material material) {
		materialRepository.delete(material);
		LOGGER.info("Deleted material with id=" + material.getId());
	}
}
