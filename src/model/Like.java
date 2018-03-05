package model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Like<T> {

	private boolean isLike;
	@JsonIgnore
	private LocalDateTime dateTimeCreated;
	@JsonIgnore
	private User user;
	@JsonIgnore
	private T object;

	public boolean isLike() {
		return isLike;
	}

	public void setLike(boolean isLike) {
		this.isLike = isLike;
	}

	public LocalDateTime getDateTimeCreated() {
		return dateTimeCreated;
	}

	public void setDateTimeCreated(LocalDateTime dateTimeCreated) {
		dateTimeCreated.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
		this.dateTimeCreated = dateTimeCreated;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public T getObject() {
		return object;
	}

	public void setObject(T object) {
		this.object = object;
	}

}
