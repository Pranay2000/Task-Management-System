package com.taskmanagement.service.admin;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.taskmanagement.dto.CommentDTO;
import com.taskmanagement.dto.TaskDTO;
import com.taskmanagement.dto.UserDTO;
import com.taskmanagement.entity.Comments;
import com.taskmanagement.entity.Role;
import com.taskmanagement.entity.Task;
import com.taskmanagement.entity.TaskStatus;
import com.taskmanagement.entity.User;
import com.taskmanagement.repository.CommentRepository;
import com.taskmanagement.repository.TaskRepository;
import com.taskmanagement.repository.UserRepository;
import com.taskmanagement.service.email.EmailService;
import com.taskmanagement.utils.JwtUtil;

import jakarta.persistence.EntityNotFoundException;

@Service
public class AdminServiceImpl implements AdminService{

	private final UserRepository userRepository;
	
	private final TaskRepository taskRepository;
	
	private final JwtUtil jwtUtil;
	
	private final CommentRepository commentRepository;
	
	private final EmailService emailService;
		
	public AdminServiceImpl(UserRepository userRepository, TaskRepository taskRepository, JwtUtil jwtUtil, CommentRepository commentRepository, EmailService emailService) {
		super();
		this.userRepository = userRepository;
		this.taskRepository = taskRepository;
		this.jwtUtil = jwtUtil;
		this.commentRepository = commentRepository;
		this.emailService = emailService;
	}

	// Get All Employees (Users with role = Employee)
	@Override
	public List<UserDTO> getUsers() {
		return userRepository.findAll()
				.stream()
				.filter(user -> user.getRole() == Role.EMPLOYEE)
				.map(User::getUserDTO)
				.collect(Collectors.toList());
	}

	// Create New Task
	@Override
	public TaskDTO createTask(TaskDTO taskDTO) {
		Optional<User> optionalUser = userRepository.findById(taskDTO.getEmployeeId());
		if(optionalUser.isPresent()) {
			Task task = new Task();
			task.setTitle(taskDTO.getTitle());
			task.setDescription(taskDTO.getDescription());
			task.setPriority(taskDTO.getPriority());
			task.setDueDate(taskDTO.getDueDate());
			task.setTaskStatus(TaskStatus.TODO);
			task.setUser(optionalUser.get());
			TaskDTO createdTask = taskRepository.save(task).getTaskDTO();
			emailService.sendTaskAssignmentEmail(optionalUser.get().getEmail(), task.getTitle());
			return createdTask;
		}
		
		return null;
	}

	// Get All Tasks List
	@Override
	public List<TaskDTO> getAllTasks() {
		return taskRepository.findAll()
				.stream()
				.sorted(Comparator.comparing(Task::getDueDate).reversed())
				.map(Task::getTaskDTO)
				.toList();
		
	}

	// Delete Task
	@Override
	public void deleteTask(Long id) {
		taskRepository.deleteById(id);
	}

	// Get Task By Id
	@Override
	public TaskDTO getTaskById(Long id) {
		Optional<Task> optionalTask = taskRepository.findById(id);
		return optionalTask.map(Task::getTaskDTO).orElse(null);
	}

	// Update Task
	@Override
	public TaskDTO updateTask(Long id, TaskDTO taskDTO) {
		Optional<Task> optionalTask = taskRepository.findById(id);
		Optional<User> optionalUser = userRepository.findById(taskDTO.getEmployeeId());
		if(optionalTask.isPresent() && optionalUser.isPresent()) {
			Task existingTask = optionalTask.get();
			existingTask.setTitle(taskDTO.getTitle());
			existingTask.setDescription(taskDTO.getDescription());
			existingTask.setDueDate(taskDTO.getDueDate());
			existingTask.setPriority(taskDTO.getPriority());
			existingTask.setTaskStatus(mapStringToTaskStatus(String.valueOf(taskDTO.getTaskStatus())));
			
			existingTask.setUser(optionalUser.get());
			
			return taskRepository.save(existingTask).getTaskDTO();
		}
		
		return null;
	}
	
	// Map a string to a TaskStatus enum value
	private TaskStatus mapStringToTaskStatus(String status) {
		return switch (status) {
			case "INPROGRESS" -> TaskStatus.INPROGRESS;
			case "DONE" -> TaskStatus.DONE;
			case "REFINEMENT" -> TaskStatus.REFINEMENT;
			case "CANCELLED"-> TaskStatus.CANCELLED;
			default -> TaskStatus.TODO;
		};
	}

	// Search Task By Title
	@Override
	public List<TaskDTO> searchTaskByTitle(String title) {
		return taskRepository.findAllByTitleContaining(title)
				.stream()
				.sorted(Comparator.comparing(Task::getDueDate).reversed())
				.map(Task::getTaskDTO)
				.collect(Collectors.toList());
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
			
			CommentDTO createdComment = commentRepository.save(comment).getCommentDTO();
			emailService.sendCommentNotificationEmail(optionalTask.get().getUser().getEmail(), optionalTask.get().getTitle(), content);
            return createdComment;
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
}
