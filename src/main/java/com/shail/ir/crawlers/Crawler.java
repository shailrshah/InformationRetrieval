package com.shail.ir.crawlers;

import org.jsoup.nodes.Element;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

/**
 * A Crawler is able to crawl the web from a seed url
 * @author Shail Shah
 */
public abstract class Crawler implements ICrawler{

	int maxUrlCount;
	int maxDepth;
	List<String> urls;
	int timeOutSeconds;

	/**
	 * Crawl the web, starting from the url
	 * @param seedPage the seed page, from which crawling starts
	 * @param keyword the keyword to search for
	 * @param isFocused true iff the crawling is focused
	 */
	public abstract void crawl(Page seedPage, String keyword, boolean isFocused);

	/**
	 * Save an HTML to file
	 * @param html the html as a string
	 * @param filename the name of the file that should contain the html
	 */
	public void savePage(String html, String filename) {
		try (FileWriter fw = new FileWriter(filename + ".html", true)) {
			fw.write(html);
		} catch (IOException ioe) {
			System.out.println("Unable to write to file");
		}
	}

	/**
	 * Resets the crawler by clearing all the urls
	 */
	public void resetCrawler() {
		urls.clear();
	}

	/**
	 * export the urls to a specified file
	 * @param filename the name of the new file
	 * @throws IOException thrown if there's a problem in writing to file
	 */
	public void exportURLs(String filename) throws IOException {
		try (FileWriter fw = new FileWriter(filename)) {
			urls.stream()
					.filter(Objects::nonNull)
					.forEach(url -> {
						try {
							fw.write(url + "\n");
						} catch (IOException e) {
							e.printStackTrace();
						}
					});
		} catch(IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Removes the fragment from the URL, if present
	 * removeFragment("foo.org/bar.html#fragment") will return "foo.org/bar.html"
	 * @param url a url
	 * @return the given url without the fragment, if present
	 */
	String removeFragment(String url) {
		char fragmentIdentifier = '#';
		return url.contains(""+fragmentIdentifier) ?
				url.substring(0, url.indexOf(fragmentIdentifier)) :
				url;
	}

	/**
	 * Is the given element crawlable?
	 * @param anchorElmenet an HTML anchor element
	 * @param keyword a search keyword
	 * @param focused true iff focused search
	 * @return true iff the given element is crawlable
	 */
	boolean isCrawlable(Element anchorElmenet, String keyword, boolean focused) {
		String elementText = anchorElmenet.text().toLowerCase();
		String elementRelUrl = removeFragment(anchorElmenet.attr("href")).toLowerCase();
		String elementClass = anchorElmenet.attr("class");

		return !elementRelUrl.contains(":") &&
				!elementClass.equals("image") &&
				(!focused || (elementText + elementRelUrl).contains(keyword));
	}

	/**
	 * Get the maximum number of URLs to be crawled
	 * @return the maximum number of URLs to be crawled
	 */
	public int getMaxUrlCount() {
		return maxUrlCount;
	}

	/**
	 * Set the maximum number of URLs to be crawled
	 * @param maxUrlCount the maximum number of URLs to be crawled
	 */
	public void setMaxUrlCount(int maxUrlCount) {
		this.maxUrlCount = maxUrlCount;
	}

	/**
	 * Get the urls that have been crawled.
	 * @return a list of URLs that have been crawled.
	 */
	public List<String> getUrls() {
		return urls;
	}

	/**
	 * Set the URLs that have been crawled
	 * @param urls a list of URLs that have been crawled
	 */
	public void setUrls(List<String> urls) {
		this.urls = urls;
	}
}