package org.eTasker.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.eTasker.model.Responsibleperson;
import org.eTasker.repository.ResponsiblePersonRepository;
import org.eTasker.tool.JsonBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ResponsiblePersonImpl implements ResponsiblePersonService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ResponsiblePersonService.class);
	
	@Autowired
	private ResponsiblePersonRepository responsiblePersonRepository;

	@Override
	public List<Responsibleperson> findAll() {
		List<Responsibleperson> responsiblePersons = responsiblePersonRepository.findAll();
		if (responsiblePersons == null) {
			LOGGER.debug("Failed to retrieve all responsible persons");
		}
		LOGGER.info("Responsible persons: " + responsiblePersons);
		return responsiblePersons;
	}
	
	@Override
	public Responsibleperson findOne(Long id) {
		Responsibleperson responsiblePerson = responsiblePersonRepository.findOne(id);
		if (responsiblePerson == null) {
			LOGGER.debug("Not found responsible person with id=" + id);
		}
		LOGGER.info("Found respons:" + JsonBuilder.build(responsiblePerson));
		return responsiblePerson;
	}

	@Override
	public Responsibleperson create(Responsibleperson responsiblePerson) {
		responsiblePerson.setCreated(new SimpleDateFormat("dd.MM.yyyy:HH.mm.ss").format(new Date()));
		Responsibleperson newResponsiblePerson = responsiblePersonRepository.save(responsiblePerson);
		if (newResponsiblePerson == null) {
			LOGGER.debug("Failed create new responsible person: " + JsonBuilder.build(responsiblePerson));
		}
		LOGGER.debug("Created new responsible person: " + JsonBuilder.build(newResponsiblePerson));
		return newResponsiblePerson;
	}

	@Override
	public Responsibleperson update(Responsibleperson responsiblePerson, Long id) {
		Responsibleperson responsiblePersonUpdate = findOne(id);
		if (responsiblePersonUpdate == null) {
			LOGGER.info("Failed update, responsible person with id=" + id + " not exists");
			return null;
		}
		if (responsiblePerson.getClient() != null ) {
			responsiblePersonUpdate.setClient(responsiblePerson.getClient());
			LOGGER.info("Responsible person with id=" + id + " updated client= " + responsiblePerson.getClient());
		}
		if (responsiblePerson.getEmail() != null && !responsiblePerson.getEmail().isEmpty()) {
			responsiblePersonUpdate.setEmail(responsiblePerson.getEmail());
			LOGGER.info("Responsible person with id=" + id + " updated email= " + responsiblePerson.getClient());
		}
		if (responsiblePerson.getFirst_name() != null && !responsiblePerson.getFirst_name().isEmpty()) {
			responsiblePersonUpdate.setFirst_name(responsiblePerson.getFirst_name());
			LOGGER.info("Responsible person with id=" + id + " updated first name= " + responsiblePerson.getFirst_name());
		}
		if (responsiblePerson.getPhone() != null && !responsiblePerson.getPhone().isEmpty()) {
			responsiblePersonUpdate.setPhone(responsiblePerson.getPhone());
			LOGGER.info("Responsible person with id=" + id + " updated phone= " + responsiblePerson.getPhone());
		}
		if (responsiblePerson.getLast_name() != null && !responsiblePerson.getLast_name().isEmpty()) {
			responsiblePersonUpdate.setLast_name(responsiblePerson.getLast_name());
			LOGGER.info("Responsible person with id=" + id + " updated second name= " + responsiblePerson.getLast_name());
		}
		responsiblePersonUpdate.setUpdated(new SimpleDateFormat("dd.MM.yyyy:HH.mm.ss").format(new Date()));
		LOGGER.info("Responsible person with id=" + id + " updated");
		return responsiblePersonRepository.save(responsiblePersonUpdate);
	}

	@Override
	public void delete(Responsibleperson responsiblePerson) {
		responsiblePersonRepository.delete(responsiblePerson);
		LOGGER.info("Deleted responsible person with id=" + responsiblePerson.getId());
	}
}
