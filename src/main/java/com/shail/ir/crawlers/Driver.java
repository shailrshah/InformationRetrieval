package com.shail.ir.crawlers;

import java.io.IOException;

public class Driver {
	public static void main(String[] args) throws IOException {
		int maxUrlCount = 1000;
		int maxDepth = 5;
		String seedUrl =  "https://en.wikipedia.org/wiki/Sustainable_energy";
		String keyword = "solar";
		Page seedPage = new Page(seedUrl, 1);
		int timeOutSeconds = 0;

		Crawler crawler = new BreathFirstCrawler(maxUrlCount, maxDepth, timeOutSeconds);
		crawler.crawl(seedPage, keyword, true);
		crawler.exportURLs("result/crawlers/BFS-Focused-URLS.txt");
		crawler.resetCrawler();

		crawler.crawl(seedPage, keyword, false);
		crawler.exportURLs("result/crawlers/BFS-Unfocused-URLS.txt");

		crawler = new DepthFirstCrawler(maxUrlCount, maxDepth, timeOutSeconds);
		crawler.crawl(seedPage, keyword, true);
		crawler.exportURLs("result/crawlers/DFS-Focused-URLS.txt");
		crawler.resetCrawler();

		crawler.crawl(seedPage, keyword, false);
		crawler.exportURLs("result/crawlers/DFS-Unfocused-URLS.txt");
	}
}
