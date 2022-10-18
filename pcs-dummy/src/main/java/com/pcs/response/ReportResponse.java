package com.pcs.response;

import java.time.LocalDate;

import com.pcs.enums.CompensationMethodology;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReportResponse {
	private int sno;
	private String partnername;
	private String compensationplan;
	private CompensationMethodology compensationmethodology;
	private LocalDate fromdate;
	private LocalDate todate;
}
