package com.akhil.photoapp.api.users.service;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.akhil.photoapp.api.users.shared.UserDTO;

public interface UsersService extends UserDetailsService{

	UserDTO createUser(UserDTO userDetails);
	UserDTO getUserByEmail(String email);
	UserDTO getUserByUserId(String userId);
	
}
