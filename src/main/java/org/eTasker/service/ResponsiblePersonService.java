package org.eTasker.service;

import java.util.List;

import org.eTasker.model.Responsibleperson;

public interface ResponsiblePersonService {

	List<Responsibleperson> findAll();

	Responsibleperson findOne(Long id);

	Responsibleperson create(Responsibleperson material);

	Responsibleperson update(Responsibleperson material, Long id);

    void delete(Responsibleperson material);
}
