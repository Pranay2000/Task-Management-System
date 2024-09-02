package com.taskmanagement.service.jwt;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.taskmanagement.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService{

	private final UserRepository userRepository;
	
	public UserServiceImpl(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	// Return a UserDetailsService instance
	@Override
	public UserDetailsService userDetailsService() {
		return new UserDetailsService() {
			
			// Load a user by username (email)
			@Override
			public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
				return userRepository.findUserByEmail(username).orElseThrow(()->new UsernameNotFoundException("User Not Found"));
			}
		};
	}
}
