package org.eTasker.service;

import java.util.List;

import org.eTasker.model.Object;


public interface ObjectService {
	
	List<Object> findAll();

    Object findOne(Long id);

    Object create(Object object);

    Object update(Object object, Long id);

    void delete(Object object);
}
