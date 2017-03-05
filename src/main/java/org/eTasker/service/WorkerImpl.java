package org.eTasker.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.eTasker.model.Worker;
import org.eTasker.repository.WorkerRepository;
import org.eTasker.tool.JsonBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WorkerImpl implements WorkerService {
	private static final Logger LOGGER = LoggerFactory.getLogger(WorkerService.class);
	
	@Autowired
	private WorkerRepository workerRepository;

	@Override
	public List<Worker> findAll() {
		List<Worker> workers = workerRepository.findAll();
		if (workers == null) {
			LOGGER.debug("Failed to retrieve all workers");
		}
		LOGGER.info("Workers: " + JsonBuilder.build(workers));
		return workers;
	}
	
	@Override
	public Worker findOne(Long id) {
		Worker worker = workerRepository.findOne(id);
		if (worker == null) {
			LOGGER.debug("Not found worker with id=" + id);
		}
		LOGGER.info("Found worker with id=" + id);
		return worker;
	}

	@Override
	public Worker create(Worker worker) {
		worker.setCreated(new SimpleDateFormat("dd.MM.yyyy:HH.mm.ss").format(new Date()));
		Worker newWorker = workerRepository.save(worker);
		if (newWorker == null) {
			LOGGER.debug("Failed create new uworker: " + JsonBuilder.build(worker));
		}
		LOGGER.debug("Created new worker: " + JsonBuilder.build(newWorker));
		return newWorker;
	}

	@Override
	public Worker update(Worker worker, Long id) {
		Worker workerUpdate = findOne(id);
		if (workerUpdate == null) {
			LOGGER.info("Worker with id=" + id + " not exists");
			return null;
		}
		if (worker.getName() != null && !worker.getName().isEmpty()) {
			workerUpdate.setName(worker.getName());
			LOGGER.info("Worker with id=" + id + " updated name= " + worker.getName());
		}
		if (worker.getEmail() != null && !worker.getEmail().isEmpty()) {
			workerUpdate.setEmail(worker.getEmail());
			LOGGER.info("Worker with id=" + id + " updated email= " + worker.getEmail());
		}
		if (worker.getPassword() != null && !worker.getPassword().isEmpty()) {
			workerUpdate.setPassword(worker.getPassword());
			LOGGER.info("Worker with id=" + id + " updated password= " + worker.getPassword());
		}
		if (worker.getLat() != null) {
			workerUpdate.setLat(worker.getLat());
			LOGGER.info("Worker with id=" + id + " updated lat= " + worker.getLat());
		}
		if (worker.getLng() != null) {
			workerUpdate.setLng(worker.getLng());
			LOGGER.info("Worker with id=" + id + " updated lng= " + worker.getLng());
		}
		if (worker.getIsactive() != null) {
			workerUpdate.setIsactive(worker.getIsactive());
			LOGGER.info("Worker with id=" + id + " updated isActive= " + worker.getIsactive());
		}
		workerUpdate.setUpdated(new SimpleDateFormat("dd.MM.yyyy:HH.mm.ss").format(new Date()));
		LOGGER.info("Worker with id=" + id + " updated");
		return workerRepository.save(workerUpdate);
	}

	@Override
	public void delete(Worker worker) {
		workerRepository.delete(worker);
		LOGGER.info("Deleted worker with id=" + worker.getId());
	}

	@Override
	public Worker findByEmail(String email) {
		Worker worker = workerRepository.findByEmail(email);
		if (worker == null) {
			LOGGER.debug("Not found worker=" + email);
		}
		LOGGER.debug("Found worker=" + email);
		return worker;
	}
}
