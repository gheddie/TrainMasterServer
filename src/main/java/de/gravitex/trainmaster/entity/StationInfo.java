package de.gravitex.trainmaster.entity;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

import com.sun.istack.NotNull;

import lombok.Data;

@Entity
@Data
public class StationInfo extends BaseEntity {

	@NotNull
	@OneToOne
	private Station station;
	
	@OneToOne
	private Track entryTrack;
	
	@OneToOne
	private Track exitTrack;

	public StationInfo(Station station, Track entryTrack, Track exitTrack) {
		super();
		this.station = station;
		this.entryTrack = entryTrack;
		this.exitTrack = exitTrack;
	}
}