package org.eTasker.service;

import java.util.List;

import org.eTasker.model.Task;

public interface TaskService {
	List<Task> findAll();

    Task findOne(Long id);

    Task create(Task task);

    Task update(Task task, Long id);

    void delete(Task task);
}
