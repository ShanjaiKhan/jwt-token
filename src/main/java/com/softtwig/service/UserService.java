package com.softtwig.service;

import java.util.List;
import java.util.Optional;

import com.softtwig.entity.User;

public interface UserService {

	Optional<User> findByUsername(String username);

	List<User> getAllUsers();

	User createUser(User user);

	User getUserById(Long id);

}
