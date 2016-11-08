package org.eTasker.service;

import java.util.List;

import org.eTasker.model.Worker;
import org.eTasker.repository.WorkerRepository;
import org.eTasker.tool.JsonBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WorkerIml implements WorkerService {
	private static final Logger LOGGER = LoggerFactory.getLogger(WorkerService.class);
	
	@Autowired
	private WorkerRepository workerRepository;

	@Override
	public List<Worker> findAll() {
		List<Worker> workers = workerRepository.findAll();
		if (workers == null) {
			LOGGER.debug("Failed to retrieve workers");
		}
		LOGGER.info("Workers: " + workers);
		return workers;
	}
	
	@Override
	public Worker findOne(Long id) {
		return workerRepository.findOne(id);
	}

	@Override
	public Worker create(Worker worker) {
		Worker newWorker = workerRepository.save(worker);
		if (newWorker == null) {
			LOGGER.debug("Failed create new uworker: " + JsonBuilder.build(worker));
		}
		LOGGER.debug("Created new worker: " + JsonBuilder.build(newWorker));
		return newWorker;
	}

	@Override
	public Worker update(Worker worker, String email) {
		Worker workerUpdate = findByEmail(email);
		if (workerUpdate == null) {
			LOGGER.info("Worker with email=" + email + " not exists");
			return null;
		}
		if (worker.getName() != null && !worker.getName().isEmpty()) {
			workerUpdate.setName(worker.getName());
			LOGGER.info("Worker with email=" + email + " updated name: " + worker.getName());
		}
		if (worker.getEmail() != null && !worker.getEmail().isEmpty()) {
			workerUpdate.setEmail(worker.getEmail());
			LOGGER.info("Worker with email=" + email + " updated email: " + worker.getEmail());
		}
		if (worker.getPassword() != null && !worker.getPassword().isEmpty()) {
			workerUpdate.setPassword(worker.getPassword());
			LOGGER.info("Worker with email=" + email + " updated password: " + worker.getPassword());
		}
		if (worker.getCompanyname() != null && !worker.getCompanyname().isEmpty()) {
			workerUpdate.setCompanyname(worker.getCompanyname());
			LOGGER.info("Worker with email=" + email + " updated companyName: " + worker.getPassword());
		}
		LOGGER.info("Worker with email: " + email + " updated");
		return workerRepository.save(workerUpdate);
	}

	@Override
	public void delete(Worker worker) {
		workerRepository.delete(worker);
		LOGGER.info("Deleted worker: " + worker.getEmail());
	}

	@Override
	public Worker findByEmail(String email) {
		Worker worker = workerRepository.findByEmail(email);
		if (worker == null) {
			LOGGER.debug("Not found worker:" + email);
		}
		LOGGER.debug("Found worker:" + email);
		return worker;
	}
}
