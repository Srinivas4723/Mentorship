package com.pcs.security.response;

import java.util.List;
import java.util.Optional;

import javax.validation.constraints.NotNull;

import com.pcs.enums.Department;
import com.pcs.enums.JobTitle;
import com.pcs.enums.Location;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JwtResponse {
	private String accesstoken;
	private String type = "Bearer";
	private Long id;
	private String username;
	private String firstname;
	private String lastname;
	private Location location;
	private JobTitle jobtitle;
	private Department department;
	private String role;

	public JwtResponse(String accessToken, Long id, String username, String firstname, String lastname,Location location, JobTitle jobtitle,
			Department department, String role) {
		this.accesstoken=accessToken;
		this.id=id;
		this.firstname=firstname;
		this.username=username;this.location=location;
		this.lastname=lastname;this.jobtitle=jobtitle;
		this.department=department;this.role=role;
		
	}
	
}
