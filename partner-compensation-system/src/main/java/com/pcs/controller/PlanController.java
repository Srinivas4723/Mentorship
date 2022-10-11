//package com.pcs.controller;
//
//import java.time.LocalDate;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.Comparator;
//import java.util.List;
//import java.util.Optional;
//import java.util.stream.Collectors;
//
//import javax.validation.Valid;
//import javax.websocket.server.PathParam;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.domain.Example;
//import org.springframework.data.domain.ExampleMatcher;
//import org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers;
//import org.springframework.data.domain.Sort;
//import org.springframework.data.domain.Sort.Direction;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.annotation.Secured;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.web.bind.annotation.CrossOrigin;
//import org.springframework.web.bind.annotation.DeleteMapping;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.pcs.model.Method;
//import com.pcs.model.Plan;
//import com.pcs.model.User;
//import com.pcs.repository.MethodRepository;
//import com.pcs.repository.PlanRepository;
//import com.pcs.repository.UserRepository;
//import com.pcs.response.PlanResponse;
//import com.pcs.response.PlanType;
//
//@CrossOrigin(origins = "http://localhost:4200")
//@RestController
//@RequestMapping("/plans/")
//public class PlanController extends BaseController {
//	
//	@Autowired PlanRepository planRepository;
//	@Autowired MethodRepository methodRepository;
//	@Autowired UserRepository userRepository;
//	Logger logger=LoggerFactory.getLogger(PlanController.class);
//	
//	@DeleteMapping("/deleteplan/planid/{planid}/methodindex/{methodindex}")
//	public ResponseEntity<String> deletePlan1(@PathVariable Long planid,@PathVariable int methodindex){
//		Optional<Plan> existingPlan= planRepository.findById(planid);
//		if(existingPlan.isPresent()) {
//			Long methodId=existingPlan.get().getMethods().get(methodindex).getId();
//			existingPlan.get().getMethods().remove(methodindex);
//			if(existingPlan.get().getMethods().isEmpty()) {
//				planRepository.delete(existingPlan.get());
//				
//			}
//			methodRepository.deleteById(methodId);
//			return ResponseEntity.ok().body("Plan Deleted Success");
//			
//		}
//		return ResponseEntity.badRequest().body("Invalid Plan ID");
//	}
//	
//	/**
//	 * Deleting a Plan
//	 * @param <T>
//	 * @param method
//	 * @param planid
//	 * @return
//	 */
//	@DeleteMapping("/deleteplan")
//	public  ResponseEntity<String> deletePlan(@Valid @RequestBody Plan plan){
//		Optional<Plan> existingPlan=planRepository.findOne(Example.of(plan));
//		if(existingPlan.isPresent()) {
//			List<Method> hastoDeleteMethod= existingPlan.get().getMethods().stream().filter(method -> method.toString().equals(plan.getMethods().get(0).toString())).collect(Collectors.toList());
//			//System.out.println(existingPlan.get().getMethods().get(0).toString()+" : "+plan.ge);
//			if(hastoDeleteMethod.isEmpty()) {
//				return ResponseEntity.badRequest().body("Invalid Plan To Delete");
//			}
//			//existingPlan.get().getMethods().remove(hastoDeleteMethod.get(0));
//			if(existingPlan.get().getMethods().isEmpty()) {
////				planRepository.delete(existingPlan.get());
////				methodRepository.delete(hastoDeleteMethod.get(0));
//			}
//			else {
////				planRepository.save(existingPlan.get());
////				methodRepository.delete(hastoDeleteMethod.get(0));
//			}
//			return ResponseEntity.ok().body("Plan Deleted Success");
//		}
//		return  ResponseEntity.badRequest().body("Invalid Plan To Delete");
//	}
//	
//	public Boolean isDateValid(LocalDate fromDate,LocalDate toDate) {
//		return fromDate.isAfter(LocalDate.now())?toDate.isAfter(fromDate)?true:false:false;
//	}
//	public Boolean isPlanValid(Method addingmethod,List<Method> existingmethods) {
//		Long am=addingmethod.getMinimum();
//		Long ax=addingmethod.getMaximum();
//		for(Method method:existingmethods) {
//			Long vm=method.getMinimum();
//			Long vx=method.getMaximum();
//			if(vm<=am && am<=vx || vm<=ax && ax<=vx || ax<am) 
//				return false;
//		}
//		return true;
//	}
//	/**
//	 * Creating a Plan
//	 * @param plan
//	 * @return
//	 */
//	@PostMapping("/createplan/user/{userid}")
//	@PreAuthorize("hasRole('COMPENSATION')")
//	public ResponseEntity<String> createPlan(@Valid @RequestBody Plan plan,@PathVariable Long userid){
//		plan.setUserid(userid);
//		ExampleMatcher planidMatcher= ExampleMatcher.matchingAll()
//	      .withIgnorePaths("userid").withIgnorePaths("planid");
//		
//		//Optional<Plan> existingPlan=planRepository.findOne(Example.of(plan,planidMatcher));
//		List<Plan> existingPlans=planRepository.findAll(Example.of(plan,planidMatcher));
//		List<Method> existingMethods=new ArrayList<>();
//		for(Plan eplan:existingPlans) {
//			existingMethods.addAll(eplan.getMethods());
//		}
//		Method method=plan.getMethods().get(0);
//		if(existingPlans.isEmpty()) {
//			if(method.getMinimum()<=method.getMaximum() &&		
//				isDateValid(plan.getFromdate(),plan.getTodate())) {
//				
//				planRepository.save(plan);
//				return ResponseEntity.ok().body("Plan Added Success");
//			}
//			else {
//				return ResponseEntity.badRequest().body("Invalid Plan To Add");
//			}
//		}
//		//else if(isPlanValid(plan.getMethods().get(0), existingPlan.get().getMethods()) && isDateValid(plan.getFromdate(),plan.getTodate())){
//		else if(isPlanValid(plan.getMethods().get(0), existingMethods) && isDateValid(plan.getFromdate(),plan.getTodate())){
//			Optional<Plan> existingPlan= existingPlans.stream().filter(x->x.getUserid().equals(userid)).findAny();
//			if(existingPlan.isEmpty()) {
//				planRepository.save(plan);
//				return ResponseEntity.ok().body("Plan Added Success");
//			}
//			else {
//				existingPlan.get().getMethods().add(plan.getMethods().get(0));
//				planRepository.save(existingPlan.get());
//				return ResponseEntity.ok().body("Plan Added Success");
//			}
//			
////			if(!existingPlan.get().getUserid().equals(userid)) {
////				plan.setUserid(userid);
////				planRepository.save(plan);
////				return ResponseEntity.ok().body("Plan Added Success");
////			}
//			
//		}
//		else {
//			return ResponseEntity.badRequest().body("Invalid Plan to add2");
//		}
//		
//	}
//	/**
//	 * Get All Data
//	 * @return
//	 */
//	@GetMapping("/report")
//	@PreAuthorize("hasRole('REPORT')")
//	public List<Plan> getReport(){
//		return planRepository.findAll(Sort.by(Direction.ASC, "partnername"));
//	}
//	/**
//	 * Get All Data
//	 * @return
//	 */
//	@GetMapping("/myplans/userid/{userid}")
//	@PreAuthorize("hasRole('COMPENSATION')")
//	public List<Plan> getMyPlans(@PathVariable Long userid){
//		return planRepository.findAllByUserid(userid,Sort.by(Direction.ASC, "partnername"));
//				
//	}
//	
////	@GetMapping("/findall")
////	public List<PlanResponse> findAll(){
////		List<PlanResponse> planResponses=new ArrayList<>();
////		List<Plan> plans= planRepository.findAll(Sort.by(Direction.ASC, "partnername"));
////		for(Plan plan:plans) {
////			for(Method method:plan.getMethods()) {
////				PlanResponse planResponse=new PlanResponse();
////				planResponse.setPartnername(plan.getPartnername());
////				planResponse.setCompensationplan(plan.getCompensationplan());
////				planResponse.setCompensationmethodology(plan.getCompensationmethodology());
////				planResponse.setFromdate(plan.getFromdate());
////				planResponse.setTodate(plan.getTodate());
////				planResponse.setMinimum(method.getMinimum());
////				planResponse.setMaximum(method.getMaximum());
////				planResponse.setPercentage(method.getPercentage());
////				planResponses.add(planResponse);
////			}
////		}
////		return planResponses;
////	}
////	
////	/**
////	 * Creates a plan Volume / Revenue
////	 * @param plan
////	 * @return
////	 */
////	@PostMapping("/createplan")
////	public ResponseEntity<String> createPlan(@Valid @RequestBody Plan plan){
////		List<Plan> existingplans=planRepository.findAllByPartnernameAndCompensationplanAndPlantype(plan.getPartnername(),plan.getCompensationplan(),plan.getPlantype());
////		if(existingplans.isEmpty()) {
////			planRepository.save(plan);
////			return  ResponseEntity.ok("Plan Added Success");
////		}
////		return  ResponseEntity.badRequest().body("Partner Name and Plan already exists");
////	}
////	
////	/**
////	 * Deletes a plan Volume / Revenue
////	 * @param plan
////	 * @return
////	 */
////	@DeleteMapping("/deleteplan")
////	public ResponseEntity<String> deletePlan(@Valid @RequestBody Plan plan){
////		List<Plan> existingPlans=planRepository.findAllByPartnernameAndCompensationplanAndPlantype(plan.getPartnername(),plan.getCompensationplan(),plan.getPlantype());
////		if(!existingPlans.isEmpty()) {
////			planRepository.delete(existingPlans.get(0));
////			return  ResponseEntity.ok("Plan Deleted Success");
////		}
////		return  ResponseEntity.badRequest().body("Partner Name and Plan Doesnot exists");
////	}
//////	public void setProperties(PlanResponse planResponse,PlanType plantype,Plan plan) {
//////		
//////		planResponse.setPartnername(plan.getPartnername());
//////		planResponse.setCompensationplan(plan.getCompensationplan());
//////		
//////		plantype.setMinimum(plan.getMinimum());
//////		plantype.setMaximum(plan.getMaximum());
//////		plantype.setPercentage(plan.getPercentage());
////////		Long pnvcount=planRepository.countByPartnernameAndPlantype(plan.getPartnername(),PlanTypes.VOLUME);
////////		Long pnrcount=planRepository.countByPartnernameAndPlantype(plan.getPartnername(),PlanTypes.REVENUE);
////////		planResponse.setPartnerNameCount(pnvcount>pnrcount?pnvcount:pnrcount);
////////		Long cpvcount=planRepository.countByPartnernameAndCompensationplanAndPlantype(plan.getPartnername(),plan.getCompensationplan(),PlanTypes.VOLUME);
////////		Long cprcount=planRepository.countByPartnernameAndCompensationplanAndPlantype(plan.getPartnername(),plan.getCompensationplan(),PlanTypes.REVENUE);
////////		planResponse.setCompensationPlanCount(cpvcount>cprcount?cpvcount:cprcount);
//////	}
////	@GetMapping("/findall")
////	public List<PlanResponse> findAll(){
////		
////		List<Plan> volumePlans=planRepository.findAllByPlantype(CompensationMethodology.VOLUME);
////		List<Plan> revenuePlans=planRepository.findAllByPlantype(CompensationMethodology.REVENUE);
////		
////		List<PlanResponse> planResponseList= new ArrayList<>();
////		PlanResponse planResponse;
////		PlanType plantype;
////		
////		if(volumePlans.isEmpty() && revenuePlans.isEmpty()) {
////			return planResponseList;
////		}
////		for(Plan volumePlan: volumePlans) {
////			planResponse=new PlanResponse();
////			plantype=new PlanType();
////			//setProperties(planResponse,plantype,volumePlan);
////			planResponse.setPartnername(volumePlan.getPartnername());
////			planResponse.setCompensationplan(volumePlan.getCompensationplan());
////			plantype.setMinimum(volumePlan.getMinimum());
////			plantype.setMaximum(volumePlan.getMaximum());
////			plantype.setPercentage(volumePlan.getPercentage());
////			planResponse.setVolume(plantype);
////			
////			List<Plan> revenue=revenuePlans.stream().filter(x->x.getPartnername().equals(volumePlan.getPartnername()) 
////					&& x.getCompensationplan().equals(volumePlan.getCompensationplan())).collect(Collectors.toList());
////			
////			if(!revenue.isEmpty()) {
////				System.out.println(revenue.get(0).getMinimum()+"SDF "+planResponse.getVolume().getMinimum());
////				plantype=new PlanType();
////				plantype.setMinimum(revenue.get(0).getMinimum());
////				plantype.setMaximum(revenue.get(0).getMinimum());
////				plantype.setPercentage(revenue.get(0).getPercentage());
////				revenuePlans.remove(revenue.get(0));
////				planResponse.setRevenue(plantype);
////			}
////			else
////				planResponse.setRevenue(new PlanType());
////			System.out.println("VPX"+planResponse.getVolume().getMinimum());
////			planResponseList.add(planResponse);
////			
////		}
////		for(Plan revenueplan: revenuePlans) {
////			planResponse=new PlanResponse();
////			plantype=new PlanType();
////			//setProperties(planResponse,plantype,revenueplan);
////			planResponse.setPartnername(revenueplan.getPartnername());
////			planResponse.setCompensationplan(revenueplan.getCompensationplan());
////			plantype.setMinimum(revenueplan.getMinimum());
////			plantype.setMaximum(revenueplan.getMaximum());
////			plantype.setPercentage(revenueplan.getPercentage());
////			planResponse.setRevenue(plantype);
////			planResponse.setVolume(new PlanType());
////			planResponseList.add(planResponse);
////			
////		}
////		
////		//Comparator<PlanRespone> compareBy= Comparator.comparing(PlanResponse::getPartnername)
////		return planResponseList.stream().sorted(Comparator.comparing(PlanResponse::getPartnername).thenComparing(PlanResponse::getCompensationplan)).collect(Collectors.toList());
//////		System.out.println("SRINIVAS "+planResponseList.stream().map(x->x.getPartnername().equals).count()+"S "+planResponseList.stream().map(x->x.getCompensationplan()).count());
//////		planResponseList.stream().map(x->x.getPartnername()).count();
////		
////	}
//}
