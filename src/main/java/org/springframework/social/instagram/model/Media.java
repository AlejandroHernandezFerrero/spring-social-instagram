package org.springframework.social.instagram.model;

import java.util.Date;

public class Media {

	private long id;
	private String caption;
	private String media_type;
	private String media_url;
	private String permalink;
	private String thumbnail_url;
	private Date timestamp;
	private String username;

	public long getId() {
		return id;
	}

	public String getCaption() {
		return caption;
	}

	public String getMedia_type() {
		return media_type;
	}

	public String getMedia_url() {
		return media_url;
	}

	public String getPermalink() {
		return permalink;
	}

	public String getThumbnail_url() {
		return thumbnail_url;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public String getUsername() {
		return username;
	}
}
