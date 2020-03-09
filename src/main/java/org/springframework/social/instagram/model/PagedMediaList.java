package org.springframework.social.instagram.model;

import java.net.URI;
import java.util.List;
import java.util.Map;

import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PagedMediaList {

	@JsonProperty("data")
	private List<Media> list;
	private String nextUrl;

	@JsonProperty("paging")
	private void paging(Map<String, Object> paging) {
		Object next = paging.get("next");
		if (next != null) {
			nextUrl = next.toString();
		}
	}

	public List<Media> getPage() {
		return list;
	}

	public boolean hasMorePages() {
		return nextUrl != null;
	}

	public PagedMediaList getNextPage() {
		return new RestTemplate().getForObject(URI.create(nextUrl), PagedMediaList.class);
	}
}
