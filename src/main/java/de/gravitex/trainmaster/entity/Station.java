package de.gravitex.trainmaster.entity;

import javax.persistence.Entity;

import lombok.Data;

@Entity
@Data
public class Station extends BaseEntity {
	
	private String stationName;

	public Station(String aStationName) {
		this.stationName = aStationName;
	}
}