package org.eTasker.service;

import java.util.List;

import org.eTasker.model.Worker;

public interface WorkerService {
	
	List<Worker> findAll();

    Worker findOne(Long id);

    Worker create(Worker user);

    Worker update(Worker user, String email);

    void delete(Worker user);
    
    Worker findByEmail(String email);
}
