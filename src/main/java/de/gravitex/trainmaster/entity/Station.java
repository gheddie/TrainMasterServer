package de.gravitex.trainmaster.entity;

import javax.persistence.Entity;
import javax.validation.constraints.NotBlank;

import lombok.Data;

@Entity
@Data
public class Station extends BaseEntity {
	
	@NotBlank
	private String stationName;
}