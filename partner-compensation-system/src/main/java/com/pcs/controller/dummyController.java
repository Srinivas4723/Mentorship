package com.pcs.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pcs.enums.Role;
import com.pcs.model.Method;
import com.pcs.model.Plan;
import com.pcs.model.User;
import com.pcs.repository.MethodRepository;
import com.pcs.repository.PlanRepository;
import com.pcs.repository.UserRepository;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/plans/")
public class dummyController extends BaseController{
	
	@Autowired PlanRepository planRepository;
	@Autowired MethodRepository methodRepository;
	@Autowired UserRepository userRepository;
	
	public Boolean isDateValid(LocalDate fromDate,LocalDate toDate) {
		return fromDate.isAfter(LocalDate.now())?toDate.isAfter(fromDate)?true:false:false;
	}
	public Boolean isMethodValid(Method addingmethod,List<Method> existingmethods) {
		Long am=addingmethod.getMinimum();
		Long ax=addingmethod.getMaximum();
		for(Method method:existingmethods) {
			Long vm=method.getMinimum();
			Long vx=method.getMaximum();
			if(vm<=am && am<=vx || vm<=ax && ax<=vx || ax<am) 
				return false;
		}
		return true;
	}
	
	private Plan isPlanValid(Plan newPlan,Long userid) {
		ExampleMatcher newPlanMatcher= ExampleMatcher.matchingAll().withIgnorePaths("userid");
		List<Plan> oldPlans= planRepository.findAll(Example.of(newPlan,newPlanMatcher));
		if(oldPlans.isEmpty()) {
			if(isDateValid(newPlan.getFromdate(),newPlan.getTodate()))
				return newPlan;
			return null;
		}
		else {
			List<Method> oldPlanMethods=new ArrayList<>();
			Set<Long> oldPlanUserids=new HashSet<>();
			int index = 0;
			for(int i=0;i<oldPlans.size();i++) {
				oldPlanMethods.addAll(oldPlans.get(i).getMethods());
				oldPlanUserids.add(oldPlans.get(i).getUserid());
				if(oldPlans.get(i).getUserid().equals(userid)) {
					index=i;
				}
			}
			if(isMethodValid(newPlan.getMethods().get(0),oldPlanMethods) 
					&& isDateValid(newPlan.getFromdate(),newPlan.getTodate())) {
				//if(!oldPlan.get().getUserid().equals(userid)) {
				if(!oldPlanUserids.contains(userid)) {
					newPlan.setUserid(userid);
					return newPlan;
				}
				oldPlans.get(index).getMethods().add(newPlan.getMethods().get(0));
				return oldPlans.get(index);
			}
		}
		return null;
	}
	@PostMapping("/createplan/user/{userid}")
	@PreAuthorize("hasRole('COMPENSATION')")
	public ResponseEntity<String> createPlan(@Valid @RequestBody Plan plan,@PathVariable Long userid){
		plan.setUserid(userid);
		Optional<User> existingUser= userRepository.findByIdAndRole(userid,Role.COMPENSATION);
		if(existingUser.isEmpty()){
			return ResponseEntity.badRequest().body("User Doesnot Exist");
		}
		else {
			Plan resultPlan=isPlanValid(plan,userid);
			if(resultPlan==null) {
				return ResponseEntity.badRequest().body("Invalid Plan");
			}
			else {
				existingUser.get().getPlans().add(resultPlan);
				userRepository.save(existingUser.get());
				return ResponseEntity.ok("Plan Added Success");
			}
			
		}
	}
	
	@GetMapping("/myplans/userid/{userid}")
	@PreAuthorize("hasRole('COMPENSATION')")
	public List<Plan> getMyPlans(@PathVariable Long userid){
		return planRepository.findAllByUserid(userid,Sort.by(Direction.ASC, "partnername"));
	}
	
