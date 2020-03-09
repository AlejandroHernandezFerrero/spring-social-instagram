package org.springframework.social.instagram.api;

import java.net.URI;

import org.springframework.social.instagram.model.InstagramProfile;
import org.springframework.social.instagram.model.Media;
import org.springframework.social.instagram.model.PagedMediaList;
import org.springframework.util.MultiValueMap;

public interface Instagram {

	<T> T get(URI path, Class<T> responseType);

	<T> T get(String path, Class<T> responseType);

	<T> T get(String path, MultiValueMap<String, String> params, Class<T> responseType);

	<T> T get(String path, Class<T> responseType, String... fields);

	<C> C post(URI uri, MultiValueMap<String, String> data, Class<C> responseType);

	<C> C post(String path, MultiValueMap<String, String> data, Class<C> responseType);

	void delete(URI uri);

	void delete(String path);

	boolean isAuthorized();

	InstagramProfile getUser();

	InstagramProfile getUser(String... fields);

	InstagramProfile getUser(long userId);

	PagedMediaList getAllMedia(long userId);

	PagedMediaList getAllMedia(long userId, String... fields);

	PagedMediaList getAllMedia();

	PagedMediaList getAllMedia(String... fields);

	PagedMediaList getLimitedMedia(long userId, int limit, String... fields);

	PagedMediaList getLimitedMedia(int limit, String... fields);

	Media getMedia(long mediaId);

	Media getMedia(long mediaId, String... fields);

}