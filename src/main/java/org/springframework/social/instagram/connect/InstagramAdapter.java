package org.springframework.social.instagram.connect;

import org.springframework.social.ApiException;
import org.springframework.social.connect.ApiAdapter;
import org.springframework.social.connect.ConnectionValues;
import org.springframework.social.connect.UserProfile;
import org.springframework.social.connect.UserProfileBuilder;
import org.springframework.social.instagram.api.Instagram;
import org.springframework.social.instagram.model.InstagramProfile;

/**
 * Instagram ApiAdapter implementation.
 */
public class InstagramAdapter implements ApiAdapter<Instagram> {

	public boolean test(Instagram instagram) {
		try {
			instagram.getUser("id");
			return true;
		} catch (ApiException e) {
			return false;
		}
	}

	public void setConnectionValues(Instagram instagram, ConnectionValues values) {
		InstagramProfile profile = instagram.getUser("id", "username");
		values.setProviderUserId(Long.toString(profile.getId()));
		values.setDisplayName(profile.getUsername());
	}

	public UserProfile fetchUserProfile(Instagram instagram) {
		InstagramProfile profile = instagram.getUser();
		return new UserProfileBuilder().setUsername(profile.getUsername()).build();
	}

	public void updateStatus(Instagram instagram, String message) {
		//
	}

}