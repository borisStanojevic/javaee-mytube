package model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Comment implements Likeable {

	public static final Comparator<Comment> RATING_COMPARATOR_ASC = (c1, c2) -> Long.compare(c1.getRating(),
			c2.getRating());
	public static final Comparator<Comment> RATING_COMPARATOR_DESC = (c1, c2) -> Long.compare(c2.getRating(),
			c1.getRating());

	public static final Comparator<Comment> DTP_COMPARATOR_ASC = (c1, c2) -> {
		if (c1.getDateTimePosted().isEqual(c2.getDateTimePosted()))
			return 0;
		else if (c1.getDateTimePosted().isBefore(c2.getDateTimePosted()))
			return -1;
		else
			return 1;
	};
	public static final Comparator<Comment> DTP_COMPARATOR_DESC = (c1, c2) -> {
		if (c1.getDateTimePosted().isEqual(c2.getDateTimePosted()))
			return 0;
		else if (c2.getDateTimePosted().isBefore(c1.getDateTimePosted()))
			return -1;
		else
			return 1;
	};

	private long id;
	private String content;
	private LocalDateTime dateTimePosted;
	private long likes;
	private long dislikes;
	private User user;
	@JsonIgnore
	private Video video;
	private boolean deleted;

	public Comment() {
		user = new User();
		video = new Video();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public LocalDateTime getDateTimePosted() {
		return dateTimePosted;
	}

	public void setDateTimePosted(LocalDateTime dateTimePosted) {
		dateTimePosted.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
		this.dateTimePosted = dateTimePosted;
	}

	public long getLikes() {
		return likes;
	}

	public void setLikes(long likes) {
		this.likes = likes;
	}

	public long getDislikes() {
		return dislikes;
	}

	public void setDislikes(long dislikes) {
		this.dislikes = dislikes;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Video getVideo() {
		return video;
	}

	public void setVideo(Video video) {
		this.video = video;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	public long getRating() {
		return this.likes - this.dislikes;
	}
}
