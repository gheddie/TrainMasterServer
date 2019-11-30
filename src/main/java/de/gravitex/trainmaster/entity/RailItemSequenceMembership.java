package de.gravitex.trainmaster.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.ForeignKey;

import lombok.Data;

@Table(uniqueConstraints = { @UniqueConstraint(columnNames = { "rail_item_sequence_id", "ordinal_position" }) })
@Entity
@Data
public class RailItemSequenceMembership extends BaseEntity {

	@OneToOne
	private RailItem railItem;

	@OneToOne
	@ForeignKey(name = "rail_item_sequence_id")
	private RailItemSequence railItemSequence;

	@NotNull
	@Column(name = "ordinal_position")
	private Integer ordinalPosition;

	public String toString() {
		return railItem.getIdentifier() + "@" + ordinalPosition;
	}
}