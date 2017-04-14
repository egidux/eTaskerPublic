package org.eTasker.service;

import java.util.List;

import org.eTasker.model.Material;

public interface MaterialService {

	List<Material> findAll();
	List<Material> findAllUsed();
	List<Material> findAllUsed(Long taskId);

    Material findOne(Long id);

    Material create(Material material);
    Material createUsed(Material material, Long taskId);

    Material update(Material material, Long id);

    void delete(Material material);
    void deleteUsed(Material material, Long taskID);
}
