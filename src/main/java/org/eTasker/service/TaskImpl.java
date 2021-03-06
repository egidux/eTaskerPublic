package org.eTasker.service;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.List;

import org.eTasker.controller.EmailController;
import org.eTasker.model.Client;
import org.eTasker.model.Material;
import org.eTasker.model.Report;
import org.eTasker.model.Task;
import org.eTasker.repository.TaskRepository;
import org.eTasker.tool.JsonBuilder;
import org.eTasker.tool.TimeStamp;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TaskImpl implements TaskService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(TaskService.class);
	private static final String FCM_SERVER_URL = "https://fcm.googleapis.com/fcm/send";
	private static final String FCM_SERVER_KEY = "AAAAa2D4uBM:APA91bFmzURcDQEK8pu1WB"
			+ "vw8lDUimMAkQZ0PYNVCjrTDU7m97E5A4sgZhg81tqIZnVGjZkveYMocqnyFQJ2QhV3S_Jq9tFDswWajkfiwiUDkb-yMc0_u_disk"
			+ "_7JQbxv2E1MK3CtJXw";
	
	@Autowired
	private TaskRepository taskRepository;
	@Autowired
	private ClientService clientService;
	@Autowired
	private ReportService reportService;
	@Autowired
	private ObjectService objectService;
	@Autowired
	private MaterialService materialService;
	@Autowired
	private EmailController emailController;
	@Autowired
	private TokenService tokenService;
	@Autowired
	private WorkerService workerService;

	@Override
	public List<Task> findAll() {
		List<Task> tasks = taskRepository.findAll();
		if (tasks == null) {
			LOGGER.debug("Failed to retrieve all tasks");
		}
		LOGGER.info("Tasks: " + JsonBuilder.build(tasks));
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
		if (task.getWorker() == null || task.getWorker().isEmpty()) {
			task.setStatus(0);
		} else {
			task.setStatus(1);
		}
		
		task.setCreated(TimeStamp.get());
		Task newTask = taskRepository.save(task);
		if (newTask == null) {
			LOGGER.debug("Failed create new task: " + JsonBuilder.build(task));
		}
		if (task.getWorker() != null) {
			sendPushNotification(newTask);
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
			Boolean status = task.getBill_status();
			taskUpdate.setBill_status(status);
			LOGGER.info("Task with id=" + id + " updated bill status= " + status);
			if (status) {
				String date = TimeStamp.get();
				taskUpdate.setBill_date(date);
				LOGGER.info("Task with id=" + id + " updated bill date= " + date);
			}
		}
		if (task.getAgreed() != null) {
			taskUpdate.setAgreed(task.getAgreed());
			LOGGER.info("Task with id=" + id + " updated agreed= " + task.getAgreed());
		}
		if (task.getSigned_by() != null && !task.getSigned_by().isEmpty()) {
			taskUpdate.setSigned_by(task.getSigned_by());
			LOGGER.info("Task with id=" + id + " updated signed by= " + task.getSigned_by());
		}
		if (task.getRating() != null) {
			taskUpdate.setRating(task.getRating());
			LOGGER.info("Task with id=" + id + " updated rating= " + task.getRating());
		}
		if (task.getSignature_exists()!= null) {
			taskUpdate.setSignature_exists(task.getSignature_exists());
			LOGGER.info("Task with id=" + id + " updated signature exists= " + task.getSignature_exists());
		}
		if (task.getFile_exists() != null) {
			taskUpdate.setFile_exists(task.getFile_exists());
			LOGGER.info("Task with id=" + id + " updated file exists= " + task.getFile_exists());
		}
		if (task.getAbort_message() != null && !task.getAbort_message().isEmpty()) {
			taskUpdate.setAbort_message(task.getAbort_message());
			LOGGER.info("Task with id=" + id + " updated abort message= " + task.getAbort_message());
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
			LOGGER.info("Task with id=" + id + " updated final price= " + taskUpdate.getFinal_price());
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
			LOGGER.info("Task with id=" + id + " updated final price= " + taskUpdate.getFinal_price());
		}
		if (task.getClient() != null) {
			taskUpdate.setClient(task.getClient());
			LOGGER.info("Task with id=" + id + " updated client= " + task.getClient());
		}
		if (task.getDescription() != null && !task.getDescription().isEmpty()) {
			taskUpdate.setDescription(task.getDescription());
			LOGGER.info("Task with id=" + id + " updated description= " + task.getDescription());
		}
		if (task.getTotal_time() != null && task.getTotal_time() != -1 ) {
			taskUpdate.setTotal_time(task.getTotal_time());
			LOGGER.info("Task with id=" + id + " updated totalTime= " + task.getTotal_time());
		}
		if (task.getTotal_time_start() != null) {
			taskUpdate.setTotal_time_start(task.getTotal_time_start());
			LOGGER.info("Task with id=" + id + " updated totalTimeStart= " + task.getTotal_time());
		}
		if (task.getObject() != null) {
			taskUpdate.setObject(task.getObject());
			LOGGER.info("Task with id=" + id + " updated object= " + task.getObject());
		}
		if (task.getStart_time() != null) {
			taskUpdate.setStart_time(task.getStart_time());
			LOGGER.info("Task with id=" + id + " updated startTime= " + task.getStart_time());
			taskUpdate.setTotal_time_start(System.currentTimeMillis());
			taskUpdate.setTotal_time(0L);
		}
		if (task.getStatus() != null) {
			Integer status = task.getStatus();
			taskUpdate.setStatus(status);
			LOGGER.info("Task with id=" + id + " updated status= " + status);
			String time = TimeStamp.get();
			String[] current = time.split("\\.");
			if (status == 2) {
				//taskUpdate.setStart_time(time);
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
			} else if (status == 3 || status == 4) {
				/*taskUpdate.setEnd_time(TimeStamp.get());
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
				LOGGER.info("Task with id=" + id + " updated task duration= " + diff);*/
			}
			if (status == 3) {
				String taskEndTime = new Date().toString();
				taskUpdate.setEnd_time(new Date().toString());
				Task taskReport = findOne(id);
				Client client = clientService.findByName(taskReport.getClient());
				String clientEmail = client.getEmail();
				Report report = reportService.get();
				String reportHeader = report.getReport_text().replaceAll("%", id.toString());
				String companyName = report.getCompany_name();
				String companyCode = report.getCompany_code();
				String companyAddress = report.getCompany_address();
				String companyPhone = report.getCompany_phone();
				String taskTitle = taskReport.getTitle();
				String taskStartTime = taskReport.getStart_time();
				String taskDescription = taskReport.getDescription();
				org.eTasker.model.Object object  = objectService.findByName(taskReport.getObject());
				String objectAddress = object.getAddress();
				List<Material> materials = materialService.findAllUsed(id);
				
				String reportStringCompany = String.format("\n\n%s\nCompany code: %s\nAddress: %s\nPhone: %s", 
						companyName, companyCode, companyAddress, companyPhone);
				
				String reportStringTask = String.format("\n\nTask address: %s\nTask title: %s"
						+ "\nTask start time: %s\nTask end time: %s\nTask description: %s", 
						objectAddress, taskTitle, taskStartTime, taskEndTime, taskDescription);
				
				StringBuilder materialsString = new StringBuilder("\n\nMaterials Used");
				for (Material m: materials) {
					materialsString.append("\n" + m.getName() + "\nQuantity: " + m.getQuantity() + "\nMeasurement unit: " +
							m.getUnit() + "\nPrice per unit: " + m.getPrice() + " EUR\nTotal price: " + 
							(m.getPrice() * m.getQuantity()) + " EUR\n");
				}
				new Thread(() -> {
					emailController.sendEmail(clientEmail, "Task report", reportHeader + reportStringCompany + reportStringTask + 
							materialsString.toString());
				}).start();
				
				LOGGER.info("Report emailed to " + clientEmail);
			}
		}
		if (task.getTitle() != null && !task.getTitle().isEmpty()) {
			taskUpdate.setTitle(task.getTitle());
			LOGGER.info("Task with id=" + id + " updated title= " + task.getTitle());
		}
		if (task.getWorker() != null) {
			taskUpdate.setWorker(task.getWorker());
			if (task.getWorker().isEmpty()) {
				taskUpdate.setStatus(0);
			} else {
			 taskUpdate.setStatus(1);
			}
			LOGGER.info("Task with id=" + id + " updated worker= " + task.getWorker());
		}
		if (task.getTask_type() != null) {
			taskUpdate.setTask_type(task.getTask_type());
			LOGGER.info("Task with id=" + id + " updated task type= " + task.getTask_type());
		}
		taskUpdate.setUpdated(TimeStamp.get());
		LOGGER.info("Task with id=" + id + " updated");
		Task updatedTask = taskRepository.save(taskUpdate);
		if (task.getWorker() != null) {
			sendPushNotification(updatedTask);
		}
		return updatedTask;
	}

	@Override
	public void delete(Task task) {
		taskRepository.delete(task);
		LOGGER.info("Deleted task with id=" + task.getId());
	}
	
	private void sendPushNotification(Task task) {
		try {
			URL url = new URL(FCM_SERVER_URL);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("POST");
			con.setRequestProperty("Authorization", "key=" + FCM_SERVER_KEY);
			con.setRequestProperty("Content-Type", "application/json");
			String token = tokenService.findOne(workerService.findByName(task.getWorker()).getEmail()).getToken();
			JSONObject jsonTo =  new JSONObject();
			jsonTo.put("to", token);
			JSONObject jsonNotification = new JSONObject();
			jsonNotification.put("title", "Received new task");
			jsonNotification.put("body", task.getTitle());
			jsonNotification.put("sound", "default");
			jsonTo.put("notification", jsonNotification);
			con.setDoOutput(true);
		    OutputStream os = con.getOutputStream();
	            os.write(jsonTo.toString().getBytes("UTF-8"));
	            os.close();
	        int responseCode = con.getResponseCode();
	        LOGGER.info("FCM respond code: " + responseCode);
			
		} catch (Exception e) {
			LOGGER.debug("Notification not sent");
		}	
	}
}

