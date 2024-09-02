package com.taskmanagement.service.auth;

import com.taskmanagement.dto.SignUpRequest;
import com.taskmanagement.dto.UserDTO;

public interface AuthService {

	UserDTO signUpUser(SignUpRequest signUpRequest);
	
	boolean hasUserWithEmail(String email);

	void sendPostVerificationEmail(String email);
}
