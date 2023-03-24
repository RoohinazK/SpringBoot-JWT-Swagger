package com.example.springrolejwt.service;

import java.util.List;

import com.example.springrolejwt.model.User;
import com.example.springrolejwt.model.UserDto;

public interface UserService {
	
	User save(UserDto user);
	List<User> findAll();
	User findOne(String username);
	
    //User save(UserDto user);
    //List<User> findAll();
    //User findOne(String username);
}
