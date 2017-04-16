package org.eTasker.service;

import java.util.List;

import org.eTasker.model.Worker;

public interface WorkerService {
	
	List<Worker> findAll();

    Worker findOne(Long id);

    Worker create(Worker worker);

    Worker update(Worker worker, Long id);

    void delete(Worker worker);
    
    Worker findByEmail(String email);
    
    Worker findByName(String name);
}
