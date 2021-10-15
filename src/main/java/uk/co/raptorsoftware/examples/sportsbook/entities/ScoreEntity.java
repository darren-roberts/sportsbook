package uk.co.raptorsoftware.examples.sportsbook.entities;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "scores")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ScoreEntity {

	@Id
	@GeneratedValue
	@Column(name = "id")
	private Long id;

	@Column(name = "score", nullable = false)
	private Long score;

	@Column(name = "time_recorded", nullable = false)
	private LocalDateTime timeRecorded;

	@ManyToOne(optional = false)
	private TeamEntity team;

	@ManyToOne(optional = false)
	private EventEntity event;
}
