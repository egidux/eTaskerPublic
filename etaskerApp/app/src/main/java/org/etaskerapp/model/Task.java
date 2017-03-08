package org.etaskerapp.model;


import java.io.Serializable;

public class Task implements Serializable {
    /* Task status
            0	Unassigned
            1   Assigned
            2   In progress
            3	Done
            4	Aborted
            5   Stopped
        */
    private Long id;
    private String title;
    private String description;
    private Integer rating; //Rating by client (1 - bad, 2 - average, 3 - good)
    private Boolean agreement; //true if client agreed
    private Integer signature_type; //(0 - no signature, 1 - signature)
    private String abort_message;
    private String client_note;
    private String client_note_reply;
    private Integer distance; //	Distance in meters
    private Integer work_price = 0; //in cents
    private Integer material_price = 0;
    private Integer final_price; //work price + matterial price
    private Integer status;
    private String planned_time;
    private String planned_end_time;
    private Boolean fetched = false; //true if Task was fetched by worker
    private String start_time;
    private String end_time;
    private Boolean start_on_time;
    private Long duration; //total in minutes
    private Boolean bill_status = false; //true if client was billed
    private String bill_date;
    private Boolean file_exists;
    private String worker;
    private Long task_type;
    private String object;
    private String objectAddress;
    private String client;
    private String created;
    private String updated;

    public void setObjectAddress(String address) {
        this.objectAddress = address;
    }
    public String getObjectAddress() {
        return this.objectAddress;
    }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public Integer getRating() {
        return rating;
    }
    public void setRating(Integer rating) {
        this.rating = rating;
    }
    public Boolean getAgreement() {
        return agreement;
    }
    public void setAgreement(Boolean agreement) {
        this.agreement = agreement;
    }
    public Integer getSignature_type() {
        return signature_type;
    }
    public void setSignature_type(Integer signature_type) {
        this.signature_type = signature_type;
    }
    public String getAbort_message() {
        return abort_message;
    }
    public void setAbort_message(String abort_message) {
        this.abort_message = abort_message;
    }
    public String getClient_note() {
        return client_note;
    }
    public void setClient_note(String client_note) {
        this.client_note = client_note;
    }
    public String getClient_note_reply() {
        return client_note_reply;
    }
    public void setClient_note_reply(String client_note_reply) {
        this.client_note_reply = client_note_reply;
    }
    public Integer getDistance() {
        return distance;
    }
    public void setDistance(Integer distance) {
        this.distance = distance;
    }
    public Integer getWork_price() {
        return work_price;
    }
    public void setWork_price(Integer work_price) {
        this.work_price = work_price;
    }
    public Integer getMaterial_price() {
        return material_price;
    }
    public void setMaterial_price(Integer material_price) {
        this.material_price = material_price;
    }
    public Integer getFinal_price() {
        return final_price;
    }
    public void setFinal_price(Integer final_price) {
        this.final_price = final_price;
    }
    public Integer getStatus() {
        return status;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }
    public String getPlanned_time() {
        return planned_time;
    }
    public void setPlanned_time(String planned_time) {
        this.planned_time = planned_time;
    }
    public String getPlanned_end_time() {
        return planned_end_time;
    }
    public void setPlanned_end_time(String planned_end_time) {
        this.planned_end_time = planned_end_time;
    }
    public Boolean getFetched() {
        return fetched;
    }
    public void setFetched(Boolean fetched) {
        this.fetched = fetched;
    }
    public String getStart_time() {
        return start_time;
    }
    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }
    public String getEnd_time() {
        return end_time;
    }
    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }
    public Boolean getStart_on_time() {
        return start_on_time;
    }
    public void setStart_on_time(Boolean start_on_time) {
        this.start_on_time = start_on_time;
    }
    public Long getDuration() {
        return duration;
    }
    public void setDuration(Long duration) {
        this.duration = duration;
    }
    public Boolean getBill_status() {
        return bill_status;
    }
    public void setBill_status(Boolean bill_status) {
        this.bill_status = bill_status;
    }
    public String getBill_date() {
        return bill_date;
    }
    public void setBill_date(String bill_date) {
        this.bill_date = bill_date;
    }
    public Boolean getFile_exists() {
        return file_exists;
    }
    public void setFile_exists(Boolean file_exists) {
        this.file_exists = file_exists;
    }
    public String getWorker() {
        return worker;
    }
    public void setWorker(String worker) {
        this.worker = worker;
    }
    public Long getTask_type() {
        return task_type;
    }
    public void setTask_type(Long task_type) {
        this.task_type = task_type;
    }
    public String getObject() {
        return object;
    }
    public void setObject(String object) {
        this.object = object;
    }
    public String getClient() {
        return client;
    }
    public void setClient(String client) {
        this.client = client;
    }
    public String getCreated() {
        return created;
    }
    public void setCreated(String created) {
        this.created = created;
    }
    public String getUpdated() {
        return updated;
    }
    public void setUpdated(String updated) {
        this.updated = updated;
    }
}
