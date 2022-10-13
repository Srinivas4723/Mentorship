package com.pcs.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.pcs.enums.Department;
import com.pcs.enums.JobTitle;
import com.pcs.enums.Location;
import com.pcs.model.User;
import com.pcs.repository.UserRepository;

class AuthControllerTest {
	@Autowired MockMvc mockmvc;
	@Mock UserRepository userRepository;
	public User sampleUser() {
		User user= new User();
		user.setFirstname("firstname");
		user.setLastname("lastname");
		user.setUsername("username");
		user.setLocation(Location.HYDERABAD);
		user.setJobtitle(JobTitle.ANALYST);
		user.setDepartment(Department.FINANCE);
		user.setPassword("password");
		return user;
	}
	@Test
	void testRegisterUser()  {
		
		try {
			User user= sampleUser();
			when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.empty());
			mockmvc.perform( MockMvcRequestBuilders
				      .post("/register", user)
				      .accept(MediaType.APPLICATION_JSON))
				      //.andDo(print())
				      .andExpect(status().isOk());
		} catch (Exception e) {
			e.printStackTrace();
		}
			      //.andExpect(MockMvcResultMatchers.content().contentType("Registration Success"));
	}

	@Test
	void testAuthenticateUser() {
		fail("Not yet implemented");
	}

	@Test
	void testUpdateUser() {
		fail("Not yet implemented");
	}

}
