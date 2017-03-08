package org.eTasker.service;

import java.util.List;

import org.eTasker.model.Task_type;

public interface TaskTypeService {
	
	List<Task_type> findAll();

	Task_type findOne(Long id);

	Task_type create(Task_type taskType);

	Task_type update(Task_type taskType, Long id);

    void delete(Task_type taskType);
}
