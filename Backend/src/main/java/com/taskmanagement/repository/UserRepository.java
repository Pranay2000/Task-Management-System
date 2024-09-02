package com.taskmanagement.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.taskmanagement.entity.Role;
import com.taskmanagement.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{

	Optional<User> findUserByEmail(String email);
	Optional<User> findByRole(Role role);
	Optional<User> findByVerificationToken(String token);
}
