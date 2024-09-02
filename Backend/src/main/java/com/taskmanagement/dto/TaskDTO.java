package com.taskmanagement.dto;

import java.sql.Date;

import com.taskmanagement.entity.TaskStatus;

public class TaskDTO {
	
	private Long id;
	private String title;
	private String description;
	private Date dueDate;
	private String priority;
	private TaskStatus taskStatus;
	private Long employeeId;
	private String employeeName;
	
	public TaskDTO() {
		
	}

	public TaskDTO(Long id, String title, String description, Date dueDate, String priority, TaskStatus taskStatus,
			Long employeeId, String employeeName) {
		super();
		this.id = id;
		this.title = title;
		this.description = description;
		this.dueDate = dueDate;
		this.priority = priority;
		this.taskStatus = taskStatus;
		this.employeeId = employeeId;
		this.employeeName = employeeName;
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

	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

	public TaskStatus getTaskStatus() {
		return taskStatus;
	}

	public void setTaskStatus(TaskStatus taskStatus) {
		this.taskStatus = taskStatus;
	}

	public Long getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(Long employeeId) {
		this.employeeId = employeeId;
	}

	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	public Date getDueDate() {
		return dueDate;
	}

	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}

	@Override
	public String toString() {
		return "TaskDTO [id=" + id + ", title=" + title + ", description=" + description + ", dueDate=" + dueDate
				+ ", priority=" + priority + ", taskStatus=" + taskStatus + ", employeeId=" + employeeId
				+ ", employeeName=" + employeeName + "]";
	}

}
