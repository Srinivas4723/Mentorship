package com.pcs.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.pcs.enums.Role;
import com.pcs.model.Plan;
import com.pcs.model.User;
import com.pcs.repository.PlanRepository;
import com.pcs.repository.UserRepository;

@Service
public class PcsService {
	@Autowired UserRepository userRepository;
	@Autowired PlanRepository planRepository;
	
	public Optional<User> findByUsername(String username){
		return userRepository.findByUsername(username);
	}
		
	public List<Plan> getReport(){
		Plan plan= new Plan();
		ExampleMatcher newPlanMatcher= ExampleMatcher.matchingAll().withIgnorePaths("userid");
		return planRepository.findAll(Example.of(plan,newPlanMatcher),Sort.by(Direction.ASC,"partnername"));
	}

	public Optional<Plan> getPlanByUser(Long planid,Long userid) {
		return planRepository.findByPlanidAndUserid(planid,userid);
	}

	public List<Plan> getMyPlans(Long userid) {
		return planRepository.findAllByUserid(userid,Sort.by(Direction.ASC, "partnername"));
	}

	public Optional<User> findByIdAndRole(Long userid, Role role) {
		return userRepository.findByIdAndRole(userid,role);
	}

	public List<Plan> findAllByIgnoringUserId(Plan plan) {
		ExampleMatcher newPlanMatcher= ExampleMatcher.matchingAll().withIgnorePaths("userid");
		return planRepository.findAll(Example.of(plan,newPlanMatcher));
	}
}
