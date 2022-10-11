package com.pcs.controller;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pcs.model.User;
import com.pcs.repository.UserRepository;
import com.pcs.request.LoginRequest;
import com.pcs.security.jwt.JwtUtils;
import com.pcs.security.response.JwtResponse;
import com.pcs.security.services.UserDetailsImpl;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/user/")
public class AuthController extends BaseController {
	@Autowired UserRepository userRepository;
	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	PasswordEncoder encoder;

	@Autowired
	JwtUtils jwtUtils;
	Random random= new Random();
	@PostMapping("/register")
	public ResponseEntity<?> registerUser(@Valid @RequestBody User user){
		//ExampleMatcher rolematcher=ExampleMatcher.matchingAll().withIgnorePaths("role","password");
		//Optional<User> existingUser=userRepository.findONe(Example.of(user,rolematcher));
		Optional<User> existingUser=userRepository.findByUsername(user.getUsername());
		if(existingUser.isEmpty()) {
			user.setPassword(encoder.encode(user.getPassword()));
			//user.setId("CU"+user.getFirstname().charAt(0)+user.getLastname().charAt(0)+ Math.abs(random.nextInt()));
			userRepository.save(user);
			
			return ResponseEntity.ok("Registration Success");
		}
		return ResponseEntity.badRequest().body("User Already Exists");
	}
	
	@PostMapping("/signin")
	public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

		SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwt = jwtUtils.generateJwtToken(authentication);
		
		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();		
		List<String> roles = userDetails.getAuthorities().stream()
				.map(item -> item.getAuthority())
				.collect(Collectors.toList());

		return ResponseEntity.ok(new JwtResponse(jwt, 
												 userDetails.getId(), 
												 userDetails.getUsername(), 
												 roles));
	}



}
