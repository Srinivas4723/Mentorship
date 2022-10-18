package com.pcs.repository;

import java.util.List;
import java.util.Optional;

import org.hibernate.criterion.Distinct;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.pcs.enums.CompensationMethodology;
import com.pcs.enums.Report;
import com.pcs.model.Plan;

@Repository
public interface PlanRepository extends JpaRepository<Plan, Long> {
	Optional<Plan> findByPartnernameAndCompensationplanAndCompensationmethodology(String partnername,String compensationplan,CompensationMethodology compensationmethodology);

	List<Plan> findAllByUserid(Long userid, Sort sort);

	Optional<Plan> findByPlanidAndUserid(Long planid, Long userid);

	Optional<Plan> findByUseridAndPlanid(Long userid,Long planid);

	void deleteAllByUseridAndPlanid(Long userid, Long planid);

	void deleteByUseridAndPlanid(Long userid, Long planid);


	

	//List<Plan> findAll(ExampleMatcher newPlanMatcher,Sort sort);
	
//	@Query("Select partnername,compensationplan,compensationmethodology,fromdate,todate from plan")
//	List<Report> findAllPlans();
	//Optional<Plan> findByPlanid(Long planid);

	//List<Plan> findAllByUserid(Long userid, Sort by);

	//List<Plan> findAllByUserid(Long userid, Sort by);
	
}
