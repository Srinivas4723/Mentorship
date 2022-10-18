package com.pcs.repository;

import java.util.List;

import javax.validation.constraints.NotNull;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pcs.model.Method;
import com.pcs.model.Plan;

@Repository
public interface MethodRepository extends JpaRepository<Method, Long> {

	List<Method> findByPlanid(Long planid);

	void deleteAllByPlanid(Long planid);

	
	
//	List<Method> findAllByMinimumAndMaximumAndPlanid( Long minimum,
//			Long maximum, Long planid);
	
	
}
