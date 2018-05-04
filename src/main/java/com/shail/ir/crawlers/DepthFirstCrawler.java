package com.shail.ir.crawlers;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 * A Depth-First Crawler searches for links by diving into each one upto a certain depth
 * @author Shail Shah
 */
public class DepthFirstCrawler extends Crawler {

	DepthFirstCrawler(int maxUrlCount, int maxDepth, int timeOutSeconds) {
		this.maxUrlCount = maxUrlCount;
		this.maxDepth = maxDepth;
		this.urls = new ArrayList<>(maxUrlCount);
		this.timeOutSeconds = timeOutSeconds;
	}

	/**
	 * Crawl the web, starting from the url
	 *
	 * @param seedPage  the seed page, from which crawling starts
	 * @param keyword   the keyword to search for
	 * @param isFocused true iff the crawling is focused
	 */
	@Override
	public void crawl(Page seedPage, String keyword, boolean isFocused) {
		int depth = seedPage.getDepth();
		String url = seedPage.getUrl();
		if(urls.size() >= maxUrlCount || depth > maxDepth || urls.contains(url))
			return;

		urls.add(url);
		System.out.println(urls.size() + ". Crawling " + url);

		try{
			TimeUnit.SECONDS.sleep(timeOutSeconds);
			String filename = "data/crawlers/" + ((isFocused)?"focused " : "unfocused ")+"dfs-"+ urls.size();
			savePage(Jsoup.connect(url).get().html(), filename);

			Elements anchorElements = Jsoup.connect(url).get().select("a[abs:href*=en.wikipedia.org/wiki/]");
			for (Element anchorElmenet : anchorElements)
				if(isCrawlable(anchorElmenet, keyword, isFocused))
					crawl(new Page(removeFragment(anchorElmenet.attr("abs:href")),
							depth + 1),
							keyword,
							isFocused);

		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
	}
}
