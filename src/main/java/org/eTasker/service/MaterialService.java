package org.eTasker.service;

import java.util.List;

import org.eTasker.model.Material;

public interface MaterialService {

	List<Material> findAll();

    Material findOne(Long id);

    Material create(Material material);

    Material update(Material material, Long id);

    void delete(Material material);
}
