package com.pcs.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pcs.enums.Role;
import com.pcs.model.User;
import com.pcs.repository.UserRepository;
import com.pcs.request.LoginRequest;
import com.pcs.response.AdminResponse;
import com.pcs.security.jwt.JwtUtils;
import com.pcs.security.response.JwtResponse;
import com.pcs.security.services.UserDetailsImpl;
import com.pcs.service.PcsService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/user/")
public class AuthController extends BaseController {
	@Autowired UserRepository userRepository;
	@Autowired PcsService service;
	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	PasswordEncoder encoder;

	@Autowired
	JwtUtils jwtUtils;
	
	@PostMapping("/register")
	public ResponseEntity<?> registerUser(@Valid @RequestBody User user){
		//ExampleMatcher rolematcher=ExampleMatcher.matchingAll().withIgnorePaths("role","password");
		//Optional<User> existingUser=userRepository.findONe(Example.of(user,rolematcher));
		//System.out.println("user1 ");
		//Optional<User> existingUser=userRepository.findByUsername(user.getUsername());
		Optional<User> existingUser=service.findByUsername(user.getUsername());
		if(existingUser.isEmpty()) {
			user.setPassword(encoder.encode(user.getPassword()));
			//user.setId("CU"+user.getFirstname().charAt(0)+user.getLastname().charAt(0)+ Math.abs(random.nextInt()));
			//userRepository.save(user);
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
		Optional<String> role = userDetails.getAuthorities().stream()
				.map(item -> item.getAuthority()).findAny();
//				.collect(Collectors.toList());
		Optional<User> user= userRepository.findByUsername(loginRequest.getUsername());
		return ResponseEntity.ok(new JwtResponse(jwt, 
												 userDetails.getId(), 
												 userDetails.getUsername(), 
												 user.get().getFirstname(),
												 user.get().getLastname(),
												 user.get().getLocation(),
												 user.get().getJobtitle(),
												 user.get().getDepartment(),
												 role.get()));
	}
	private AdminResponse createResponse(User user,AdminResponse adminResponse) {
		adminResponse.setEmpid(user.getId());
		adminResponse.setFirstname(user.getFirstname());
		adminResponse.setLastname(user.getLastname());
		adminResponse.setLocation(user.getLocation());
		adminResponse.setJobtitle(user.getJobtitle());
		adminResponse.setDepartment(user.getDepartment());
		adminResponse.setRole(user.getRole());
		return adminResponse;
	}
	@GetMapping("/userdata")
	@PreAuthorize("hasRole('ADMIN')")
	public List<AdminResponse> getUserData(){
		List<AdminResponse> adminResponseList=new ArrayList<>();
		List<User> users=userRepository.findAllByRoleOrRole(Role.COMPENSATION,Role.REPORT);
		users.forEach(user->adminResponseList.add(createResponse(user,new AdminResponse())));
		return adminResponseList;
		
	}
	
	@PutMapping("/updateuser")
	@PreAuthorize("hasRole('ADMIN')")
	public List<AdminResponse> updateUser(@RequestBody AdminResponse adminResponse){
		Optional<User> existingUser= userRepository.findById(adminResponse.getEmpid());
		if(existingUser.isPresent()) {
			existingUser.get().setFirstname(adminResponse.getFirstname());
			existingUser.get().setLastname(adminResponse.getLastname());
			existingUser.get().setLocation(adminResponse.getLocation());
			existingUser.get().setJobtitle(adminResponse.getJobtitle());
			existingUser.get().setDepartment(adminResponse.getDepartment());
			userRepository.save(existingUser.get());
			return getUserData();
		}
		return new ArrayList<>();
	}


}
