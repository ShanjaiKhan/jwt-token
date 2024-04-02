package com.softtwig.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.softtwig.entity.User;

public interface UserDao extends JpaRepository<User, Long>{

	Optional<User> findByUsername(String username);
	
	

}
