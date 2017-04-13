package org.eTasker.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eTasker.model.Material;
import org.eTasker.repository.MaterialRepository;
import org.eTasker.tool.JsonBuilder;
import org.eTasker.tool.TimeStamp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MateriaImpl implements MaterialService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(MaterialService.class);
	private static Map<Long, List<Material>> usedMaterialDB = new HashMap<>();
	
	@Autowired
	private MaterialRepository materialRepository;

	@Override
	public List<Material> findAll() {
		List<Material> materials = materialRepository.findAll();
		if (materials == null) {
			LOGGER.debug("Failed to retrieve all materials");
		}
		LOGGER.info("Materials: " + JsonBuilder.build(materials));
		return materials;
	}
	
	@Override
	public List<Material> findAllUsed() {
		List<Material> materials = new ArrayList<>();
		for (Long key: usedMaterialDB.keySet()) {
			materials.addAll(usedMaterialDB.get(key));
		}
		LOGGER.info("Used materials: " + JsonBuilder.build(materials));
		return materials;
	}
	
	@Override
	public Material findOne(Long id) {
		Material material = materialRepository.findOne(id);
		LOGGER.info("Found material:" + JsonBuilder.build(material));
		return material;
	}

	@Override
	public Material create(Material material) {
		Material newMaterial = materialRepository.save(material);
		if (newMaterial == null) {
			LOGGER.debug("Failed create new material: " + JsonBuilder.build(material));
		}
		 LOGGER.debug("Created new material: " + JsonBuilder.build(material));
		return materialRepository.save(material);
	}
	
	@Override
	public Material createUsed(Material material, Long taskId) {
		List<Material> list = usedMaterialDB.get(taskId);
		if (list == null) {
			list = new ArrayList<>();
			usedMaterialDB.put(taskId, list);
		}
		list.add(material);
		material.setTime_used(TimeStamp.get());
		material.setTask(taskId);
		LOGGER.debug("Created new used material: " + JsonBuilder.build(material));
		return material;
	}

	@Override
	public Material update(Material material, Long id) {
		Material materialUpdate = findOne(id);
		
		if (materialUpdate == null) {
			LOGGER.info("Failed update, material with id=" + id + " not exists");
			return null;
		}
		if (material.getName() != null && !material.getName().isEmpty()) {
			materialUpdate.setName(material.getName());
			LOGGER.info("Material with id=" + id + " updated name= " + material.getName());
		}
		if (material.getPrice() != null) {
			materialUpdate.setPrice(material.getPrice());
			LOGGER.info("Material with id=" + id + " updated price= " + material.getPrice());
		}
		if (material.getTask() != null) {
			materialUpdate.setTask(material.getTask());
			LOGGER.info("Material with id=" + id + " updated task= " + material.getTask());
		}
		if (material.getUnit() != null && !material.getUnit().isEmpty()) {
			materialUpdate.setUnit(material.getUnit());
			LOGGER.info("Material with id=" + id + " updated unit= " + material.getUnit());
		}
		if (material.getQuantity() != null) {
			materialUpdate.setQuantity(material.getQuantity());
			LOGGER.info("Material with id=" + id + " updated quantity= " + material.getQuantity());
		}
		if (material.getLocation() != null && !material.getLocation().isEmpty()) {
			materialUpdate.setLocation(material.getLocation());
			LOGGER.info("Material with id=" + id + " updated location= " + material.getLocation());
		}
		if (material.getLocation() != null && !material.getLocation().isEmpty()) {
			materialUpdate.setLocation(material.getLocation());
			LOGGER.info("Material with id=" + id + " updated location= " + material.getLocation());
		}
		
		LOGGER.info("Material with id=" + id + " updated");
		
		return materialRepository.save(materialUpdate);
	}
	
	@Override
	public void deleteUsed(Material material, Long taskID) {
		List<Material> list = usedMaterialDB.get(taskID);
		if (list != null) {
			for (int i = 0; i < list.size(); i++) {
				if (list.get(i).getId() == material.getId()) {
					list.remove(i);
				}
			}
		}	
	}

	@Override
	public void delete(Material material) {
		materialRepository.delete(material);
		LOGGER.info("Deleted material with id=" + material.getId());
	}
}
