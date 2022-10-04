package com.pcs.model;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int empid;
	
	@NotBlank(message="First Name Cannot be Blank")
	@Size(min=3,message="Minimum size is 3")
	private String firstname;
	
	@NotBlank(message="First Name Cannot be Blank")
	@Size(min=3,message="Minimum size is 3")
	private String lastname;
	
	@NotNull(message="Location Cannot be Blank")
	@Enumerated(EnumType.STRING)
	private Location location;
	
	@NotNull(message="Job Title Cannot be Blank")
	@Enumerated(EnumType.STRING)
	private JobTitle jobtitle;
	
	@NotNull(message="Department Cannot be Blank")
	@Enumerated(EnumType.STRING)
	private Department department;
	
	@NotNull(message="Role Cannot be Blank")
	@Enumerated(EnumType.STRING)
	private Role role;
}
