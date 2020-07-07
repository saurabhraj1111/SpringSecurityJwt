package com.jocata.utils;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.jocata.entity.User;
import com.jocata.repository.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		System.out.println("=======userInput========"+username);
		User user = userRepo.findByUser(username);
		//password encode
		System.out.println("=======userFetch========"+user.getUser());
		System.out.println("==============="+user.getPassword());
		//String encodedPass=passwordEncoder.encode(user.getPassword());
		 String encodedPass=user.getPassword();
		//match encoded pass
		
		return new org.springframework.security.core.userdetails.User(user.getUser(),encodedPass,new ArrayList<>());
	} 
	
}
