package com.softtwig.controller;

import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.softtwig.bo.Login;
import com.softtwig.bo.LoginResponse;
import com.softtwig.entity.User;
import com.softtwig.jwt.JwtTokenUtils;
import com.softtwig.security.service.UserDetailsImpl;
import com.softtwig.service.UserService;

@RestController
 
public class LoginController {
	
	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	UserService userService;

	

	@Autowired
	PasswordEncoder encoder;

	@Autowired
	JwtTokenUtils gecTokenUtils;

	
	@PostMapping("/login")
	public ResponseEntity<?> authenticateUser( @RequestBody Login login,HttpServletRequest request) {

		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(login.getUsername(), login.getPassword()));

		SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwt = gecTokenUtils.generateJwtToken(authentication);
		
		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();		
		List<String> roles = userDetails.getAuthorities().stream()
				.map(item -> item.getAuthority())
				.collect(Collectors.toList());
		
		
		return ResponseEntity.ok(new LoginResponse(jwt, 
												 userDetails.getId(), 
												 userDetails.getUsername(), 
												 userDetails.getEmail(), 
												 roles));
	}

	@PostMapping("/create")
	
	public ResponseEntity<?> addUser(@RequestBody User user,HttpServletRequest request){
		String password=encoder.encode(user.getPassword());
		user.setPassword(password);
		 user=userService.createUser(user);
		if(null!=user) {
			return new ResponseEntity<>(user,HttpStatus.CREATED);
		}
		
		
		return new ResponseEntity<>(user,HttpStatus.NOT_FOUND);
		
		 
		
	}
	
	
	@GetMapping("/view")
	 @PreAuthorize("hasAuthority('ROLE_ADMIN')")
	public ResponseEntity<?> getAllUsers(HttpServletRequest request){
		
		List<User> users=userService.getAllUsers();
		
		
		return new ResponseEntity<>(users,HttpStatus.OK);
		
		
		
	}
	
@GetMapping("/get/{id}")
	public ResponseEntity<?> getUserById(@PathVariable Long id,HttpServletRequest request){
		
		User user=userService.getUserById(id);
		if(null!=user) {
			return new ResponseEntity<>(user,HttpStatus.OK);
		}
		
		
		return new ResponseEntity<>(user,HttpStatus.NOT_FOUND);
		
		 
		
	}
	
	
}
