package com.akhil.photoapp.api.users.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.akhil.photoapp.api.users.data.UsersEntity;
import com.akhil.photoapp.api.users.data.UsersRepository;
import com.akhil.photoapp.api.users.shared.UserDTO;
import com.akhil.photoapp.api.users.ui.model.AlbumResponseModel;
import com.akhil.photoapp.api.users.ui.model.AlbumsServiceClient;
import feign.FeignException;

@Service
public class UsersServiceImpl implements UsersService {
	BCryptPasswordEncoder bCryptPasswordEncoder;
	UsersRepository usersRepository;
	// RestTemplate restTemplate;
	Environment env;
	@Autowired
	AlbumsServiceClient albumsServiceClient;

	Logger logger = org.slf4j.LoggerFactory.getLogger(this.getClass());

	@Autowired
	public UsersServiceImpl(UsersRepository usersRepository, BCryptPasswordEncoder bCryptPasswordEncoder,
			Environment env) {
		this.usersRepository = usersRepository;
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
		// this.restTemplate = restTemplate;
		this.env = env;
	}

	@Override
	public UserDTO createUser(UserDTO userDetails) {
		userDetails.setUserId(UUID.randomUUID().toString());
		userDetails.setEncryptedPassword(bCryptPasswordEncoder.encode(userDetails.getPassword()));
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		UsersEntity userEntity = modelMapper.map(userDetails, UsersEntity.class);
		usersRepository.save(userEntity);
		UserDTO returnValue = modelMapper.map(userEntity, UserDTO.class);
		return returnValue;

	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UsersEntity userEntity = usersRepository.findByEmail(username);

		if (userEntity == null) {
			throw new UsernameNotFoundException(username);
		}
		return new User(userEntity.getEmail(), userEntity.getEncryptedPassword(), true, true, true, true,
				new ArrayList<>());
	}

	@Override
	public UserDTO getUserByEmail(String email) {
		UsersEntity userEntity = usersRepository.findByEmail(email);
		if (userEntity == null) {
			throw new UsernameNotFoundException(email);
		}
		return new ModelMapper().map(userEntity, UserDTO.class);
	}

	@Override
	public UserDTO getUserByUserId(String userId) {
		// TODO Auto-generated method stub
		UsersEntity userEntity = usersRepository.findByUserId(userId);
		if (userEntity == null)
			throw new UsernameNotFoundException("User Not Found");

		UserDTO userDto = new ModelMapper().map(userEntity, UserDTO.class);
//		String albumsUrl = String.format(env.getProperty("albums.url"), userId);
//		ResponseEntity<List<AlbumResponseModel>> response = restTemplate.exchange(albumsUrl, HttpMethod.GET, null,
//				new ParameterizedTypeReference<List<AlbumResponseModel>>() {
//				});
//		List<AlbumResponseModel> albumsList = response.getBody();

		/*
		 * List<AlbumResponseModel> albumsList = null; try { albumsList =
		 * albumsServiceClient.getAlbums(userId); } catch (FeignException e) {
		 * logger.error(e.getLocalizedMessage()); }
		 */
		logger.info("Before calling albums microservices");
		List<AlbumResponseModel> albumsList = albumsServiceClient.getAlbums(userId);
		logger.info("After calling albums microservices");

		userDto.setAlbums(albumsList);
		return userDto;
	}
}
