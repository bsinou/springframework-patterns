package net.sinou.patterns.spring.mongodb.domain;
import org.springframework.data.annotation.Id;

public class BlogPost {

	@Id private String id;

	private String title;
	private String body;

	public String getTitle() {
		return title;
	}

	public void setTitle(String firstName) {
		this.title = firstName;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String lastName) {
		this.body = lastName;
	}
}