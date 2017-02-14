package org.eTasker.service;

import java.util.List;

import org.eTasker.model.Object;
import org.eTasker.repository.ObjectRepository;
import org.eTasker.tool.JsonBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ObjectImpl implements ObjectService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ObjectService.class);
	
	@Autowired
	private ObjectRepository objectRepository;

	@Override
	public List<Object> findAll() {
		List<Object> objects = objectRepository.findAll();
		if (objects == null) {
			LOGGER.debug("Failed to retrieve all objects");
		}
		LOGGER.info("Objects: " + JsonBuilder.build(objects));
		return objects;
	}
	
	@Override
	public Object findOne(Long id) {
		Object object = objectRepository.findOne(id);
		if (object == null) {
			LOGGER.debug("Not found object with id=" + id);
		}
		LOGGER.info("Found object with id=" + id);
		return object;
	}

	@Override
	public Object create(Object object) {
		Object newObject = objectRepository.save(object);
		if (newObject == null) {
			LOGGER.debug("Failed create new object: " + JsonBuilder.build(object));
		}
		LOGGER.debug("Created new object: " + JsonBuilder.build(newObject));
		return newObject;
	}

	@Override
	public Object update(Object object, Long id) {
		Object objectUpdate = findOne(id);
		if (objectUpdate == null) {
			LOGGER.info("Object with id=" + id + " not exists");
			return null;
		}
		if (object.getAddress() != null && !object.getAddress().isEmpty()) {
			objectUpdate.setAddress(object.getAddress());
			LOGGER.info("Object with id=" + id + " updated address= " + object.getAddress());
		}
		if (object.getClient() != null) {
			objectUpdate.setClient(object.getClient());
			LOGGER.info("Object with id=" + id + " updated client= " + object.getClient());
		}
		if (object.getName() != null && !object.getName().isEmpty()) {
			objectUpdate.setName(object.getName());
			LOGGER.info("Object with id=" + id + " updated name= " + object.getName());
		}
		LOGGER.info("Object with id=" + id + " updated");
		return objectRepository.save(objectUpdate);
	}

	@Override
	public void delete(Object object) {
		objectRepository.delete(object);
		LOGGER.info("Deleted object with id=" + object.getId());
	}
}
