package com.pcs.response;

import java.time.LocalDate;

import com.pcs.enums.CompensationMethodology;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlanResponse {
	private String partnername;
	private String compensationplan;
	private CompensationMethodology compensationmethodology;
	private LocalDate fromdate;
	private LocalDate todate;
	private Long minimum;
	private Long maximum;
	private Long percentage;
}
