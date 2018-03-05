package model;

public interface Likeable {

	public default int getRating(long likes, long dislikes) {
		return (int) (likes - dislikes);
	}

}
