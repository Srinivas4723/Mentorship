package com.pcs.response;

import com.pcs.enums.Department;
import com.pcs.enums.JobTitle;
import com.pcs.enums.Location;
import com.pcs.enums.Role;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminResponse {
	private Long empid;
	private String firstname;
	private String lastname;
	private Location location;
	private JobTitle jobtitle;
	private Department department;
	private Role role;
	
}
