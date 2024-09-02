package com.taskmanagement.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.taskmanagement.dto.CommentDTO;
import com.taskmanagement.dto.TaskDTO;
import com.taskmanagement.service.employee.EmployeeService;

@RestController
@RequestMapping("/api/employee")
@CrossOrigin("*")
public class EmployeeController {

	private final EmployeeService employeeService;
	
	public EmployeeController(EmployeeService employeeService) {
		super();
		this.employeeService = employeeService;
	}

	// Get All tasks of Employee Handler
	@GetMapping("/tasks")
	public ResponseEntity<List<TaskDTO>> getTasksByUserId() {
		return ResponseEntity.ok(employeeService.getTasksByUserId());
	}
	
	// Update Task Status Handler
	@GetMapping("/task/{id}/{status}")
	public ResponseEntity<TaskDTO> updateTask(@PathVariable Long id, @PathVariable String status) {
		TaskDTO updatedTaskDTO = employeeService.updateTask(id, status);
		if(updatedTaskDTO == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}
		
		return ResponseEntity.ok(updatedTaskDTO);
	}
	
	// Get Task By Id Handler
	@GetMapping("/task/{id}")
	public ResponseEntity<TaskDTO> getTaskById(@PathVariable Long id) {
		return ResponseEntity.ok(employeeService.getTaskById(id));
	}
	
	// Post Employee Comment Handler
	@PostMapping("/task/comment/{taskId}")
	public ResponseEntity<CommentDTO> postComment(@PathVariable Long taskId, @RequestParam String content) {
		CommentDTO createdcommentDTO = employeeService.postComment(taskId, content);
		if(createdcommentDTO == null) 
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		
		return ResponseEntity.status(HttpStatus.CREATED).body(createdcommentDTO);
	}
	
	// Get All Comments for Specific Task Handler
	@GetMapping("/comments/{taskId}")
	public ResponseEntity<List<CommentDTO>> getCommentsByTaskId(@PathVariable Long taskId) {
		return ResponseEntity.ok(employeeService.getCommentsByTaskId(taskId));
	}
	
}
