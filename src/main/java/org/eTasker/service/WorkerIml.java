package org.eTasker.service;

import java.util.List;

import org.eTasker.model.Worker;
import org.eTasker.repository.WorkerRepository;
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
		return workerRepository.findAll();
	}
	
	@Override
	public Worker findOne(Long id) {
		return workerRepository.findOne(id);
	}

	@Override
	public Worker create(Worker worker) {
		return workerRepository.save(worker);
	}

	@Override
	public Worker update(Worker worker, String email) {
		Worker workerUpdate = findByEmail(email);
		if (workerUpdate == null) {
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
		return workerRepository.save(workerUpdate);
	}

	@Override
	public void delete(Worker worker) {
		workerRepository.delete(worker);
	}

	@Override
	public Worker findByEmail(String email) {
		return workerRepository.findByEmail(email);
	}

	@Override
	public Worker changePassword(String email, String currentPassword, String newPassword) {
		Worker worker = findByEmail(email);
		if (worker == null) {
			LOGGER.debug("Failed change password for worker:" + email + ", worker not exists");
			return null;
		}
		if (!worker.getPassword().equals(currentPassword)) {
			LOGGER.debug("Failed change password for worker:" + email + ", given currentpassword:" + currentPassword + 
					" doesn't match with existing password=" + worker.getPassword());
			return null; 
		}
		worker.setPassword(newPassword);
		return workerRepository.save(worker);
	}
}
