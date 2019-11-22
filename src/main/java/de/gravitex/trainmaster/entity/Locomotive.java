package de.gravitex.trainmaster.entity;

import lombok.Data;

@Data
public class Locomotive extends RailItem {
	
	private String locoNumber;

	@Override
	public String getIdentifier() {
		return locoNumber;
	}

	@Override
	public RailItem asConcreteItem() {
		return new Locomotive(getIdentifier());
	}

	public Locomotive(String locoNumber) {
		super();
		this.locoNumber = locoNumber;
	}
}