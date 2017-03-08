package org.eTasker.service;

import java.util.List;

import org.eTasker.model.Responsible_person;

public interface ResponsiblePersonService {

	List<Responsible_person> findAll();

	Responsible_person findOne(Long id);

	Responsible_person create(Responsible_person material);

	Responsible_person update(Responsible_person material, Long id);

    void delete(Responsible_person material);
}
