package uk.co.raptorsoftware.examples.sportsbook.entities;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "event")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventEntity {

	@Id
	@GeneratedValue
	@Column(name = "id")
	private Long id;

	@Column(name = "event_name", unique = true, nullable = false)
	private String name;

	@Column(name = "event_date_time", nullable = false)
	private LocalDateTime eventDateTime;

	@OneToMany(mappedBy = "event")
	private List<ScoreEntity> scores;

	@ManyToOne(optional = false)
	private TeamEntity teamA;

	@ManyToOne(optional = false)
	private TeamEntity teamB;
}
