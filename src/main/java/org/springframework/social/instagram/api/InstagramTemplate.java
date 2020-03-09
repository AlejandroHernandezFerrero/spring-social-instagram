package org.springframework.social.instagram.api;

import java.net.URI;
import java.util.Arrays;

import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.social.MissingAuthorizationException;
import org.springframework.social.instagram.model.InstagramProfile;
import org.springframework.social.instagram.model.Media;
import org.springframework.social.instagram.model.PagedMediaList;
import org.springframework.social.oauth2.AbstractOAuth2ApiBinding;
import org.springframework.social.support.URIBuilder;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

/**
 * This is the central class for interacting with Instagram.
 * <p>
 * Not all operations through Instagram require OAuth 2-based authentication.
 * However, to perform authenticated operations, InstagramTemplate must be
 * constructed with a valid access token.
 * </p>
 * <p>
 * There are quite a few operations that do not require OAuth authentication. In
 * those cases, you may use a {@link InstagramTemplate} that is created through
 * the default constructor. Attempts to perform secured operations through such
 * an instance, however, will result in {@link IllegalStateException} being
 * thrown.
 * </p>
 */
public class InstagramTemplate extends AbstractOAuth2ApiBinding implements Instagram {

	private static final String API_URL_BASE = "https://graph.instagram.com/";
	private static final URI API_URI_BASE = URI.create(API_URL_BASE);
	private static final String MEDIA_PATH = "media/";
	private static final String ME_PATH = "me/";

	private static final String FIELDS = "fields";
	private static final String USER_FIELDS = "account_type,id,media_count,username";
	private static final String MEDIA_FIELDS = "caption,id,media_type,media_url,permalink,thumbnail_url,timestamp,username";

	private final String accessToken;
	private final String clientId;
	private boolean isAuthorized = true;

	/**
	 * Create a new instance of InstagramTemplate. This constructor creates a new
	 * InstagramTemplate able to perform unauthenticated operations against
	 * Instagram's API. Some operations do not require OAuth authentication. A
	 * InstagramTemplate created with this constructor will support those
	 * operations. Those operations requiring authentication will throw
	 * {@link BadCredentialsException}.
	 * 
	 * @param clientId
	 */
	public InstagramTemplate(String clientId) {
		this(clientId, null, false);
	}

	/**
	 * Create a new instance of InstagramTemplate.
	 * 
	 * @param clientId    the application's client ID
	 * @param accessToken an access token acquired through OAuth authentication with
	 *                    Instagram
	 */
	public InstagramTemplate(String clientId, String accessToken) {
		this(clientId, accessToken, true);
	}

	private InstagramTemplate(String clientId, String accessToken, boolean isAuthorizedForUser) {
		super(accessToken);
		this.clientId = clientId;
		this.accessToken = accessToken;
		this.isAuthorized = isAuthorizedForUser;
		MappingJackson2HttpMessageConverter json = new MappingJackson2HttpMessageConverter();
		json.setSupportedMediaTypes(Arrays.asList(new MediaType("text", "javascript")));
		getRestTemplate().getMessageConverters().add(json);
		getRestTemplate().setErrorHandler(new InstagramErrorHandler());
	}

	@Override
	public <T> T get(URI path, Class<T> responseType) {
		return getRestTemplate().getForObject(buildUri(path), responseType);
	}

	@Override
	public <T> T get(String path, Class<T> responseType) {
		return getRestTemplate().getForObject(buildUri(path), responseType);
	}

	@Override
	public <T> T get(String path, MultiValueMap<String, String> params, Class<T> responseType) {
		return getRestTemplate().getForObject(buildUri(path, params), responseType);
	}

	@Override
	public <T> T get(String path, Class<T> responseType, String... fields) {
		return getRestTemplate().getForObject(buildUri(path, fields), responseType);
	}

	@Override
	public <C> C post(URI uri, MultiValueMap<String, String> data, Class<C> responseType) {
		return getRestTemplate().postForObject(buildUri(uri), data, responseType);
	}

	@Override
	public <C> C post(String path, MultiValueMap<String, String> data, Class<C> responseType) {
		return getRestTemplate().postForObject(buildUri(path), data, responseType);
	}

	@Override
	public void delete(URI uri) {
		getRestTemplate().delete(buildUri(uri));
	}

