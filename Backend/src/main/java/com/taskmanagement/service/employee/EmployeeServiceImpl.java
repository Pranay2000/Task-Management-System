package com.taskmanagement.service.employee;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.taskmanagement.dto.CommentDTO;
import com.taskmanagement.dto.TaskDTO;
import com.taskmanagement.entity.Comments;
import com.taskmanagement.entity.Task;
import com.taskmanagement.entity.TaskStatus;
import com.taskmanagement.entity.User;
import com.taskmanagement.repository.CommentRepository;
import com.taskmanagement.repository.TaskRepository;
import com.taskmanagement.utils.JwtUtil;

import jakarta.persistence.EntityNotFoundException;

@Service
public class EmployeeServiceImpl implements EmployeeService {

	private final TaskRepository taskRepository;

	private final JwtUtil jwtUtil;
	
	private final CommentRepository commentRepository;
			
	public EmployeeServiceImpl(TaskRepository taskRepository, JwtUtil jwtUtil, CommentRepository commentRepository) {
		super();
		this.taskRepository = taskRepository;
		this.jwtUtil = jwtUtil;
		this.commentRepository = commentRepository;
	}

	// Get All Tasks List of the Employee
	@Override
	public List<TaskDTO> getTasksByUserId() {
		User user = jwtUtil.getLoggedInUser();
		if(user != null) {
			return taskRepository.findAllByUserId(user.getId())
					.stream()
					.sorted(Comparator.comparing(Task::getDueDate).reversed())
					.map(Task::getTaskDTO)
					.collect(Collectors.toList()); 
		}
		
		throw new EntityNotFoundException("User Not Found");
	}

	// Update Task Status
	@Override
	public TaskDTO updateTask(Long id, String status) {
		Optional<Task> optionalTask = taskRepository.findById(id);
		if(optionalTask.isPresent()) {
			Task existingTask = optionalTask.get();
			existingTask.setTaskStatus(mapStringToTaskStatus(status));
			return taskRepository.save(existingTask).getTaskDTO();
		}
		
		throw new EntityNotFoundException("Task Not Found");
	}
	
	// Get the task by Id
	@Override
	public TaskDTO getTaskById(Long id) {
		Optional<Task> optionalTask = taskRepository.findById(id);
		return optionalTask.map(Task::getTaskDTO).orElse(null);
	}
	
	// Post Comment on Task
	@Override
	public CommentDTO postComment(Long taskId, String content) {
		Optional<Task> optionalTask = taskRepository.findById(taskId);
		User user = jwtUtil.getLoggedInUser();
		if((optionalTask.isPresent()) && user != null) {
			Comments comment = new Comments();
			comment.setCreatedAt(new Date());
			comment.setContent(content);
			comment.setTask(optionalTask.get());
			comment.setUser(user);
			return commentRepository.save(comment).getCommentDTO();
		}
		
		throw new EntityNotFoundException("User or Task Not Found!");
	}

	// Get All Comments for a Task
	@Override
	public List<CommentDTO> getCommentsByTaskId(Long taskId) {
		return commentRepository.findAllByTaskId(taskId)
				.stream()
				.map(Comments::getCommentDTO)
				.collect(Collectors.toList());
	}
	
	private TaskStatus mapStringToTaskStatus(String status) {
		return switch (status) {
			case "INPROGRESS" -> TaskStatus.INPROGRESS;
			case "DONE" -> TaskStatus.DONE;
			case "REFINEMENT" -> TaskStatus.REFINEMENT;
			case "CANCELLED"-> TaskStatus.CANCELLED;
			default -> TaskStatus.TODO;
		};
	}
}
