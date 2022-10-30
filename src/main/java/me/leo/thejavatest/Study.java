package me.leo.thejavatest;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Study {

	private StudyStatus status = StudyStatus.DRAFT;
	private int limit;

	public Study(int limit) {
		if (limit < 0) {
			throw new IllegalArgumentException("limit 은 0 보다 커야 한다.");
		}
		this.limit = limit;
	}
}
