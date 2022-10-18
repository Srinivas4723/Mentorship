package com.pcs.controller;

import static org.junit.Assert.assertEquals;
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
import org.springframework.http.ResponseEntity;

import com.pcs.enums.CompensationMethodology;
import com.pcs.enums.Department;
import com.pcs.enums.JobTitle;
import com.pcs.enums.Location;
import com.pcs.enums.Role;
import com.pcs.model.Method;
import com.pcs.model.Plan;
import com.pcs.model.User;
import com.pcs.repository.MethodRepository;
import com.pcs.repository.PlanRepository;
import com.pcs.repository.UserRepository;
import com.pcs.service.PcsService;

public class planControllerTest {
	
	@InjectMocks PlanController planController;
	@Mock UserRepository userRepository;
	@Mock PlanRepository planRepository;
	@Mock MethodRepository methodRepository;
	@Mock PcsService service;
	
	private User user;
	private Plan plan;
	private Method method;
	private List<Plan> planList;
	/**
	 * 
	 * @return
	 */
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
		return user;
	}
	@Before
	public void setUp() throws Exception {
		 MockitoAnnotations.initMocks(this);
		    user=sampleUser();
		    plan=samplePlan();
		    method=sampleMethod();
		    planList = new ArrayList<>();
			planList.add(plan);
		   // loginRequest=sampleLoginRequest();
		    //adminResponse=sampleAdminResponse();
	}
	
	@Test
	public void testGetReport() {
		
		when(service.getReport()).thenReturn(planList);
		assertEquals(1,planController.getReport().get(0).getSno());
	}
	
	@Test
	public void testIsPlanValid() {
		List<Plan> pList= new ArrayList<>();
		Plan newPlan= samplePlan();
		newPlan.setPlanid(Long.valueOf(1));
		newPlan.setUserid(Long.valueOf(1));
		List<Method> mList=new ArrayList<>();
		Method newMethod = sampleMethod();
		newMethod.setMinimum(Long.valueOf(5));
		newMethod.setMaximum(Long.valueOf(6));
		mList.add(newMethod);
		//mList.add(method);
		newPlan.setMethods(mList);
		pList.add(newPlan);
		
		List<Plan> opList= new ArrayList<>();
		Plan oldPlan= samplePlan();
		oldPlan.setPlanid(Long.valueOf(1));
		oldPlan.setUserid(Long.valueOf(1));
		List<Method> omList=new ArrayList<>();
		Method oldMethod = sampleMethod();
		oldMethod.setMinimum(Long.valueOf(1));
		oldMethod.setMaximum(Long.valueOf(2));
		omList.add(oldMethod);
		//mList.add(method);
		oldPlan.setMethods(omList);
		opList.add(oldPlan);
		
		when(service.findAllByIgnoringUserId(newPlan)).thenReturn(opList);
		assertEquals(oldPlan.getPlanid(),planController.isPlanValid(newPlan, Long.valueOf(2),new ArrayList<Integer>()).getPlanid());
		assertEquals(oldPlan.getPlanid(),planController.isPlanValid(newPlan, Long.valueOf(1),new ArrayList<Integer>()).getPlanid());
	}
	@Test
	public void testCreatePlanWhenEmpty() {
		User newUser=sampleUser();
		newUser.setPlans(new ArrayList<Plan>());
		when(service.findByIdAndRole(user.getId(),Role.COMPENSATION)).thenReturn(Optional.of(newUser));
		assertEquals(ResponseEntity.ok("Plan Added Success"),planController.createPlan(plan, user.getId()));
	}
//	@Test
//	public void testCreatePlanWhenValidPlan() {
//		when(service.findByIdAndRole(Long.valueOf(2),Role.COMPENSATION)).thenReturn(Optional.of(user));
//		//plan.setFromdate(LocalDate.of(2000,10,2));
//		when(service.findAllByIgnoringUserId(plan)).thenReturn(planList);
//		assertEquals(ResponseEntity.badRequest().body("Invalid Plan"),planController.createPlan(plan, user.getId()));
//	}
	@Test
	public void testCreatePlanWhenInvalidPlan() {
		List<Plan> pList= new ArrayList<>();
		Plan newPlan= samplePlan();
		newPlan.setPlanid(Long.valueOf(1));
		newPlan.setUserid(Long.valueOf(1));
		List<Method> mList=new ArrayList<>();
		mList.add(method);
		//mList.add(method);
		newPlan.setMethods(mList);
		pList.add(newPlan);
		user.setId(Long.valueOf(1));
		when(service.findByIdAndRole(user.getId(),Role.COMPENSATION)).thenReturn(Optional.ofNullable(user));
		when(service.findAllByIgnoringUserId(newPlan)).thenReturn(pList);
		assertEquals(ResponseEntity.badRequest().body("Invalid Plan"),planController.createPlan(newPlan, user.getId()));
	}
	
	@Test
	public void testCreatePlanWhenNewUser() {
		when(service.findByIdAndRole(user.getId(),Role.COMPENSATION)).thenReturn(Optional.empty());
		assertEquals(ResponseEntity.badRequest().body("User Doesnot Exist"),planController.createPlan(plan, user.getId()));
	}

	@Test
	public void testGetMyPlans() {
		when(service.getMyPlans(user.getId())).thenReturn(planList);
		assertEquals(plan.getPlanid(),planController.getMyPlans(user.getId()).get(0).getPlanid());
	}
	@Test
	public void testDeletePlanWithInvalidPlan() {
		when(service.getPlanByUser(user.getId(),plan.getPlanid())).thenReturn(Optional.empty());
		assertEquals(ResponseEntity.badRequest().body("Invalid Plan ID"),planController.deleteMethod(user.getId(), plan.getPlanid(),0));
	}
	
	@Test
	public void testDeletePlanWhenMethodsPresent() {
		Plan newPlan= samplePlan();
		List<Method> mList=new ArrayList<>();
		mList.add(method);
		mList.add(method);
		newPlan.setMethods(mList);
		when(service.getPlanByUser(user.getId(),plan.getPlanid())).thenReturn(Optional.of(newPlan));
		assertEquals(ResponseEntity.ok().body("Plan Deleted Success"),planController.deleteMethod(user.getId(), plan.getPlanid(),0));
	}
	@Test
	public void testDeletePlanWhenMethodsEmpty() {
		when(service.getPlanByUser(user.getId(),plan.getPlanid())).thenReturn(Optional.of(plan));
		assertEquals(ResponseEntity.ok().body("Plan Deleted Success"),planController.deleteMethod(user.getId(), plan.getPlanid(),0));
	}

	

}
