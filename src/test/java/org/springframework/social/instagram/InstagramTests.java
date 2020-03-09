package org.springframework.social.instagram;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.social.instagram.api.Instagram;
import org.springframework.social.instagram.api.InstagramTemplate;
import org.springframework.social.instagram.model.InstagramProfile;
import org.springframework.social.instagram.model.PagedMediaList;

class InstagramTests {

	private static final String TOKEN = "IGQVJXendWSlRWY3lJUFFKa2tvbEhqU0VibS04bnFTSzZAzMW0xSWNNbVBla3pBRERCUnVqNi1rdnNKZAHNpa1E5eENvbFNlQXFNZAWtUcW5yNV93R29KaHhwTjZAjbkZAkWGJkSTlkeVBn";

	static Instagram instagram;

	@BeforeAll
	public static void setup() {
		instagram = new InstagramTemplate(null, TOKEN);
	}

	@Test
	public void user() {
		InstagramProfile user = instagram.getUser();
		assertNotNull(user.getUsername());
	}

	@Test
	public void userFields() {
		InstagramProfile user = instagram.getUser("id");
		assertNull(user.getUsername());
	}

	@Test
	public void media() {
		PagedMediaList media = instagram.getAllMedia();
		assertTrue(media.getPage().size() > 0);
	}

	@Test
	public void paging() {
		PagedMediaList media = instagram.getLimitedMedia(1);
		assertTrue(media.getPage().size() == 1);
		assertTrue(media.getNextPage().getPage().size() == 1);
	}

}
