package com.softtwig.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.softtwig.dao.UserDao;
import com.softtwig.entity.User;
@Service
@Transactional
public class UserServiceImpl implements UserService{

	@Autowired
	UserDao userdao;
	
	@Override
	public Optional<User> findByUsername(String username) {
		
		return userdao.findByUsername(username);
	}
	@Override
	public List<User> getAllUsers() {
		// TODO Auto-generated method stub
		return userdao.findAll();
	}
	@Override
	public User createUser(User user) {
		
		return userdao.save(user);
	}
	@Override
	public User getUserById(Long id) {
		
		return userdao.findById(id).get();
	}

}
