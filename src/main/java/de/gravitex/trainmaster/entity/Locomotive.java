package de.gravitex.trainmaster.entity;

import javax.persistence.Column;

import lombok.Data;

@Data
public class Locomotive extends RailItem {
	
	@Column(unique = true)
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