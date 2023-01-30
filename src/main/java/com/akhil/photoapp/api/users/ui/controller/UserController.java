package com.akhil.photoapp.api.users.ui.controller;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.akhil.photoapp.api.users.service.UsersService;
import com.akhil.photoapp.api.users.shared.UserDTO;
import com.akhil.photoapp.api.users.ui.model.CreateUserRequestModel;
import com.akhil.photoapp.api.users.ui.model.CreateUserResponseModel;
import com.akhil.photoapp.api.users.ui.model.UserResponseModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/users")
public class UserController {

	@Autowired
	private Environment env;

	@Autowired
	UsersService usersService;

	@GetMapping("/status/check")
	public String status() {
		return "Working on port " + env.getProperty("local.server.port") + "with token secret: "
				+ env.getProperty("token.secret");

	}

	@PostMapping(consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE }, produces = {
			MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	public ResponseEntity<CreateUserResponseModel> createUser(@Valid @RequestBody CreateUserRequestModel userDetails) {
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		UserDTO userDTO = modelMapper.map(userDetails, UserDTO.class);
		UserDTO createdUser = usersService.createUser(userDTO);
		CreateUserResponseModel returnValue = modelMapper.map(createdUser, CreateUserResponseModel.class);
		return ResponseEntity.status(HttpStatus.CREATED).body(returnValue);

	}

	@GetMapping(value="/{userId}" ,produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	public ResponseEntity<UserResponseModel> getUser(@PathVariable("userId") String userId) {
		UserDTO userDto = usersService.getUserByUserId(userId);
		UserResponseModel returnValue = new ModelMapper().map(userDto, UserResponseModel.class);
		return ResponseEntity.status(HttpStatus.OK).body(returnValue);
	}
}
