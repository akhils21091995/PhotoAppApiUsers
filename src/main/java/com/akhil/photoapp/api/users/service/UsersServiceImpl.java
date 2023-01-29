package com.akhil.photoapp.api.users.service;

import java.util.ArrayList;
import java.util.UUID;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.akhil.photoapp.api.users.data.UsersEntity;
import com.akhil.photoapp.api.users.data.UsersRepository;
import com.akhil.photoapp.api.users.shared.UserDTO;

@Service
public class UsersServiceImpl implements UsersService {
    BCryptPasswordEncoder bCryptPasswordEncoder;
	UsersRepository usersRepository;

	@Autowired
	public UsersServiceImpl(UsersRepository usersRepository,BCryptPasswordEncoder bCryptPasswordEncoder) {
		this.usersRepository = usersRepository;
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
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
        return new User(userEntity.getEmail(), userEntity.getEncryptedPassword(),true, true, true, true, new ArrayList<>());
    }

	  @Override
	    public UserDTO getUserByEmail(String email) {
	        UsersEntity userEntity = usersRepository.findByEmail(email);
	        if (userEntity == null) {
	            throw new UsernameNotFoundException(email);
	        }
	        return new ModelMapper().map(userEntity, UserDTO.class);
	    }
		
}
