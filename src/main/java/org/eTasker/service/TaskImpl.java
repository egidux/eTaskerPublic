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
		task.setStatus(0);
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
		if (task.getAgreement() != null) {
			taskUpdate.setAgreement(task.getAgreement());
			LOGGER.info("Task with id=" + id + " updated agreement= " + task.getAgreement());
		}
		if (task.getBill_status() != null) {
			taskUpdate.setBill_status(task.getBill_status());
			LOGGER.info("Task with id=" + id + " updated bill status= " + task.getBill_status());
		}
		if (task.getFetched() != null) {
			taskUpdate.setFetched(task.getFetched());
			LOGGER.info("Task with id=" + id + " updated fetched= " + task.getFetched());
		}
		if (task.getFile_exists() != null) {
			taskUpdate.setFile_exists(task.getFile_exists());
			LOGGER.info("Task with id=" + id + " updated file exists= " + task.getFile_exists());
		}
		if (task.getStart_on_time() != null) {
			taskUpdate.setStart_on_time(task.getStart_on_time());
			LOGGER.info("Task with id=" + id + " updated start on time= " + task.getStart_time());
		}
		if (task.getAbort_message() != null && !task.getAbort_message().isEmpty()) {
			taskUpdate.setAbort_message(task.getAbort_message());
			LOGGER.info("Task with id=" + id + " updated abort message= " + task.getAbort_message());
		}
		if (task.getBill_date() != null && !task.getBill_date().isEmpty()) {
			taskUpdate.setBill_date(task.getBill_date());
			LOGGER.info("Task with id=" + id + " updated bill date= " + task.getBill_date());
		}
		if (task.getClient_note() != null && !task.getClient_note().isEmpty()) {
			taskUpdate.setClient_note(task.getClient_note());
			LOGGER.info("Task with id=" + id + " updated client note= " + task.getClient_note());
		}
		if (task.getClient_note_reply() != null && !task.getClient_note_reply().isEmpty()) {
			taskUpdate.setClient_note_reply(task.getClient_note_reply());
			LOGGER.info("Task with id=" + id + " updated client note reply= " + task.getClient_note());
		}
		if (task.getDistance() != null) {
			taskUpdate.setDistance(task.getDistance());
			LOGGER.info("Task with id=" + id + " updated distance= " + task.getDistance());
		}
		if (task.getDuration() != null) {
			taskUpdate.setDuration(task.getDuration());
			LOGGER.info("Task with id=" + id + " updated duration= " + task.getDuration());
		}
		if (task.getEnd_time() != null && !task.getEnd_time().isEmpty()) {
			taskUpdate.setEnd_time(task.getEnd_time());
			LOGGER.info("Task with id=" + id + " updated end time= " + task.getEnd_time());
		}
		if (task.getFinal_price() != null) {
			taskUpdate.setFinal_price(task.getFinal_price());
			LOGGER.info("Task with id=" + id + " updated final price= " + task.getFinal_price());
		}
		if (task.getMaterial_price() != null) {
			taskUpdate.setMaterial_price(task.getMaterial_price());
			LOGGER.info("Task with id=" + id + " updated material price= " + task.getMaterial_price());
		}
		if (task.getPlanned_end_time() != null && !task.getPlanned_end_time().isEmpty()) {
			taskUpdate.setPlanned_end_time(task.getPlanned_end_time());
			LOGGER.info("Task with id=" + id + " updated planned end time= " + task.getPlanned_end_time());
		}
		if (task.getPlanned_time() != null && !task.getPlanned_time().isEmpty()) {
			taskUpdate.setPlanned_time(task.getPlanned_time());
			LOGGER.info("Task with id=" + id + " updated planned time= " + task.getPlanned_time());
		}
		if (task.getRating() != null) {
			taskUpdate.setRating(task.getRating());
			LOGGER.info("Task with id=" + id + " updated rating= " + task.getRating());
		}
		if (task.getSignature_type() != null) {
			taskUpdate.setSignature_type(task.getSignature_type());
			LOGGER.info("Task with id=" + id + " updated signature type= " + task.getSignature_type());
		}
		if (task.getStart_time() != null && !task.getStart_time().isEmpty()) {
			taskUpdate.setStart_time(task.getStart_time());
			LOGGER.info("Task with id=" + id + " updated start time= " + task.getStart_time());
		}
		if (task.getWork_price() != null) {
			taskUpdate.setWork_price(task.getWork_price());
			LOGGER.info("Task with id=" + id + " updated work price= " + task.getWork_price());
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

