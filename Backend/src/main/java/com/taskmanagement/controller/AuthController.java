package com.taskmanagement.controller;

import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.taskmanagement.dto.AuthenticationRequest;
import com.taskmanagement.dto.AuthenticationResponse;
import com.taskmanagement.dto.SignUpRequest;
import com.taskmanagement.dto.UserDTO;
import com.taskmanagement.entity.User;
import com.taskmanagement.repository.UserRepository;
import com.taskmanagement.service.auth.AuthService;
import com.taskmanagement.service.jwt.UserService;
import com.taskmanagement.utils.JwtUtil;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin("*")
public class AuthController {

	private AuthService authService;
	
	private UserRepository userRepository;
	
	private JwtUtil jwtUtil;
	
	private UserService userService;
	
	private final AuthenticationManager authenticationManager;
	
	public AuthController(AuthService authService, UserRepository userRepository, JwtUtil jwtUtil,
			UserService userService, AuthenticationManager authenticationManager) {
		super();
		this.authService = authService;
		this.userRepository = userRepository;
		this.jwtUtil = jwtUtil;
		this.userService = userService;
		this.authenticationManager = authenticationManager;
	}

	// Sign Up Handler
	@PostMapping("/signup")
	public ResponseEntity<?> signUpUser(@RequestBody SignUpRequest signUpRequest) {
		if(authService.hasUserWithEmail(signUpRequest.getEmail()))
			return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("User Already exists with this Email!");
		else {
			UserDTO userDTO = authService.signUpUser(signUpRequest);
			if(userDTO == null)
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User Not Created!");
			
			return ResponseEntity.status(HttpStatus.CREATED).body(userDTO);
		}
	}
	
	// Login Handler
	@PostMapping("/login")
	public AuthenticationResponse login(@RequestBody AuthenticationRequest authenticationRequest) {
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getEmail(), authenticationRequest.getPassword()));
		} catch (BadCredentialsException e) {
			throw new BadCredentialsException("Incorrect Username or Password!");
		}
		
		final UserDetails userDetails = userService.userDetailsService().loadUserByUsername(authenticationRequest.getEmail());
		Optional<User> optionalUser = userRepository.findUserByEmail(authenticationRequest.getEmail());
		final String jwtToken = jwtUtil.generateToken(userDetails);
		AuthenticationResponse authenticationResponse = new AuthenticationResponse();
		if(optionalUser.isPresent()) {
			authenticationResponse.setJwt(jwtToken);
			authenticationResponse.setUserId(optionalUser.get().getId());
			authenticationResponse.setRole(optionalUser.get().getRole());
		}
		return authenticationResponse;
	}
	
	@PostMapping("/verify")
	public ResponseEntity<?> verifyUser(@RequestParam("token") String token) {
	    Optional<User> optionalUser = userRepository.findByVerificationToken(token);
	    if (optionalUser.isPresent()) {
	        User user = optionalUser.get();
	        user.setEnabled(true);
	        user.setVerificationToken(null);
	        userRepository.save(user);
	        
	        // Send a notification email after successful verification
	        authService.sendPostVerificationEmail(user.getEmail());
	        
	        return ResponseEntity.ok(Map.of("message","User verified successfully!"));
	    } else {
	        return ResponseEntity.badRequest().body(Map.of("message", "Invalid verification token!"));
	    }
	}
}
