//package com.example.Game_Details.Service;
//
//import com.example.Game_Details.Entity.SteamGameDetails;
//import com.example.Game_Details.Entity.SteamGames;
//import com.example.Game_Details.Repository.SteamGameDetailsRepository;
//import com.example.Game_Details.Repository.SteamGameRepository;
//import org.json.JSONObject;
//import org.springframework.scheduling.annotation.Async;
//import org.springframework.stereotype.Service;
//import org.springframework.web.client.HttpClientErrorException;
//import org.springframework.web.client.RestTemplate;
//
//import java.util.List;
//
//@Service
//public class SteamGameDetailsService {
//
//	private final SteamGameRepository steamGameRepository;
//	private final SteamGameDetailsRepository steamGameDetailsRepository;
//	private final RestTemplate restTemplate;
//
//	public SteamGameDetailsService(SteamGameRepository steamGameRepository,
//								   SteamGameDetailsRepository steamGameDetailsRepository,
//								   RestTemplate restTemplate) {
//		this.steamGameRepository = steamGameRepository;
//		this.steamGameDetailsRepository = steamGameDetailsRepository;
//		this.restTemplate = restTemplate;
//	}
//
//	@Async
//	public void fetchAllInBatches(int batchSize) {
//		List<SteamGames> allGames = steamGameRepository.findAll();
//		int total = allGames.size();
//		System.out.println("Total games to process: " + total);
//
//		for (int i = 0; i < total; i += batchSize) {
//			int end = Math.min(i + batchSize, total);
//			List<SteamGames> batch = allGames.subList(i, end);
//
//			for (SteamGames game : batch) {
//				fetchAndStoreGameDetail(game);
//				countdownSleep(1500); // 1.5s delay between requests
//			}
//
//			System.out.println("✅ Processed batch " + (i / batchSize + 1));
//		}
//
//		System.out.println("🎯 Completed all game details!");
//	}
//
//	private void fetchAndStoreGameDetail(SteamGames game) {
//		try {
//			if (steamGameDetailsRepository.existsByAppid(game.getGameId())) return;
//
//			String url = "https://store.steampowered.com/api/appdetails?appids=" + game.getGameId();
//			String response;
//
//			try {
//				response = restTemplate.getForObject(url, String.class);
//			} catch (HttpClientErrorException.TooManyRequests e) {
//				System.out.println("429 Too Many Requests for AppID " + game.getGameId() + ". Waiting 10s...");
//				countdownSleep(10000); // wait 10s with countdown
//				response = restTemplate.getForObject(url, String.class); // retry once
//			}
//
//			if (response == null || response.isBlank()) return;
//
//			JSONObject root = new JSONObject(response);
//			JSONObject appData = root.optJSONObject(String.valueOf(game.getGameId()));
//			if (appData == null || !appData.optBoolean("success")) return;
//
//			JSONObject data = appData.optJSONObject("data");
//			if (data == null) return;
//
//			SteamGameDetails details = new SteamGameDetails();
//			details.setAppid(game.getGameId());
//			details.setName(data.optString("name"));
//			details.setType(data.optString("type"));
//			details.setShortDescription(truncate(data.optString("short_description"), 5000)); // prevent SQL overflow
//			details.setHeaderImage(data.optString("header_image"));
//			details.setCapsuleImage(data.optString("capsule_image"));
//			details.setCapsuleImageV5(data.optString("capsule_imagev5"));
//
//			// Format requirements
//			details.setPcRequirements(formatRequirementsJson(data.optJSONObject("pc_requirements")));
//			details.setMacRequirements(formatRequirementsJson(data.optJSONObject("mac_requirements")));
//			details.setLinuxRequirements(formatRequirementsJson(data.optJSONObject("linux_requirements")));
//
//			// Store raw JSON
//			details.setRawJson(data.toString());
//			details.setSteamGame(game);
//
//			steamGameDetailsRepository.save(details);
//
//		} catch (Exception e) {
//			System.err.println("⚠️ Error fetching AppID " + game.getGameId() + ": " + e.getMessage());
//		}
//	}
//
//	/**
//	 * Convert Steam's HTML/JSON requirements into a readable JSON object.
//	 */
//	private String formatRequirementsJson(JSONObject req) {
//		if (req == null) return "{}";
//
//		JSONObject formatted = new JSONObject();
//
//		String minimum = req.optString("minimum");
//		if (minimum != null && !minimum.isBlank()) {
//			formatted.put("minimum", minimum.replaceAll("<[^>]*>", "").trim());
//		}
//
//		String recommended = req.optString("recommended");
//		if (recommended != null && !recommended.isBlank()) {
//			formatted.put("recommended", recommended.replaceAll("<[^>]*>", "").trim());
//		}
//
//		return formatted.toString();
//	}
//
//	/**
//	 * Sleep with a countdown timer
//	 */
//	private void countdownSleep(long millis) {
//		long interval = 100; // 100ms updates
//		long remaining = millis;
//
//		while (remaining > 0) {
//			System.out.print("\r⏳ Sleeping for " + (remaining / 1000.0) + " seconds... ");
//			try {
//				Thread.sleep(Math.min(interval, remaining));
//			} catch (InterruptedException e) {
//				Thread.currentThread().interrupt();
//				break;
//			}
//			remaining -= interval;
//		}
//		System.out.println("\r✅ Resuming next request...          ");
//	}
//
//	private String truncate(String text, int maxLength) {
//		if (text == null) return "";
//		return text.length() > maxLength ? text.substring(0, maxLength) : text;
//	}
//}
//
//
//

