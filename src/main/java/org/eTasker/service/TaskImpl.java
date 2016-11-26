package org.eTasker.service;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.eTasker.model.Task;
import org.eTasker.repository.TaskRepository;
import org.eTasker.tool.JsonBuilder;
import org.eTasker.tool.TimeStamp;
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
		task.setCreated(TimeStamp.get());
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
		if (task.getFile_exists() != null) {
			taskUpdate.setFile_exists(task.getFile_exists());
			LOGGER.info("Task with id=" + id + " updated file exists= " + task.getFile_exists());
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
		if (task.getMaterial_price() != null) {
			Integer price = task.getMaterial_price();
			taskUpdate.setMaterial_price(price);
			LOGGER.info("Task with id=" + id + " updated material price= " + price);
			taskUpdate.setFinal_price(taskUpdate.getWork_price() + price);
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
		if (task.getWork_price() != null) {
			int price = task.getWork_price();
			taskUpdate.setWork_price(price);
			LOGGER.info("Task with id=" + id + " updated work price= " + price);
			taskUpdate.setFinal_price(taskUpdate.getMaterial_price() + price);
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
			Integer status = task.getStatus();
			taskUpdate.setStatus(status);
			LOGGER.info("Task with id=" + id + " updated status= " + status);
			String time = TimeStamp.get();
			String[] current = time.split("\\.");
			if (status == 1) {
				taskUpdate.setStart_time(time);
				taskUpdate.setFetched(true);
				String[] planned = taskUpdate.getPlanned_time().split("\\.");
				taskUpdate.setStart_on_time(true);
				for (int i = 0; i < planned.length - 1; i++) {
					if (Integer.parseInt(current[i]) > Integer.parseInt(planned[i])) {
						taskUpdate.setStart_on_time(false);
						LOGGER.info("Task with id=" + id + " updated started on time= " + false);
						break;
					}
				}
			} else if (status == 3) {
				taskUpdate.setEnd_time(TimeStamp.get());
				String[] start = taskUpdate.getStart_time().split("\\.");
				String[] end = taskUpdate.getEnd_time().split("\\.");
				int startDay = Integer.parseInt(start[0]);
				int endDay = Integer.parseInt(end[0]);
				int startMonth = Integer.parseInt(start[1]);
				int endMonth = Integer.parseInt(end[1]);
				int startYear = Integer.parseInt(start[2]);
				int endYear= Integer.parseInt(end[2]);
				int startHour = Integer.parseInt(start[3]);
				int endHour = Integer.parseInt(end[3]);
				int startMin = Integer.parseInt(start[4]);
				int endMin = Integer.parseInt(end[4]);
				Date startDate = new GregorianCalendar(startYear, startMonth, startDay, startHour, startMin).
						getTime();
				Date endDate = new GregorianCalendar(endYear, endMonth, endDay, endHour, endMin).getTime();
				long diff = (endDate.getTime() - startDate.getTime()) / 1000 / 60;
				taskUpdate.setDuration(diff);
				LOGGER.info("Task with id=" + id + " updated task duration= " + diff);
			}
		}
		if (task.getTitle() != null && !task.getTitle().isEmpty()) {
			taskUpdate.setTitle(task.getTitle());
			LOGGER.info("Task with id=" + id + " updated title= " + task.getTitle());
		}
		if (task.getWorker() != null) {
			taskUpdate.setWorker(task.getWorker());
			LOGGER.info("Task with id=" + id + " updated worker= " + task.getWorker());
		}
		if (task.getTask_type() != null) {
			taskUpdate.setTask_type(task.getTask_type());
			LOGGER.info("Task with id=" + id + " updated task type= " + task.getTask_type());
		}
		taskUpdate.setUpdated(TimeStamp.get());
		LOGGER.info("Task with id=" + id + " updated");
		return taskRepository.save(taskUpdate);
	}

	@Override
	public void delete(Task task) {
		taskRepository.delete(task);
		LOGGER.info("Deleted task with id=" + task.getId());
	}
}

