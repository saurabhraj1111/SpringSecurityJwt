package com.jocata.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jocata.entity.User;
import com.jocata.model.AuthRequest;
import com.jocata.model.UserRequest;
import com.jocata.repository.UserRepository;
import com.jocata.utils.JWTUtils;

@RestController
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JWTUtils jwtUtils;

	@RequestMapping("/getMessage")
	public String getMessage() {
		return "Hi Saurabh raj";
	}

	@RequestMapping("/addUser")
	public String addUser(@RequestBody UserRequest userRequest) {
		// String user = userRequest.getUser();
		User user = new User();
		user.setUser(userRequest.getUser());
		// encode Password
		String encodedPass = passwordEncoder.encode(userRequest.getPassword());

		user.setPassword(encodedPass);
		// user.setPassword(userRequest.getPassword());
		userRepository.save(user);

		return userRequest.getUser() + "  Added successfully";
	}

	@RequestMapping("/getAll")
	public List<User> getAllUser() {
		List<User> users = null;
		users = userRepository.findAll();
		return users;
	}

	@RequestMapping("/authenticate")
	public String authenticate(@RequestBody AuthRequest authRequest) {

		try {
			// authenticate
			authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(authRequest.getUser(), authRequest.getPassword()));
		} catch (Exception ex) {
			throw new UsernameNotFoundException("invalid User name or Password");
		}

		// generate token
		String token = jwtUtils.generateToken(authRequest.getUser());
		// String token = "kjgykyllyugyg6786hjgyuih";
		return token;

	}
}
