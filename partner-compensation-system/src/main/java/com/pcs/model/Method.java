package com.pcs.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
@Table(	name = "method")
public class Method {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
		
	@NotNull(message = "minimum value cannot be null")
	@Min(value = 1, message = "minimum value cannot be less than Zero")
	private Long minimum;
	
	@NotNull(message = "maximum value cannot be null")
	@Min(value = 1, message = "Vmaximum value cannot be less than One")
	private Long maximum;

	@NotNull(message = "percentage value cannot be null")
	@Min(value = 0, message = "percentage value cannot be less than Zero")
	private Long percentage;
	
	public String toString() {
		return id+" "+minimum+" "+maximum+" "+percentage;
	}
	private Long planid;
}
