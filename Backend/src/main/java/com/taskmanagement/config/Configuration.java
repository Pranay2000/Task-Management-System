package com.taskmanagement.config;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.taskmanagement.entity.Role;
import com.taskmanagement.service.jwt.UserService;

/**
 * Configuration class for Spring Security
 */
@org.springframework.context.annotation.Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class Configuration {

	private final JwtAuthenticationFilter jwtAuthenticationFilter;
	
	private final UserService userService;

	public Configuration(JwtAuthenticationFilter jwtAuthenticationFilter, UserService userService) {
		super();
		this.jwtAuthenticationFilter = jwtAuthenticationFilter;
		this.userService = userService;
	}
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.csrf(AbstractHttpConfigurer::disable)
			.authorizeHttpRequests(request -> request.requestMatchers("/api/auth/**").permitAll() // // allow access to /api/auth/** endpoints
					.requestMatchers("/api/admin/**").hasAnyAuthority(Role.ADMIN.name()) // restrict access to /api/admin/** endpoints to ADMIN role
					.requestMatchers("/api/employee/**").hasAnyAuthority(Role.EMPLOYEE.name()) // restrict access to /api/employee/** endpoints to EMPLOYEE role
					.anyRequest().authenticated()).sessionManagement(manager -> manager.sessionCreationPolicy(STATELESS))
			.authenticationProvider(authenticationProvider()).addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
			return http.build();
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(userService.userDetailsService());
		authProvider.setPasswordEncoder(passwordEncoder());
		return authProvider;
	}
	
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
		return config.getAuthenticationManager();
	}
}
