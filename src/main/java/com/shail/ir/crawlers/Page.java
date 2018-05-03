package com.shail.ir.crawlers;

/**
 * A Page represents a HTML page that has already been or is about to be crawled
 * @author Shail Shah
 */
public class Page {
	private String url;
	private int depth;

	public Page(String url, int depth) {
		this.url = url;
		this.depth = depth;
	}

	/**
	 * Get the URL of the page
	 * @return the URL of the page
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * Get the depth of the page
	 * @return the depth of the page
	 */
	public int getDepth() {
		return depth;
	}
}
