package de.gravitex.trainmaster.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.ForeignKey;

import com.sun.istack.NotNull;

import lombok.Data;

// @Table(uniqueConstraints = { @UniqueConstraint(columnNames = { "rail_item_sequence_holder_id", "ordinal_position" }) })
@Entity
@Data
public class RailItemSequence extends BaseEntity implements PositionedItem {
	
	@NotNull
	@Column(name = "ordinal_position")
	private Integer ordinalPosition;
	
	@ForeignKey(name = "rail_item_sequence_holder_id")
	@OneToOne
	private RailItemSequenceHolder railItemSequenceHolder;

	@OneToMany
	private List<RailItemSequenceMembership> railItemSequenceMemberships = new ArrayList<>();

	public RailItemSequence(Integer ordinalPosition) {
		super();
		this.ordinalPosition = ordinalPosition;
	}

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