package com.example.Game_Details.Service;

import com.example.Game_Details.Entity.SteamGameDetails;
import com.example.Game_Details.Entity.SteamGames;
import com.example.Game_Details.Repository.SteamGameDetailsRepository;
import com.example.Game_Details.Repository.SteamGameRepository;
import org.json.JSONObject;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class SteamGameDetailsService {

	private final SteamGameRepository steamGameRepository;
	private final SteamGameDetailsRepository steamGameDetailsRepository;
	private final RestTemplate restTemplate;

	public SteamGameDetailsService(SteamGameRepository steamGameRepository,
								   SteamGameDetailsRepository steamGameDetailsRepository,
								   RestTemplate restTemplate) {
		this.steamGameRepository = steamGameRepository;
		this.steamGameDetailsRepository = steamGameDetailsRepository;
		this.restTemplate = restTemplate;
	}

	@Async
	public void fetchAllInBatches(int batchSize) {
		List<SteamGames> allGames = steamGameRepository.findAll();
		int total = allGames.size();
		System.out.println("Total games to process: " + total);

		for (int i = 0; i < total; i += batchSize) {
			int end = Math.min(i + batchSize, total);
			List<SteamGames> batch = allGames.subList(i, end);

			for (SteamGames game : batch) {
				fetchAndStoreGameDetail(game);
				countdownSleep(1500); // 1.5s delay between requests
			}

			System.out.println("✅ Processed batch " + (i / batchSize + 1));
		}

		System.out.println("🎯 Completed all game details!");
	}

	private void fetchAndStoreGameDetail(SteamGames game) {
		SteamGameDetails details = steamGameDetailsRepository.findByAppid(game.getGameId())
				.orElse(new SteamGameDetails());

		details.setAppid(game.getGameId());
		details.setSteamGame(game);

		try {
			String url = "https://store.steampowered.com/api/appdetails?appids=" + game.getGameId();
			String response;

			try {
				response = restTemplate.getForObject(url, String.class);
			} catch (HttpClientErrorException.TooManyRequests e) {
				System.out.println("429 Too Many Requests for AppID " + game.getGameId() + ". Waiting 10s...");
				countdownSleep(10000); // wait 10s with countdown
				response = restTemplate.getForObject(url, String.class); // retry once
			}

			if (response == null || response.isBlank()) {
				details.setStatus("FAILED");
				steamGameDetailsRepository.save(details);
				return;
			}

			JSONObject root = new JSONObject(response);
			JSONObject appData = root.optJSONObject(String.valueOf(game.getGameId()));

			if (appData == null || !appData.optBoolean("success")) {
				details.setStatus("FAILED");
				steamGameDetailsRepository.save(details);
				return;
			}

			JSONObject data = appData.optJSONObject("data");
			if (data == null) {
				details.setStatus("FAILED");
				steamGameDetailsRepository.save(details);
				return;
			}

			// Fill in details
			details.setName(data.optString("name"));
			details.setType(data.optString("type"));
			details.setShortDescription(truncate(data.optString("short_description"), 5000));
			details.setHeaderImage(data.optString("header_image"));
			details.setCapsuleImage(data.optString("capsule_image"));
			details.setCapsuleImageV5(data.optString("capsule_imagev5"));
			details.setPcRequirements(formatRequirementsJson(data.optJSONObject("pc_requirements")));
			details.setMacRequirements(formatRequirementsJson(data.optJSONObject("mac_requirements")));
			details.setLinuxRequirements(formatRequirementsJson(data.optJSONObject("linux_requirements")));
			details.setRawJson(data.toString());
			details.setStatus("SUCCESS");

		} catch (Exception e) {
			System.err.println("⚠️ Error fetching AppID " + game.getGameId() + ": " + e.getMessage());
			details.setStatus("FAILED");
		}

		steamGameDetailsRepository.save(details);
	}

	private String formatRequirementsJson(JSONObject req) {
		if (req == null) return "{}";

		JSONObject formatted = new JSONObject();
		String minimum = req.optString("minimum");
		if (minimum != null && !minimum.isBlank()) {
			formatted.put("minimum", minimum.replaceAll("<[^>]*>", "").trim());
		}
		String recommended = req.optString("recommended");
		if (recommended != null && !recommended.isBlank()) {
			formatted.put("recommended", recommended.replaceAll("<[^>]*>", "").trim());
		}

		return formatted.toString();
	}

	private void countdownSleep(long millis) {
		long interval = 100; // 100ms updates
		long remaining = millis;

		while (remaining > 0) {
			System.out.print("\r⏳ Sleeping for " + (remaining / 1000.0) + " seconds... ");
			try {
				Thread.sleep(Math.min(interval, remaining));
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				break;
			}
			remaining -= interval;
		}
		System.out.println("\r✅ Resuming next request...          ");
	}

	private String truncate(String text, int maxLength) {
		if (text == null) return "";
		return text.length() > maxLength ? text.substring(0, maxLength) : text;
	}
}
