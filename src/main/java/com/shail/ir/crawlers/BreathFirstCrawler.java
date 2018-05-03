package com.shail.ir.crawlers;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.concurrent.TimeUnit;

/**
 * A Breadth-First Crawler searches for links level-by-level
 * @author Shail Shah
 */
public class BreathFirstCrawler extends Crawler {

	public BreathFirstCrawler(int maxUrlCount, int maxDepth, int timeOutSeconds) {
		this.maxUrlCount = maxUrlCount;
		this.maxDepth = maxDepth;
		this.urls = new ArrayList<>(maxUrlCount);
		this.timeOutSeconds = timeOutSeconds;
	}

	/**
	 * Crawl the web, starting from the seed url
	 *
	 * @param seedPage  the seed page, from which crawling starts
	 * @param keyword   the keyword to search for
	 * @param isFocused true iff the crawling is focused
	 */
	@Override
	public void crawl(Page seedPage, String keyword, boolean isFocused) {
		Deque<Page> queue = new ArrayDeque<>();
		queue.add(seedPage);

		while(!queue.isEmpty()) {
			if(urls.size() >= maxUrlCount) return;

			Page page = queue.remove();
			String url = page.getUrl();
			int depth = page.getDepth();

			if(depth > maxDepth || urls.contains(url))
				continue;

			urls.add(url);
			System.out.println(urls.size() + ". Crawling " + url);

			try {
				String filename = "data/crawlers/" + ((isFocused)?"focused " : "unfocused ")+"bfs-"+ urls.size();
				savePage(Jsoup.connect(url).get().html(), filename);
				Elements elements = Jsoup.connect(url).get().select("a[abs:href*=en.wikipedia.org/wiki/]");
				for (Element element : elements)
					if(isCrawlable(element, keyword, isFocused))
						queue.add(new Page(removeFragment(element.attr("abs:href")),
								depth + 1));

				TimeUnit.SECONDS.sleep(timeOutSeconds);
			} catch (IOException | InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
