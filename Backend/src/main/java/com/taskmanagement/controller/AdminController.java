package com.taskmanagement.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.taskmanagement.dto.CommentDTO;
import com.taskmanagement.dto.TaskDTO;
import com.taskmanagement.service.admin.AdminService;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin("*")
public class AdminController {
	
	private final AdminService adminService;

	public AdminController(AdminService adminService) {
		super();
		this.adminService = adminService;
	}
	
	// Get All Users Handler
	@GetMapping("/users")
	public ResponseEntity<?> getUsers() {
		return ResponseEntity.ok(adminService.getUsers());
	}
	
	// Create Task Handler
	@PostMapping("/task")
	public ResponseEntity<TaskDTO> createTask(@RequestBody TaskDTO taskDTO) {
		TaskDTO createTaskDTO = adminService.createTask(taskDTO);
		if(createTaskDTO == null) 
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		
		return ResponseEntity.status(HttpStatus.CREATED).body(createTaskDTO);
	}
	
	// Get All Tasks Handler
	@GetMapping("/tasks")
	public ResponseEntity<?> getAllTasks() {
		return ResponseEntity.ok(adminService.getAllTasks());
	}
	
	// Delete Task Handler
	@DeleteMapping("/task/{id}")
	public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
		adminService.deleteTask(id);
		return ResponseEntity.ok(null);
	}
	
	// Get Task By Task Id Handler
	@GetMapping("/task/{id}")
	public ResponseEntity<TaskDTO> getTaskById(@PathVariable Long id) {
		return ResponseEntity.ok(adminService.getTaskById(id));
	}
	
	// Update Task Handler
	@PutMapping("/task/{id}")
	public ResponseEntity<?> updateTask(@PathVariable Long id, @RequestBody TaskDTO taskDTO) {
		TaskDTO updatedTask = adminService.updateTask(id, taskDTO);
		if(updatedTask == null)	return ResponseEntity.notFound().build();
		
		return ResponseEntity.ok(updatedTask);
	}
	
	// Search Task Handler
	@GetMapping("/tasks/search/{title}")
	public ResponseEntity<List<TaskDTO>> searchTask(@PathVariable String title) {
		return ResponseEntity.ok(adminService.searchTaskByTitle(title));
	}
	
	// Post Admin Comment Handler
	@PostMapping("/task/comment/{taskId}")
	public ResponseEntity<CommentDTO> postComment(@PathVariable Long taskId, @RequestParam String content) {
		CommentDTO createdcommentDTO = adminService.postComment(taskId, content);
		if(createdcommentDTO == null) 
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		
		return ResponseEntity.status(HttpStatus.CREATED).body(createdcommentDTO);
	}
	
	// Get All Comments for specific Task Handler
	@GetMapping("/comments/{taskId}")
	public ResponseEntity<List<CommentDTO>> getCommentsByTaskId(@PathVariable Long taskId) {
		return ResponseEntity.ok(adminService.getCommentsByTaskId(taskId));
	}
}
