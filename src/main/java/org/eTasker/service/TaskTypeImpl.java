package org.eTasker.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.eTasker.model.Task_type;
import org.eTasker.repository.TaskTypeRepository;
import org.eTasker.tool.JsonBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TaskTypeImpl implements TaskTypeService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(TaskTypeService.class);
	
	@Autowired
	private TaskTypeRepository taskTypeRepository;

	@Override
	public List<Task_type> findAll() {
		List<Task_type> taskTypes = taskTypeRepository.findAll();
		if (taskTypes == null) {
			LOGGER.debug("Failed to retrieve all task types");
		}
		LOGGER.info("Task types: " + taskTypes);
		return taskTypes;
	}
	
	@Override
	public Task_type findOne(Long id) {
		Task_type taskType = taskTypeRepository.findOne(id);
		if (taskType == null) {
			LOGGER.debug("Not found task type with id=" + id);
		}
		LOGGER.info("Found task type:" + JsonBuilder.build(taskType));
		return taskType;
	}

	@Override
	public Task_type create(Task_type taskType) {
		taskType.setCreated(new SimpleDateFormat("dd.MM.yyyy:HH.mm.ss").format(new Date()));
		Task_type newTaskType = taskTypeRepository.save(taskType);
		if (newTaskType == null) {
			LOGGER.debug("Failed create new task type: " + JsonBuilder.build(taskType));
		}
		LOGGER.debug("Created new task type: " + JsonBuilder.build(newTaskType));
		return newTaskType;
	}

	@Override
	public Task_type update(Task_type taskType, Long id) {
		Task_type taskTypeUpdate = findOne(id);
		if (taskTypeUpdate == null) {
			LOGGER.info("Failed update, task type with id=" + id + " not exists");
			return null;
		}
		if (taskType.getSignature() != null) {
			taskTypeUpdate.setSignature(taskType.getSignature());
			LOGGER.info("Task type with id=" + id + " updated signature= " + taskType.getSignature());
		}
		if (taskType.getMail_list() != null && !taskType.getMail_list().isEmpty()) {
			taskTypeUpdate.setMail_list(taskType.getMail_list());
			LOGGER.info("Task type with id=" + id + " updated mail list= " + taskType.getMail_list());
		}
		if (taskType.getTitle() != null && !taskType.getTitle().isEmpty()) {
			taskTypeUpdate.setTitle(taskType.getTitle());
			LOGGER.info("Task type with id=" + id + " updated title= " + taskType.getTitle());
		}
		taskTypeUpdate.setUpdated(new SimpleDateFormat("dd.MM.yyyy:HH.mm.ss").format(new Date()));
		LOGGER.info("Task type with id=" + id + " updated");
		return taskTypeRepository.save(taskTypeUpdate);
	}

	@Override
	public void delete(Task_type taskType) {
		taskTypeRepository.delete(taskType);
		LOGGER.info("Deleted task type with id=" + taskType.getId());
	}
}