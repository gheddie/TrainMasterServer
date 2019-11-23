package de.gravitex.trainmaster.entity;

import javax.persistence.Column;
import javax.persistence.Entity;

import lombok.Data;

@Entity
@Data
public class Waggon extends RailItem {
	
	public Waggon(String aWaggonNumber) {
		super();
		this.waggonNumber = aWaggonNumber;
	}
	
	@Column(unique = true)
	private String waggonNumber;

	@Override
	public String getIdentifier() {
		return waggonNumber;
	}

	@Override
	public RailItem asConcreteItem() {
		return new Waggon(getIdentifier());
	}
}