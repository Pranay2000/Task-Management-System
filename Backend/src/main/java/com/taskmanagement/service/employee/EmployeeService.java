package com.taskmanagement.service.employee;

import java.util.List;

import com.taskmanagement.dto.CommentDTO;
import com.taskmanagement.dto.TaskDTO;

public interface EmployeeService {

	List<TaskDTO> getTasksByUserId();
	
	TaskDTO updateTask(Long id, String status);
	
	TaskDTO getTaskById(Long id);
	
	CommentDTO postComment(Long taskId, String content);
	
	List<CommentDTO> getCommentsByTaskId(Long taskId);
}
