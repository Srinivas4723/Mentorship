package com.pcs.repository;

import java.util.List;
import java.util.Optional;

import javax.validation.constraints.NotNull;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pcs.model.Plan;
import com.pcs.model.PlanTypes;

@Repository
public interface PlanRepository extends JpaRepository<Plan, Long> {
	
	List<Plan> findAllByPartnernameAndCompensationplanAndPlantype(String partnername,String compensationplan,PlanTypes plantype);

	List<Plan> findAllByPlantype(PlanTypes revenue);
	
	Long countByPartnernameAndCompensationplanAndPlantype(String partnername,String compensationplan,PlanTypes plantype);


	Long countByPartnernameAndPlantype(String partnername,PlanTypes plantype);

	Long countByCompensationplanAndPlantype(String compensationplan, PlanTypes volume);
}
