package model;

public class Subscription {

	// User who is subscribing to some other user
	private User subscriber;
	// User who gets subscription
	private User subscribed;

	public User getSubscriber() {
		return subscriber;
	}

	public void setSubscriber(User subscriber) {
		this.subscriber = subscriber;
	}

	public User getSubscribed() {
		return subscribed;
	}

	public void setSubscribed(User subscribed) {
		this.subscribed = subscribed;
	}

}
