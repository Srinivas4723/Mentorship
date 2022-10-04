package com.pcs.controller;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pcs.model.Plan;
import com.pcs.model.PlanTypes;
import com.pcs.repository.PlanRepository;
import com.pcs.response.PlanResponse;
import com.pcs.response.PlanType;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/plans/")
public class PlanController extends BaseController {
	
	@Autowired PlanRepository planRepository;
	
	/**
	 * Creates a plan Volume / Revenue
	 * @param plan
	 * @return
	 */
	@PostMapping("/createplan")
	public ResponseEntity<String> createPlan(@Valid @RequestBody Plan plan){
		List<Plan> existingplans=planRepository.findAllByPartnernameAndCompensationplanAndPlantype(plan.getPartnername(),plan.getCompensationplan(),plan.getPlantype());
		if(existingplans.isEmpty()) {
			planRepository.save(plan);
			return  ResponseEntity.ok("Plan Added Success");
		}
		return  ResponseEntity.badRequest().body("Partner Name and Plan already exists");
	}
	
	/**
	 * Deletes a plan Volume / Revenue
	 * @param plan
	 * @return
	 */
	@DeleteMapping("/deleteplan")
	public ResponseEntity<String> deletePlan(@Valid @RequestBody Plan plan){
		List<Plan> existingPlans=planRepository.findAllByPartnernameAndCompensationplanAndPlantype(plan.getPartnername(),plan.getCompensationplan(),plan.getPlantype());
		if(!existingPlans.isEmpty()) {
			planRepository.delete(existingPlans.get(0));
			return  ResponseEntity.ok("Plan Deleted Success");
		}
		return  ResponseEntity.badRequest().body("Partner Name and Plan Doesnot exists");
	}
	public void setProperties(PlanResponse planResponse,PlanType plantype,Plan plan) {
		
		planResponse.setPartnername(plan.getPartnername());
		planResponse.setCompensationplan(plan.getCompensationplan());
		
		plantype.setMinimum(plan.getMinimum());
		plantype.setMaximum(plan.getMaximum());
		plantype.setPercentage(plan.getPercentage());
//		Long pnvcount=planRepository.countByPartnernameAndPlantype(plan.getPartnername(),PlanTypes.VOLUME);
//		Long pnrcount=planRepository.countByPartnernameAndPlantype(plan.getPartnername(),PlanTypes.REVENUE);
//		planResponse.setPartnerNameCount(pnvcount>pnrcount?pnvcount:pnrcount);
//		Long cpvcount=planRepository.countByPartnernameAndCompensationplanAndPlantype(plan.getPartnername(),plan.getCompensationplan(),PlanTypes.VOLUME);
//		Long cprcount=planRepository.countByPartnernameAndCompensationplanAndPlantype(plan.getPartnername(),plan.getCompensationplan(),PlanTypes.REVENUE);
//		planResponse.setCompensationPlanCount(cpvcount>cprcount?cpvcount:cprcount);
	}
	@GetMapping("/findall")
	public List<PlanResponse> findAll(){
		
		List<Plan> volumePlans=planRepository.findAllByPlantype(PlanTypes.VOLUME);
		List<Plan> revenuePlans=planRepository.findAllByPlantype(PlanTypes.REVENUE);
		
		List<PlanResponse> planResponseList= new ArrayList<>();
		PlanResponse planResponse;
		PlanType plantype;
		
		if(volumePlans.isEmpty() && revenuePlans.isEmpty()) {
			return planResponseList;
		}
		for(Plan volumePlan: volumePlans) {
			planResponse=new PlanResponse();
			plantype=new PlanType();
			setProperties(planResponse,plantype,volumePlan);
			planResponse.setVolume(plantype);
			
			List<Plan> revenue=revenuePlans.stream().filter(x->x.getPartnername().equals(volumePlan.getPartnername()) 
					&& x.getCompensationplan().equals(volumePlan.getCompensationplan())).collect(Collectors.toList());
			
			if(!revenue.isEmpty()) {
				System.out.println(revenue.get(0).getMinimum()+"SDF "+planResponse.getVolume().getMinimum());
				plantype=new PlanType();
				plantype.setMinimum(revenue.get(0).getMinimum());
				plantype.setMaximum(revenue.get(0).getMinimum());
				plantype.setPercentage(revenue.get(0).getPercentage());
				revenuePlans.remove(revenue.get(0));
				planResponse.setRevenue(plantype);
			}
			else
				planResponse.setRevenue(new PlanType());
			System.out.println("VPX"+planResponse.getVolume().getMinimum());
			planResponseList.add(planResponse);
			
		}
		for(Plan revenueplan: revenuePlans) {
			planResponse=new PlanResponse();
			plantype=new PlanType();
			setProperties(planResponse,plantype,revenueplan);
			planResponse.setRevenue(plantype);
			planResponse.setVolume(new PlanType());
			planResponseList.add(planResponse);
			
		}
		for(PlanResponse planresponse: planResponseList) {
			Long pncount=planResponseList.stream().filter(x->x.getPartnername().equals(planresponse.getPartnername())).count();
			Long cpcount=planResponseList.stream().filter(x->x.getPartnername().equals(planresponse.getPartnername()) && x.getCompensationplan().equals(planresponse.getCompensationplan())).count();
			planresponse.setPartnerNameCount(pncount);
			planresponse.setCompensationPlanCount(cpcount);
		}
		//Comparator<PlanRespone> compareBy= Comparator.comparing(PlanResponse::getPartnername)
		return planResponseList.stream().sorted(Comparator.comparing(PlanResponse::getPartnername).thenComparing(PlanResponse::getCompensationplan)).collect(Collectors.toList());
//		System.out.println("SRINIVAS "+planResponseList.stream().map(x->x.getPartnername().equals).count()+"S "+planResponseList.stream().map(x->x.getCompensationplan()).count());
//		planResponseList.stream().map(x->x.getPartnername()).count();
		
	}
}
