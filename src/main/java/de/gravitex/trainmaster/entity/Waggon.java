package de.gravitex.trainmaster.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.NotBlank;

import de.gravitex.trainmaster.dlh.EntityHelper;
import lombok.Data;

@Entity
@Data
public class Waggon extends RailItem {
	
	@NotBlank
	@Column(unique = true)
	private String waggonNumber;

	@Override
	public String getIdentifier() {
		return waggonNumber;
	}

	@Override
	public RailItem asConcreteItem() {
		return EntityHelper.makeWaggon(getIdentifier());
	}
}