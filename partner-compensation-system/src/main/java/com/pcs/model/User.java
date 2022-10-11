package com.pcs.model;

import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.pcs.controller.BaseController;
import com.pcs.enums.Department;
import com.pcs.enums.JobTitle;
import com.pcs.enums.Location;
import com.pcs.enums.Role;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(	name = "user")
public class User extends BaseController {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotBlank(message="First Name Cannot be Blank")
	@Size(min=3,message="Minimum size is 3")
	private String firstname;
	
	@NotBlank(message="First Name Cannot be Blank")
	@Size(min=3,message="Minimum size is 3")
	private String lastname;
	
	@NotBlank(message="User Name Cannot be Blank")
	@Size(min=3,message="Minimum size is 3")
	private String username;
	
	@NotBlank(message="Password Name Cannot be Blank")
	@Size(max = 120)
	private String password;
	
	@Enumerated(EnumType.STRING)
	@NotNull(message="Location Cannot be Blank")
	private Location location;
	
	@Enumerated(EnumType.STRING)
	@NotNull(message="Job Title Cannot be Blank")
	private JobTitle jobtitle;
	
	@Enumerated(EnumType.STRING)
	@NotNull(message="Department Cannot be Blank")
	private Department department;
	
	@Enumerated(EnumType.STRING)
	@NotNull(message="Role Cannot be Blank")
	private Role role;
	
//	@OneToMany(cascade=CascadeType.ALL)
//	@JoinColumn(name="id",referencedColumnName="userid")
//	private List<Plan> plans;
	@OneToMany(cascade=CascadeType.ALL)
	@JoinColumn(name="userid",referencedColumnName="id")
	private List<Plan> plans;
	
//	@ManyToMany(cascade=CascadeType.ALL)
//	@JoinColumn(name="userid",referencedColumnName="planid")
//	private List<Method> methods;
}
