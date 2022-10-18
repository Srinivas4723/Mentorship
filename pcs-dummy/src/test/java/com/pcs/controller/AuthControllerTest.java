package com.pcs.controller;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.pcs.enums.Department;
import com.pcs.enums.JobTitle;
import com.pcs.enums.Location;
import com.pcs.enums.Role;
import com.pcs.model.User;
import com.pcs.repository.UserRepository;
import com.pcs.request.LoginRequest;
import com.pcs.response.AdminResponse;
import com.pcs.security.jwt.JwtUtils;
import com.pcs.security.services.UserDetailsImpl;
import com.pcs.service.PcsService;

public class AuthControllerTest {
	@InjectMocks AuthController authController;
	@Mock UserRepository userRepository;
	@Mock PcsService service;
	@Mock PasswordEncoder encoder;
	@Mock JwtUtils jwtUtils;
	@Mock AuthenticationManager authenticationManager;
	private User user;
	private LoginRequest loginRequest;
	private AdminResponse adminResponse;
	private User sampleUser() {
		User user= new User();
		user.setFirstname("firstname");
		user.setLastname("lastname");
		user.setUsername("username");
		user.setLocation(Location.HYDERABAD);
		user.setDepartment(Department.FINANCE);
		user.setJobtitle(JobTitle.ANALYST);
		user.setRole(Role.ADMIN);
		user.setPassword("password");
		return user;
	}
	private LoginRequest sampleLoginRequest() {
		LoginRequest loginRequest= new LoginRequest();
		loginRequest.setUsername("username");
		loginRequest.setPassword("password");
		return loginRequest;
	}
	private AdminResponse sampleAdminResponse() {
		AdminResponse adminResponse=new AdminResponse();
		adminResponse.setEmpid(Long.valueOf(1));
		adminResponse.setFirstname("firstname");
		adminResponse.setLastname("lastname");
		adminResponse.setLocation(Location.HYDERABAD);
		adminResponse.setJobtitle(JobTitle.ANALYST);
		adminResponse.setDepartment(Department.FINANCE);
		adminResponse.setRole(Role.ADMIN);
		return adminResponse;
	}
	@Before
	public void setup() {
	    MockitoAnnotations.initMocks(this);
	    user=sampleUser();
	    loginRequest=sampleLoginRequest();
	    adminResponse=sampleAdminResponse();
	}

	@Test
	public void testRegisterUserSuccess() throws Exception {
		
		Mockito.when(service.findByUsername(user.getUsername())).thenReturn(Optional.empty());
		Mockito.when(encoder.encode(user.getUsername())).thenReturn("password");
		assertEquals(ResponseEntity.ok("Registration Success"),authController.registerUser(user));
		
	}
	
	@Test
	public void testRegisterUserFail() throws Exception {
		
		Mockito.when(service.findByUsername(user.getUsername())).thenReturn(Optional.of(user));
		assertEquals(ResponseEntity.badRequest().body("User Already Exists"),authController.registerUser(user));
		
	}

	@Test
	public void testAuthenticateUser() {
		
		GrantedAuthority authority= new SimpleGrantedAuthority("ROLE_"+user.getRole());
		List<GrantedAuthority> authorities = new ArrayList<>();
		authorities.add(authority);
		user.setId(Long.valueOf(1));
		UserDetailsImpl userDetailsImpl= new UserDetailsImpl(user.getId(),user.getUsername(),user.getPassword(),authorities);
		
		Authentication authentication=	new UsernamePasswordAuthenticationToken(userDetailsImpl,authorities );
		when( authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()))).thenReturn(authentication);
		when(jwtUtils.generateJwtToken(authentication)).thenReturn("ksdfhdfueh");
		
		when(userRepository.findByUsername(loginRequest.getUsername())).thenReturn(Optional.of(user));
		assertEquals(200,authController.authenticateUser(loginRequest).getStatusCodeValue());
	}
	@Test
	public void testGetUserData() {
		List<User> list=Arrays.asList(user);
		when(userRepository.findAllByRoleOrRole(Role.COMPENSATION,Role.REPORT)).thenReturn(list);
		assertEquals(user.getId(),authController.getUserData().get(0).getEmpid());
	}
	
	@Test
	public void testUpdateUser() {
		when(userRepository.findById(adminResponse.getEmpid())).thenReturn(Optional.of(user));
		List<User> list=Arrays.asList(user);
		when(userRepository.findAllByRoleOrRole(Role.COMPENSATION,Role.REPORT)).thenReturn(list);
		assertEquals(user.getId(),authController.updateUser(adminResponse).get(0).getEmpid());
	}
	@Test
	public void testUpdateUserWithInvalidUser() {
		when(userRepository.findById(adminResponse.getEmpid())).thenReturn(Optional.empty());
		assertEquals(0,authController.updateUser(adminResponse).size());
	}
}
