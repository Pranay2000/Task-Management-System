package com.taskmanagement.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.taskmanagement.entity.Comments;

@Repository
public interface CommentRepository extends JpaRepository<Comments, Long>{
	
	List<Comments> findAllByTaskId(Long taskId);
}
