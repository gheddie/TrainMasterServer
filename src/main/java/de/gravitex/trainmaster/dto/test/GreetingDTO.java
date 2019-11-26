package de.gravitex.trainmaster.dto.test;

import de.gravitex.trainmaster.dto.ServerDTO;

public class GreetingDTO implements ServerDTO {

	private final long id;
	
	private final String content;

	public GreetingDTO(long id, String content) {
		this.id = id;
		this.content = content;
	}

	public long getId() {
		return id;
	}

	public String getContent() {
		return content;
	}
}