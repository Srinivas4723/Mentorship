package com.pcs.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlanResponse {
	private String partnername;
	private String compensationplan;
	private PlanType volume;
	private PlanType revenue;
	private Long partnerNameCount;
	private Long compensationPlanCount;
}