	@Override
	public void delete(String path) {
		getRestTemplate().delete(buildUri(path));
	}

	protected URIBuilder withAccessToken(URI uri) {
		return (accessToken == null) ? URIBuilder.fromUri(uri).queryParam("client_id", clientId)
				: URIBuilder.fromUri(uri).queryParam("access_token", accessToken);
	}

	protected URIBuilder withAccessToken(String uri) {
		return (accessToken == null) ? URIBuilder.fromUri(uri).queryParam("client_id", clientId)
				: URIBuilder.fromUri(uri).queryParam("access_token", accessToken);
	}

	protected URI buildUri(URI path) {
		URIBuilder uriBuilder = withAccessToken(API_URI_BASE.resolve(path));
		return uriBuilder.build();
	}

	protected URI buildUri(String path) {
		URIBuilder uriBuilder = withAccessToken(API_URL_BASE + path);
		return uriBuilder.build();
	}

	protected URI buildUri(String path, MultiValueMap<String, String> params) {
		URIBuilder uriBuilder = withAccessToken(API_URL_BASE + path);
		uriBuilder.queryParams(params);
		return uriBuilder.build();
	}

	protected URI buildUri(String path, String... fields) {
		LinkedMultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>(1);
		params.put(FIELDS, Arrays.asList(fields));
		URIBuilder uriBuilder = withAccessToken(API_URL_BASE + path);
		uriBuilder.queryParams(params);
		return uriBuilder.build();
	}

	@Override
	public boolean isAuthorized() {
		return isAuthorized;
	}

	protected void requireUserAuthorization() {
		if (!isAuthorized()) {
			throw new MissingAuthorizationException("");
		}
	}

	protected InstagramProfile getUser(String userPath) {
		requireUserAuthorization();
		return get(userPath, InstagramProfile.class, USER_FIELDS);
	}

	protected InstagramProfile getUser(String userPath, String... fields) {
		requireUserAuthorization();
		return get(userPath, InstagramProfile.class, fields);
	}

	@Override
	public InstagramProfile getUser(String... fields) {
		return getUser(ME_PATH, fields);
	}

	@Override
	public InstagramProfile getUser() {
		return getUser(ME_PATH);
	}

	@Override
	public InstagramProfile getUser(long userId) {
		return getUser(userId + "/");
	}

	protected PagedMediaList getAllMedia(String userPath) {
		return get(userPath + MEDIA_PATH, PagedMediaList.class, MEDIA_FIELDS);
	}

	protected PagedMediaList getAllMedia(String userPath, String... fields) {
		return get(userPath + MEDIA_PATH, PagedMediaList.class, fields);
	}

	@Override
	public PagedMediaList getAllMedia(long userId) {
		return getAllMedia(Long.toString(userId));
	}

	@Override
	public PagedMediaList getAllMedia(long userId, String... fields) {
		return getAllMedia(Long.toString(userId), fields);
	}

	@Override
	public PagedMediaList getAllMedia() {
		return getAllMedia(ME_PATH);
	}

	@Override
	public PagedMediaList getAllMedia(String... fields) {
		return getAllMedia(ME_PATH, fields);
	}

	protected PagedMediaList getLimitedMedia(String userPath, int limit, String... fields) {
		LinkedMultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>(1);
		params.add("limit", Integer.toString(limit));
		if (fields != null && fields.length > 0) {
			params.put(FIELDS, Arrays.asList(fields));
		} else {
			params.put(FIELDS, Arrays.asList(MEDIA_FIELDS));
		}
		return get(userPath + MEDIA_PATH, params, PagedMediaList.class);
	}

	@Override
	public PagedMediaList getLimitedMedia(long userId, int limit, String... fields) {
		return getLimitedMedia(Long.toString(userId), limit, fields);
	}

	@Override
	public PagedMediaList getLimitedMedia(int limit, String... fields) {
		return getLimitedMedia(ME_PATH, limit, fields);
	}

	@Override
	public Media getMedia(long mediaId) {
		return get(buildUri(mediaId + "/", MEDIA_FIELDS), Media.class);
	}

	@Override
	public Media getMedia(long mediaId, String... fields) {
		return get(buildUri(mediaId + "/", fields), Media.class);
	}

}
