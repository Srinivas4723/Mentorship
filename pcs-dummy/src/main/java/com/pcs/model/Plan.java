package com.pcs.model;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


import com.pcs.enums.CompensationMethodology;

import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
@Table(	name = "plan")
public class Plan {
	
	@Id
	@GeneratedValue
	private Long planid;
	
	@NotBlank(message = "Partner Name Cannot not be Blank")
	@Size(min = 3, message = "Minimum size id 3")
	private String partnername;
	
	@NotBlank(message = "Compesation Plan Cannot not be Blank")
	@Size(min = 3, message = "Minimum size id 3")
	private String compensationplan;
	
	@Enumerated(EnumType.STRING)
	@NotNull(message="PlanType Cannot be Blank")
	private CompensationMethodology compensationmethodology;
	
	@NotNull(message="From Data Cannot be Null")
	private LocalDate fromdate;
	
	@NotNull(message="From Data Cannot be Null")
	private LocalDate todate;
	
	@OneToMany(cascade=CascadeType.ALL)
	@JoinColumn(name="planid",referencedColumnName="planid")
	private List<@Valid Method> methods;
	
//	@ManyToMany(targetEntity=Compensation.class,cascade=CascadeType.ALL)
//	@JoinColumn(name="planid",referencedColumnName="userid")
	private Long userid;
	
}
