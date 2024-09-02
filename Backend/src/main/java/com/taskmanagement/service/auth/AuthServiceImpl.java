package com.taskmanagement.service.auth;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.taskmanagement.dto.SignUpRequest;
import com.taskmanagement.dto.UserDTO;
import com.taskmanagement.entity.Role;
import com.taskmanagement.entity.User;
import com.taskmanagement.repository.UserRepository;

import jakarta.annotation.PostConstruct;

@Service
public class AuthServiceImpl implements AuthService {

	@Autowired
	private UserRepository userRepository;

	@Value("${auth.admin.password}")
	private String adminPassword;
	
	@Value("${spring.mail.username}")
	private String fromEmail;
	
	@Value("${verification.base.url}")
	private String verificationBaseUrl;
	
	@Autowired
	private JavaMailSender javaMailSender;
		
	public AuthServiceImpl(UserRepository userRepository, @Value("${auth.admin.password}") String adminPassword) {
		super();
		this.userRepository = userRepository;
		this.adminPassword = adminPassword;
	}
	
	// Create admin account if it doesn't exist
	@PostConstruct
	public void createAdminAccount() {
		Optional<User> optionalUser = userRepository.findByRole(Role.ADMIN);
		if(optionalUser.isEmpty()) {
			User user = new User();
			user.setName("Pranay");
			user.setEmail("admin@test.com");
			user.setPassword(new BCryptPasswordEncoder().encode(this.adminPassword));
			user.setRole(Role.ADMIN);
			userRepository.save(user);
			System.out.println("Admin account created successfully!");
		} else {
			System.out.println("Admin Account already exists!");
		}
	}

	// Sign up a new user
	@Override
	public UserDTO signUpUser(SignUpRequest signUpRequest) {
		User user = new User();
		user.setEmail(signUpRequest.getEmail());
		user.setName(signUpRequest.getName());
		user.setPassword(new BCryptPasswordEncoder().encode(signUpRequest.getPassword()));
		user.setRole(Role.EMPLOYEE);
		user.setEnabled(false);
		
		// generate a verification token
	    String verificationToken = UUID.randomUUID().toString();
	    user.setVerificationToken(verificationToken);	    
		User createdUser = userRepository.save(user);
		
	    // send a verification email to the user
		sendVerificationEmail(user.getEmail(), verificationToken);
		
		UserDTO userDTO = createdUser.getUserDTO();
	    userDTO.setVerificationToken(verificationToken);
		return userDTO;
		
	}

	// Check if a user with a given email exists
	@Override
	public boolean hasUserWithEmail(String email) {
		return userRepository.findUserByEmail(email).isPresent();
	}
	
	private void sendVerificationEmail(String email, String verificationToken) {
		MimeMessagePreparator messagePreparator = mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
            messageHelper.setFrom(fromEmail);
            messageHelper.setTo(email);
            messageHelper.setSubject("Verify your Email Address. TSM Registration!");

            String verificationLink = verificationBaseUrl + "?token=" + verificationToken;

            String emailBody = "<html><body>" +
                    "<h2>Verify your email address</h2>" +
                    "<p>Please click on the link below to verify your email address:</p>" +
                    "<a href='" + verificationLink + "'>" + verificationLink + "</a>" +
                    "</body></html>";

            messageHelper.setText(emailBody, true);
        };

        javaMailSender.send(messagePreparator);
	}
	
	// Send post-verification notification email
    @Override
    public void sendPostVerificationEmail(String email) {
        MimeMessagePreparator messagePreparator = mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
            messageHelper.setFrom(fromEmail);
            messageHelper.setTo(email);
            messageHelper.setSubject("Email Verified Successfully!");

            String emailBody = "<html><body>" +
                    "<h2>Welcome to Task Management System</h2>" +
                    "<p>Your email has been successfully verified. You can now log in to your account.</p>" +
                    "</body></html>";

            messageHelper.setText(emailBody, true);
        };

        javaMailSender.send(messagePreparator);
    }
}
