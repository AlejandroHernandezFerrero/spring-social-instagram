package org.springframework.social.instagram.model;

public class InstagramProfile {

	private long id;
	private String account_type;
	private int media_count;
	private String username;

	public long getId() {
		return id;
	}

	public String getUsername() {
		return username;
	}

	public int getMediaCount() {
		return media_count;
	}

	public String getAccount_type() {
		return account_type;
	}
}
