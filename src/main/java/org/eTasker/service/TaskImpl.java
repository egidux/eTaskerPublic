package org.eTasker.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.eTasker.model.Task;
import org.eTasker.repository.TaskRepository;
import org.eTasker.tool.JsonBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TaskImpl implements TaskService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(TaskService.class);
	
	@Autowired
	private TaskRepository taskRepository;

	@Override
	public List<Task> findAll() {
		List<Task> tasks = taskRepository.findAll();
		if (tasks == null) {
			LOGGER.debug("Failed to retrieve all tasks");
		}
		LOGGER.info("Tasks: " + tasks);
		return tasks;
	}
	
	@Override
	public Task findOne(Long id) {
		Task task = taskRepository.findOne(id);
		if (task == null) {
			LOGGER.debug("Not found task with id=" + id);
		}
		LOGGER.info("Found task with id=" + id);
		return task;
	}

	@Override
	public Task create(Task task) {
		task.setStatus(Task.Status.CREATED.toString());
		task.setCreated(new SimpleDateFormat("dd.MM.yyyy:HH.mm.ss").format(new Date()));
		Task newTask = taskRepository.save(task);
		if (newTask == null) {
			LOGGER.debug("Failed create new task: " + JsonBuilder.build(task));
		}
		LOGGER.debug("Created new task: " + JsonBuilder.build(newTask));
		return newTask;
	}

	@Override
	public Task update(Task task, Long id) {
		Task taskUpdate = findOne(id);
		if (taskUpdate == null) {
			LOGGER.info("Task with id=" + id + " not exists");
			return null;
		}
		if (task.getClient() != null) {
			taskUpdate.setClient(task.getClient());
			LOGGER.info("Task with id=" + id + " updated client= " + task.getClient());
		}
		if (task.getDescription() != null && !task.getDescription().isEmpty()) {
			taskUpdate.setDescription(task.getDescription());
			LOGGER.info("Task with id=" + id + " updated description= " + task.getDescription());
		}
		if (task.getObject() != null) {
			taskUpdate.setObject(task.getObject());
			LOGGER.info("Task with id=" + id + " updated object= " + task.getObject());
		}
		if (task.getStatus() != null) {
			taskUpdate.setStatus(task.getStatus());
			LOGGER.info("Task with id=" + id + " updated status= " + task.getStatus());
		}
		if (task.getTitle() != null && !task.getTitle().isEmpty()) {
			taskUpdate.setTitle(task.getTitle());
			LOGGER.info("Task with id=" + id + " updated title= " + task.getTitle());
		}
		if (task.getUpdated() != null && !task.getUpdated().isEmpty()) {
			taskUpdate.setUpdated(task.getUpdated());
			LOGGER.info("Task with id=" + id + " updated updated= " + task.getUpdated());
		}
		if (task.getWorker() != null) {
			taskUpdate.setWorker(task.getWorker());
			LOGGER.info("Task with id=" + id + " updated worker= " + task.getWorker());
		}
		taskUpdate.setUpdated(new SimpleDateFormat("dd.MM.yyyy:HH.mm.ss").format(new Date()));
		LOGGER.info("Task with id=" + id + " updated");
		return taskRepository.save(taskUpdate);
	}

	@Override
	public void delete(Task task) {
		taskRepository.delete(task);
		LOGGER.info("Deleted task with id=" + task.getId());
	}
}

