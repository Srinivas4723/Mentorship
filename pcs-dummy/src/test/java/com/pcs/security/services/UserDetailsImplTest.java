package com.pcs.security.services;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import com.pcs.enums.Department;
import com.pcs.enums.JobTitle;
import com.pcs.enums.Location;
import com.pcs.enums.Role;
import com.pcs.model.User;

public class UserDetailsImplTest {
	@InjectMocks UserDetailsImpl userDetailsImpl;
	private User sampleUser() {
		User user= new User();
		user.setId(Long.valueOf(1));
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
	@Before
	public void setUp() throws Exception {
		 MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void testBuild() {
		User user= sampleUser();
		assertEquals(user.getId(),userDetailsImpl.build(user).getId());
	}

}
