package com.pcs.model;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SecondaryTable;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
@Table(	name = "Plans")
public class Plan {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int planid;
	
	@NotBlank(message = "Partner Name Cannot not be Blank")
	@Size(min = 3, message = "Minimum size id 3")
	private String partnername;
	
	@NotBlank(message = "Compesation Plan Cannot not be Blank")
	@Size(min = 3, message = "Minimum size id 3")
	private String compensationplan;
	
	@Enumerated(EnumType.STRING)
	@NotNull(message="PlanType Cannot be Blank")
	private PlanTypes plantype;
	
	@NotNull(message = "minimum value cannot be null")
	@Min(value = 1, message = "minimum value cannot be less than Zero")
	private int minimum;

	@NotNull(message = "maximum value cannot be null")
	@Min(value = 1, message = "Volume - maximum value cannot be less than One")
	private int maximum;

	@NotNull(message = "percentage value cannot be null")
	@Min(value = 1, message = "percentage value cannot be less than Zero")
	private int percentage;
	
//	@NotNull(message = "Revenue - minimum value cannot be null")
//	@Min(value = 0, message = "Revenue - minimum value cannot be less than Zero")
//	private int revenuemin;
//
//	@NotNull(message = "Revenue - maximum value cannot be null")
//	@Min(value = 1, message = "Revenue - maximum value cannot be less than One")
//	private int revenuemax;
//
//	@NotNull(message = "Revenue - percentage value cannot be null")
//	@Min(value = 0, message = "Revenue - percentage value cannot be less than zero")
//	private int revenuepercent;
	
//	@NotNull(message="From Date cannot be Blank")
//	private LocalDate fromdate;
//	
//	@NotNull(message="To Date cannot be Blank")
//	private LocalDate todate;

}
