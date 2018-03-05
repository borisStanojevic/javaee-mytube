	package model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;

public class Video implements Likeable {

	public static final Comparator<Video> NAME_COMPARATOR_ASC = (v1, v2) -> v1.getName().compareTo(v2.getName());
	public static final Comparator<Video> NAME_COMPARATOR_DESC = (v1, v2) -> v2.getName().compareTo(v1.getName());

	public static final Comparator<Video> OWNER_COMPARATOR_ASC = (v1, v2) -> v1.getOwner().getUsername()
			.compareTo(v2.getOwner().getUsername());
	public static final Comparator<Video> OWNER_COMPARATOR_DESC = (v1, v2) -> v2.getOwner().getUsername()
			.compareTo(v1.getOwner().getUsername());

	public static final Comparator<Video> VIEWS_COMPARATOR_ASC = (v1, v2) -> Long.compare(v1.getViews(), v2.getViews());

	public static final Comparator<Video> VIEWS_COMPARATOR_DESC = (v1, v2) -> Long.compare(v2.getViews(),
			v1.getViews());

	public static final Comparator<Video> DTU_COMPARATOR_ASC = (v1, v2) -> {
		if (v1.getDateTimeUploaded().isEqual(v2.getDateTimeUploaded()))
			return 0;
		else if (v1.getDateTimeUploaded().isBefore(v2.getDateTimeUploaded()))
			return -1;
		else
			return 1;
	};
	public static final Comparator<Video> DTU_COMPARATOR_DESC = (v1, v2) -> {
		if (v1.getDateTimeUploaded().isEqual(v2.getDateTimeUploaded()))
			return 0;
		else if (v2.getDateTimeUploaded().isBefore(v1.getDateTimeUploaded()))
			return -1;
		else
			return 1;
	};

	private long id;
	private String name;
	private String videoUrl;
	private String description;
	private Visibility visibility;
	private boolean commentsAllowed;
	private boolean ratingVisible;
	private boolean blocked;
	private long views;
	private long likes;
	private long dislikes;
	private LocalDateTime dateTimeUploaded;
	private boolean deleted;
	private User owner;

	public Video() {
		description = "";
		owner = new User();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getVideoUrl() {
		return videoUrl;
	}

	public void setVideoUrl(String videoUrl) {
		this.videoUrl = videoUrl;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Visibility getVisibility() {
		return visibility;
	}

	public void setVisibility(Visibility visibility) {
		this.visibility = visibility;
	}

	public boolean isCommentsAllowed() {
		return commentsAllowed;
	}

	public void setCommentsAllowed(boolean commentsAllowed) {
		this.commentsAllowed = commentsAllowed;
	}

	public boolean isRatingVisible() {
		return ratingVisible;
	}

	public void setRatingVisible(boolean ratingVisible) {
		this.ratingVisible = ratingVisible;
	}

	public boolean isBlocked() {
		return blocked;
	}

	public void setBlocked(boolean blocked) {
		this.blocked = blocked;
	}

	public long getViews() {
		return views;
	}

	public void setViews(long views) {
		this.views = views;
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

	public LocalDateTime getDateTimeUploaded() {
		return dateTimeUploaded;
	}

	public void setDateTimeUploaded(LocalDateTime dateTimeUploaded) {
		dateTimeUploaded.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
		this.dateTimeUploaded = dateTimeUploaded;
	}

	public User getOwner() {
		return owner;
	}

	public void setOwner(User owner) {
		this.owner = owner;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this)
			return true;
		if (obj == null)
			return false;
		if (this.getClass() != obj.getClass())
			return false;
		Video other = (Video) obj;
		if (this.id != other.id)
			return false;
		return true;
	}

}
