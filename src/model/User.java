package model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.HashMap;

public class User {

	public static final Comparator<User> USERNAME_COMPARATOR_ASC = (u1, u2) -> u1.getUsername()
			.compareTo(u2.getUsername());
	public static final Comparator<User> USERNAME_COMPARATOR_DESC = (u1, u2) -> u2.getUsername()
			.compareTo(u1.getUsername());

	public static final Comparator<User> FIRST_NAME_COMPARATOR_ASC = (u1, u2) -> u1.getFirstName()
			.compareTo(u2.getFirstName());
	public static final Comparator<User> FIRST_NAME_COMPARATOR_DESC = (u1, u2) -> u2.getFirstName()
			.compareTo(u1.getFirstName());

	public static final Comparator<User> LAST_NAME_COMPARATOR_ASC = (u1, u2) -> u1.getLastName()
			.compareTo(u2.getLastName());
	public static final Comparator<User> LAST_NAME_COMPARATOR_DESC = (u1, u2) -> u2.getLastName()
			.compareTo(u1.getLastName());

	public static final Comparator<User> EMAIL_COMPARATOR_ASC = (u1, u2) -> u1.getEmail().compareTo(u2.getEmail());
	public static final Comparator<User> EMAIL_COMPARATOR_DESC = (u1, u2) -> u2.getEmail().compareTo(u1.getEmail());

	public static final Comparator<User> ROLE_COMPARATOR_ASC = (u1, u2) -> u1.getRole().toString()
			.compareTo(u2.getRole().toString());
	public static final Comparator<User> ROLE_COMPARATOR_DESC = (u1, u2) -> u2.getRole().toString()
			.compareTo(u1.getRole().toString());

	private String username;
	private String password;
	private String firstName;
	private String lastName;
	private String email;
	private String channelDescription;
	private LocalDate registrationDate;
	private Role role;
	private String thumbnailUrl;
	private boolean blocked;
	private boolean deleted;
	private HashMap<String, User> subscribers;
	private HashMap<String, User> subscribeds;
	private HashMap<Long, Like<Video>> videoLikes;
	private HashMap<Long, Like<Comment>> commentLikes;

	public User() {
		firstName = "";
		lastName = "";
		// thumbnailUrl = System.getProperty("java.io.tmpdir") + File.separator +
		// "images" + File.separator + "default.png";
		channelDescription = "";
		subscribers = new HashMap<>();
		subscribeds = new HashMap<>();
		videoLikes = new HashMap<>();
		commentLikes = new HashMap<>();
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getChannelDescription() {
		return channelDescription;
	}

	public void setChannelDescription(String channelDescription) {
		this.channelDescription = channelDescription;
	}

	public LocalDate getRegistrationDate() {
		return registrationDate;
	}

	public void setRegistrationDate(LocalDate registrationDate) {
		registrationDate.format(DateTimeFormatter.ISO_LOCAL_DATE);
		this.registrationDate = registrationDate;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public boolean isBlocked() {
		return blocked;
	}

	public void setBlocked(boolean blocked) {
		this.blocked = blocked;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	public void setSubscribers(HashMap<String, User> subscribers) {
		this.subscribers = subscribers;
	}

	public void setSubscribeds(HashMap<String, User> subscribeds) {
		this.subscribeds = subscribeds;
	}

	public String getThumbnailUrl() {
		return thumbnailUrl;
	}

	public void setThumbnailUrl(String thumbnailUrl) {
		this.thumbnailUrl = thumbnailUrl;
	}

	public void setVideoLikes(HashMap<Long, Like<Video>> videoLikes) {
		this.videoLikes = videoLikes;
	}

	public void setCommentLikes(HashMap<Long, Like<Comment>> commentLikes) {
		this.commentLikes = commentLikes;
	}

	public HashMap<String, User> getSubscribers() {
		return subscribers;
	}

	public HashMap<String, User> getSubscribeds() {
		return subscribeds;
	}

	public HashMap<Long, Like<Video>> getVideoLikes() {
		return videoLikes;
	}

	public HashMap<Long, Like<Comment>> getCommentLikes() {
		return commentLikes;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (obj == this)
			return true;
		if (this.getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		if (this.username == null && other.username != null)
			return false;
		else if (!this.username.equals(other.username))
			return false;
		return true;
	}

}
