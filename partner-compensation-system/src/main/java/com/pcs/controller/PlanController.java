package com.pcs.controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
import com.pcs.response.ReportResponse;
import com.pcs.service.PcsService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/plans/")
public class PlanController extends BaseController{
	
	@Autowired PlanRepository planRepository;
	@Autowired MethodRepository methodRepository;
	@Autowired UserRepository userRepository;
	@Autowired PcsService service;
	
	/**
	 * Verifies Method is valid or not
	 * @param addingmethod
	 * @param existingmethods
	 * @return
	 */
	public Boolean isMethodValid(Method addingmethod,List<Method> existingmethods) {
		Long am=addingmethod.getMinimum();
		Long ax=addingmethod.getMaximum();
		for(Method method:existingmethods) {
			Long vm=method.getMinimum();
			Long vx=method.getMaximum();
			System.out.println(""+am+ax+vm+vx);
			if(vm<=am && am<=vx || vm<=ax && ax<=vx || ax<am) 
				return false;
		}
		return true;
	}
	/**
	 * Adding a method to existing Plan
	 * @param method
	 * @param userid
	 * @param planid
	 * @return
	 */
	@PostMapping("/addmethod/{userid}/{planid}")
	@PreAuthorize("hasRole('COMPENSATION')")
	public ResponseEntity<?> addMethod(@Valid @RequestBody Method method,@PathVariable Long userid,@PathVariable Long planid){
		//Optional<User> oldUser= userRepository.findById(userid);
		List<Method> mList= methodRepository.findByPlanid(planid);
		if(isMethodValid(method,mList)) {
			Optional<Plan> oldPlan= planRepository.findByUseridAndPlanid(userid,planid);
			oldPlan.get().getMethods().add(method);
			planRepository.save(oldPlan.get());
			return ResponseEntity.ok("PLan Added Success");
		}
		return ResponseEntity.badRequest().body("Invalid Method");
	}
	/**
	 * Verifies Plan is Valid
	 * @param newPlan
	 * @param userid
	 * @param failedIndexes
	 * @return
	 */
	public Plan isPlanValid(Plan newPlan,Long userid,List<Integer> failedIndexes) {
		
		List<Plan> oldPlans= service.findAllByIgnoringUserId(newPlan);
		if(oldPlans.isEmpty()) {
			
			for(int i=0;i<newPlan.getMethods().size();i++) {
				List<Method> restMethods= new ArrayList<>();
				restMethods.addAll(newPlan.getMethods());
				restMethods.remove(i);
				if(!isMethodValid(newPlan.getMethods().get(i),restMethods) ) {
					failedIndexes.add(i);
				}
			}
			if(failedIndexes.isEmpty()) {
				
					return newPlan;
				}
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
			
			for(int i=0;i<newPlan.getMethods().size();i++) {
				if(!isMethodValid(newPlan.getMethods().get(i),oldPlanMethods) ) {
					failedIndexes.add(i);
				}
			}
			if(failedIndexes.isEmpty()) {
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
	/**
	 * creating a new Plan
	 * @param plan
	 * @param userid
	 * @return
	 */
	@PostMapping("/createplan/user/{userid}")
	@PreAuthorize("hasRole('COMPENSATION')")
	public ResponseEntity<?> createPlan(@Valid @RequestBody Plan plan,@PathVariable Long userid){
		plan.setUserid(userid);
		List<Integer> failedIndexes=new ArrayList<>();
		Optional<User> existingUser= service.findByIdAndRole(userid,Role.COMPENSATION);
		if(existingUser.isEmpty()){
			return ResponseEntity.badRequest().body("User Doesnot Exist");
		}
		else {
			Plan resultPlan=isPlanValid(plan,userid,failedIndexes);
			if(resultPlan==null) {
				return ResponseEntity.badRequest().body(failedIndexes);
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
		//return planRepository.findAllByUserid(userid,Sort.by(Direction.ASC, "partnername"));
		return service.getMyPlans(userid);
	}
	
	@DeleteMapping("/deletemethod/{userid}/{planid}/{methodindex}")
	@PreAuthorize("hasRole('COMPENSATION')")
	public ResponseEntity<String> deleteMethod(@PathVariable Long userid,@PathVariable Long planid,@PathVariable int methodindex){
		//Optional<Plan> existingPlan= planRepository.findByPlanidAndUserid(planid,userid);
		Optional<Plan> existingPlan= service.getPlanByUser(planid,userid);
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
	@DeleteMapping("/deleteplan/{userid}/{planid}")
	@PreAuthorize("hasRole('COMPENSATION')")
	public ResponseEntity<String> deletePlan(@PathVariable Long userid,@PathVariable Long planid){
		Optional<Plan> existingPlan= service.getPlanByUser(planid,userid);
		if(existingPlan.isPresent()) {
			List<Long> mids= existingPlan.get().getMethods().stream().map(x->x.getId()).collect(Collectors.toList());
			//existingPlan.get().setMethods(new ArrayList<Method>());
			methodRepository.deleteAllById(mids);
			planRepository.delete(existingPlan.get());
			//methodRepository.deleteAllByPlanid(Long.valueOf(4));
			return ResponseEntity.ok().body("Plan Deleted Success");
			
		}
		return ResponseEntity.badRequest().body("Invalid Plan ID");
	}
	
	@GetMapping("/report")
	@PreAuthorize("hasRole('REPORT')")
	public List<ReportResponse> getReport(){
		//ExampleMatcher newPlanMatcher= ExampleMatcher.matchingAll().withIgnorePaths("userid");
		 //List<Plan> plans=planRepository.findAll(Example.of(new Plan(),newPlanMatcher),Sort.by(Direction.ASC,"partnername"));
		List<Plan> plans= service.getReport(); 
		List<ReportResponse> reportResponses= new ArrayList<>();
		 ReportResponse reportResponse= null;
		 for(int i=0;i<plans.size();i++) {
			 reportResponse=new ReportResponse();
			 reportResponse.setSno(i+1);
			 reportResponse.setPartnername(plans.get(i).getPartnername());
			 reportResponse.setCompensationplan(plans.get(i).getCompensationplan());
			 reportResponse.setCompensationmethodology(plans.get(i).getCompensationmethodology());
			 reportResponse.setFromdate(plans.get(i).getFromdate());
			 reportResponse.setTodate(plans.get(i).getTodate());
			 reportResponses.add(reportResponse);
		 };
		 return reportResponses;
	}
}
