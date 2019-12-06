package de.gravitex.trainmaster.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.ForeignKey;

import lombok.Data;

// @Table(uniqueConstraints = { @UniqueConstraint(columnNames = { "rail_item_sequence_holder_id", "ordinal_position" }) })
@Entity
@Data
public class RailItemSequence extends BaseEntity implements PositionedItem {
	
	@NotNull
	@Column(name = "ordinal_position")
	private Integer ordinalPosition;
	
	@NotBlank
	private String sequenceIdentifier;
	
	// @ForeignKey(name = "rail_item_sequence_holder_id")
	@OneToOne
	private Track track;
	
	@OneToOne
	private Train train;

	@OneToMany
	private List<RailItemSequenceMembership> railItemSequenceMemberships = new ArrayList<>();

	@Override
	@PrePersist
	public void adjustedOrdinalPositions() {
		int ordinalIndex = 0;
		for (RailItemSequenceMembership r : railItemSequenceMemberships) {
			r.setOrdinalPosition(ordinalIndex);
			ordinalIndex++;
		}
	}
}