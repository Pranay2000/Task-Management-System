package com.taskmanagement.service.admin;

import java.util.List;

import com.taskmanagement.dto.CommentDTO;
import com.taskmanagement.dto.TaskDTO;
import com.taskmanagement.dto.UserDTO;

public interface AdminService {

	List<UserDTO> getUsers();
	
	TaskDTO createTask(TaskDTO taskDTO);
	
	List<TaskDTO> getAllTasks();
	
	void deleteTask(Long id);
	
	TaskDTO getTaskById(Long id);
	
	TaskDTO updateTask(Long id, TaskDTO taskDTO);
	
	List<TaskDTO> searchTaskByTitle(String title);
	
	CommentDTO postComment(Long taskId, String content);
	
	List<CommentDTO> getCommentsByTaskId(Long taskId);
}
