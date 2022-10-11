package com.pcs.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pcs.enums.CompensationMethodology;
import com.pcs.model.Plan;

@Repository
public interface PlanRepository extends JpaRepository<Plan, Long> {
	Optional<Plan> findByPartnernameAndCompensationplanAndCompensationmethodology(String partnername,String compensationplan,CompensationMethodology compensationmethodology);

	List<Plan> findAllByUserid(Long userid, Sort sort);

	Optional<Plan> findByPlanidAndUserid(Long planid, Long userid);

	//Optional<Plan> findByPlanid(Long planid);

	//List<Plan> findAllByUserid(Long userid, Sort by);

	//List<Plan> findAllByUserid(Long userid, Sort by);
	
}
