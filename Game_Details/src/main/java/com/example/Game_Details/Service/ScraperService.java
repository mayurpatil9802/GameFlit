package com.example.Game_Details.Service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

@Service
public class ScraperService {

	public Map<String, Object> fetchGameDetails(String gameSlug) {
		Map<String, Object> result = new HashMap<>();
		try {
			String url = "https://gamesystemrequirements.com/game/" + gameSlug;

			Document doc = Jsoup.connect(url)
					.userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) "
							+ "AppleWebKit/537.36 (KHTML, like Gecko) "
							+ "Chrome/123.0.0.0 Safari/537.36")
					.referrer("https://www.google.com/")
					.timeout(15000)
					.get();

			// Title
			result.put("title", doc.select("h1").text());

			// Image URL (gepig.com)
			Element imgElement = doc.selectFirst("img[alt$='cover']");
			result.put("imageUrl", imgElement != null ? imgElement.attr("src") : "No image found");

			// Game details: release date, genre, developer, publisher
			Element infoBlock = doc.selectFirst("div.game_head_details");
			if (infoBlock != null) {
				// Split by <br> and process each line
				Elements lines = infoBlock.select("br");
				String html = infoBlock.html();
				String[] parts = html.split("<br>");
				for (String part : parts) {
					part = part.trim();
					if (part.startsWith("Release date:")) {
						result.put("releaseDate", part.replace("Release date:", "").trim());
					} else if (part.startsWith("Genre:")) {
						Element temp = Jsoup.parse(part);
						Elements genreLinks = temp.select("a");
						List<String> genres = new ArrayList<>();
						for (Element g : genreLinks) genres.add(g.text());
						result.put("genre", String.join(", ", genres));
					} else if (part.startsWith("Developer:")) {
						result.put("developer", part.replace("Developer:", "").trim());
					} else if (part.startsWith("Publisher:")) {
						result.put("publisher", part.replace("Publisher:", "").trim());
					}
				}
			}

			// Minimum & recommended system requirements
			Map<String, String> minimum = new LinkedHashMap<>();
			Map<String, String> recommended = new LinkedHashMap<>();

			Element minSection = doc.selectFirst("h2:matchesOwn(^Minimum system requirements:)");
			Element recSection = doc.selectFirst("h2:matchesOwn(^Recommended system requirements:)");

			if (minSection != null) {
				Element next = minSection.nextElementSibling();
				while (next != null && !next.tagName().equals("h2")) {
					String[] partsMin = next.text().split(":", 2);
					if (partsMin.length == 2) minimum.put(partsMin[0].trim(), partsMin[1].trim());
					next = next.nextElementSibling();
				}
			}

			if (recSection != null) {
				Element next = recSection.nextElementSibling();
				while (next != null && !next.tagName().equals("h2")) {
					String[] partsRec = next.text().split(":", 2);
					if (partsRec.length == 2) recommended.put(partsRec[0].trim(), partsRec[1].trim());
					next = next.nextElementSibling();
				}
			}

			result.put("minimumRequirements", minimum);
			result.put("recommendedRequirements", recommended);

		} catch (IOException e) {
			result.put("error", "Failed to fetch data: " + e.getMessage());
		}

		return result;
	}
}
