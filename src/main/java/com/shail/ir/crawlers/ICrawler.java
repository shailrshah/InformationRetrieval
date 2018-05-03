package com.shail.ir.crawlers;

import java.io.IOException;
import java.util.List;

public interface ICrawler {
	/**
	 * Crawl the web, starting from the url
	 * @param seedPage the seed page, from which crawling starts
	 * @param keyword the keyword to search for
	 * @param isFocused true iff the crawling is focused
	 */
	void crawl(Page seedPage, String keyword, boolean isFocused);

	/**
	 * Save an HTML to file
	 * @param html the html as a string
	 * @param filename the name of the file that should contain the html
	 */
	void savePage(String html, String filename);

	/**
	 * Resets the crawler by clearing all the urls
	 */
	public void resetCrawler();

	/**
	 * export the urls to a specified file
	 * @param filename the name of the new file
	 * @throws IOException thrown if there's a problem in writing to file
	 */
	public void exportURLs(String filename) throws IOException;

	/**
	 * Get the maximum number of URLs to be crawled
	 * @return the maximum number of URLs to be crawled
	 */
	public int getMaxUrlCount();

	/**
	 * Set the maximum number of URLs to be crawled
	 * @param maxUrlCount the maximum number of URLs to be crawled
	 */
	public void setMaxUrlCount(int maxUrlCount);

	/**
	 * Get the urls that have been crawled.
	 * @return a list of URLs that have been crawled.
	 */
	public List<String> getUrls();

	/**
	 * Set the URLs that have been crawled
	 * @param urls a list of URLs that have been crawled
	 */
	public void setUrls(List<String> urls);
}
