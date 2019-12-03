package de.gravitex.trainmaster.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.NotBlank;

import de.gravitex.trainmaster.dlh.EntityHelper;
import lombok.Data;

@Entity
@Data
public class Locomotive extends RailItem {
	
	@NotBlank
	@Column(unique = true)
	private String locoNumber;

	@Override
	public String getIdentifier() {
		return locoNumber;
	}

	@Override
	public RailItem asConcreteItem() {
		return EntityHelper.makeLocomotive(getIdentifier());
	}
}