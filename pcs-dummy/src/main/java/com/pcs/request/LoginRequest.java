package com.pcs.request;

import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {
	@NotBlank(message="User Name Cannot be blank")
	private String username;
	@NotBlank(message="User Name Cannot be blank")
	private String password;
}