	@DeleteMapping("/deleteplan/{userid}/{planid}/{methodindex}")
	@PreAuthorize("hasRole('COMPENSATION')")
	public ResponseEntity<String> deletePlan(@PathVariable Long userid,@PathVariable Long planid,@PathVariable int methodindex){
		Optional<Plan> existingPlan= planRepository.findByPlanidAndUserid(planid,userid);
		if(existingPlan.isPresent()) {
			Long methodId=existingPlan.get().getMethods().get(methodindex).getId();
			existingPlan.get().getMethods().remove(methodindex);
			if(existingPlan.get().getMethods().isEmpty()) {
				planRepository.delete(existingPlan.get());
				
			}
			methodRepository.deleteById(methodId);
			return ResponseEntity.ok().body("Plan Deleted Success");
			
		}
		return ResponseEntity.badRequest().body("Invalid Plan ID");
	}
//	@PostMapping("/createplan/user/{userid}")
//	@PreAuthorize("hasRole('COMPENSATION')")
//	public ResponseEntity<String> createPlan(@Valid @RequestBody Plan plan,@PathVariable Long userid){
//		Optional<User> existingUser= userRepository.findByIdAndRole(userid,Role.COMPENSATION);
//		String planStatus=null;
//		
//		if(existingUser.isPresent()){
//			Optional<Plan> existingPlan = planRepository.findOne(Example.of(plan));
//			if(existingPlan.isPresent()) {
//				planStatus= getPlanStatus(plan,existingPlan.get());
//				Long existingPlanId=existingPlan.get().getPlanid();
//				
//				switch(planStatus) {
//						
//				case "NOT_VALID":	return ResponseEntity.badRequest().body("Invalid Plan");
//				
//				case "VALID":  		
//									Optional<Plan> existingUserPlan = existingUser.get().getPlans() .stream().filter(x->x.getPlanid().equals(existingPlanId)).findFirst();
//									if(existingUserPlan.isPresent())
//										existingUserPlan.get().getMethods().add(plan.getMethods().get(0));
//									else
//										existingUser.get().getPlans().add(plan);
//									userRepository.save(existingUser.get());
//									return ResponseEntity.ok("Plan Added Success");
//				default:break;
//			}
//			}
//			else {
//				existingUser.get().getPlans().add(plan);
//				userRepository.save(existingUser.get());
//				return ResponseEntity.ok("Plan Added Success");
//			}
//			
//		}
//		return ResponseEntity.badRequest().body("User Doesnot Exist");
//			//hasUserContainsPlan(plan)
//			if(existingUser.get().getPlans().isEmpty()) {
//				List<Plan> plans =Arrays.asList(plan);
//				existingUser.get().setPlans(plans);
//				userRepository.save(existingUser.get());
//				return ResponseEntity.ok("Plan Added Success");
//			}
//			else {
//				List<Compensation> existingComp= compensationRepository.findAllByUserid(userid);
//				if(existingComp.isEmpty()) {
//					System.out.println("if");
//					Compensation comp= new Compensation();
//					comp.setUserid(userid);
//					comp.setPlans(Arrays.asList(plan));
//					compensationRepository.save(comp);
//					return ResponseEntity.ok("Plan Added Success1"); 
//				}
			//-------------------------------------
//				Optional<Plan> existingPlan= planRepository.findOne(Example.of(plan));
//				if(existingPlan.isEmpty()) {
//					System.out.println("if");
//					List<Plan> plans =Arrays.asList(plan);
//					 existingUser.get().getPlans().add(plan);
//					//existingUser.get().setPlans(plans);
//					userRepository.save(existingUser.get());
//					return ResponseEntity.ok("Plan Added Success1");
//				}
//				else if(isPlanValid(plan.getMethods().get(0), existingPlan.get().getMethods()) && isDateValid(plan.getFromdate(),plan.getTodate())){
//					System.out.println("else");
//					existingPlan.get().getMethods().add(plan.getMethods().get(0));
//					planRepository.save(existingPlan.get());
//					return ResponseEntity.ok("Plan Added Success2");
//				}
				//existingUser.get().getPlans().add(plan);
				
			//}
		
	
}
