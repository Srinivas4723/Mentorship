package com.pcs.service;

import static org.junit.Assert.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

import com.pcs.enums.CompensationMethodology;
import com.pcs.enums.Department;
import com.pcs.enums.JobTitle;
import com.pcs.enums.Location;
import com.pcs.enums.Role;
import com.pcs.model.Method;
import com.pcs.model.Plan;
import com.pcs.model.User;
import com.pcs.repository.PlanRepository;
import com.pcs.repository.UserRepository;

public class ServiceTest {
	@InjectMocks PcsService service;
	@Mock UserRepository userRepository;
	@Mock PlanRepository planRepository;
	
	public Method sampleMethod() {
		Method method = new Method();
		method.setId(Long.valueOf(1));
		method.setMinimum(Long.valueOf(1));
		method.setMaximum(Long.valueOf(2));
		method.setPercentage(Long.valueOf(3));
		method.setPlanid(Long.valueOf(1));
		return method;
	}
	public Plan samplePlan() {
		Plan plan = new Plan();
		plan.setPartnername("partnername");
		plan.setCompensationplan("plan1");
		plan.setCompensationmethodology(CompensationMethodology.VOLUME);
		plan.setFromdate(LocalDate.now());
		plan.setTodate(LocalDate.now().plusDays(1));
		List<Method> methodList=new ArrayList<>();
		methodList.add(sampleMethod());
		plan.setMethods(methodList);
		return plan;
	}
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
		List<Plan> pList= new ArrayList<>();
		pList.add(samplePlan());
		user.setPlans(pList);
		return user;
	}
	private User user;
	private Plan plan;
	private Method method;
	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		user=sampleUser();
		plan=samplePlan();
		method=sampleMethod();
	}

	@Test
	public void testFindByUsername() {
		when(userRepository.findByUsername("username")).thenReturn(Optional.of(user));
		assertEquals(user.getPlans().get(0).getMethods().get(0).getPercentage(),service.findByUsername("username").get().getPlans().get(0).getMethods().get(0).getPercentage());
	}
	
	@Test
	public void testGetReport() {
		ExampleMatcher newPlanMatcher= ExampleMatcher.matchingAll().withIgnorePaths("userid");
		when(planRepository.findAll(Example.of(new Plan(),newPlanMatcher),Sort.by(Direction.ASC,"partnername"))).thenReturn(user.getPlans());
		assertEquals(0,service.getReport().size());
	}

	@Test
	public void testGetPlanByUser() {
		when(planRepository.findByPlanidAndUserid(plan.getPlanid(),user.getId())).thenReturn(Optional.of(plan));
		assertEquals(plan.getPartnername(),service.getPlanByUser(plan.getPlanid(), user.getId()).get().getPartnername());
	}

	@Test
	public void testGetMyPlans() {
		when(planRepository.findAllByUserid(user.getId(),Sort.by(Direction.ASC, "partnername"))).thenReturn(user.getPlans());
		assertEquals(plan.getCompensationplan(),service.getMyPlans(user.getId()).get(0).getCompensationplan());
	}

	@Test
	public void testFindByIdAndRole() {
		when(userRepository.findByIdAndRole(user.getId(),user.getRole())).thenReturn(Optional.of(user));
		assertEquals(user.getDepartment(),service.findByIdAndRole(user.getId(), user.getRole()).get().getDepartment());
	}

	@Test
	public void testFindAllByIgnoringUserId() {
		ExampleMatcher newPlanMatcher= ExampleMatcher.matchingAll().withIgnorePaths("userid");
		when(planRepository.findAll(Example.of(plan,newPlanMatcher))).thenReturn(user.getPlans());
		assertEquals(plan.getCompensationmethodology(),service.findAllByIgnoringUserId(plan).get(0).getCompensationmethodology());
	}

}
