package com.taskmanagement.dto;

import com.taskmanagement.entity.Role;

public class AuthenticationResponse {

	private String jwt;
	private Long userId;
	private Role role;
	
	
	public AuthenticationResponse() {

	}

	public AuthenticationResponse(String jwt, Long userId, Role role) {
		super();
		this.jwt = jwt;
		this.userId = userId;
		this.role = role;
	}

	public String getJwt() {
		return jwt;
	}

	public void setJwt(String jwt) {
		this.jwt = jwt;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	@Override
	public String toString() {
		return "AuthenticationResponse [jwt=" + jwt + ", userId=" + userId + ", role=" + role + "]";
	}
	
}
