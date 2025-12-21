//package com.example.Game_Details.Service;
//
//import com.example.Game_Details.Entity.SteamGameDetails;
//import com.example.Game_Details.Entity.SteamGames;
//import com.example.Game_Details.Repository.SteamGameDetailsRepository;
//import com.example.Game_Details.Repository.SteamGameRepository;
//import org.json.JSONObject;
//import org.springframework.stereotype.Service;
//import org.springframework.web.client.RestTemplate;
//
//import org.springframework.web.client.HttpClientErrorException;
//
//import java.util.List;
//import java.util.concurrent.*;
//
//@Service
//public class SteamGameDetailsUpdateService {
//
//	private final SteamGameRepository gameRepository;
//	private final SteamGameDetailsRepository detailsRepository;
//	private final RestTemplate restTemplate;
//	private final ExecutorService executor = Executors.newSingleThreadExecutor();
//
//	private static final long API_TIMEOUT_MS = 5000; // 5 seconds per request
//	private static final long RATE_LIMIT_DELAY_MS = 1500; // delay between requests
//
//	public SteamGameDetailsUpdateService(SteamGameRepository gameRepository,
//										 SteamGameDetailsRepository detailsRepository,
//										 RestTemplate restTemplate) {
//		this.gameRepository = gameRepository;
//		this.detailsRepository = detailsRepository;
//		this.restTemplate = restTemplate;
//	}
//
//	public void fetchMissingGames() {
//		List<SteamGames> missingGames = detailsRepository.findGamesNotInDetails();
//		System.out.println("Total missing games: " + missingGames.size());
//
//		for (SteamGames game : missingGames) {
//			try {
//				Future<Boolean> future = executor.submit(() -> fetchAndSaveGame(game));
//				boolean success = future.get(API_TIMEOUT_MS, TimeUnit.MILLISECONDS);
//
//				if (!success) {
//					System.out.println("Skipping AppID " + game.getGameId() + " due to failure or invalid response.");
//				}
//			} catch (TimeoutException e) {
//				System.out.println("⏱ Timeout for AppID " + game.getGameId() + ", skipping...");
//			} catch (Exception e) {
//				System.out.println("⚠ Error for AppID " + game.getGameId() + ": " + e.getMessage());
//			}
//
//			try {
//				Thread.sleep(RATE_LIMIT_DELAY_MS);
//			} catch (InterruptedException e) {
//				Thread.currentThread().interrupt();
//			}
//		}
//
//		System.out.println("Finished fetching all missing games!");
//		executor.shutdown();
//	}
//
//	private boolean fetchAndSaveGame(SteamGames game) {
//		SteamGameDetails details = detailsRepository.findByAppid(game.getGameId())
//				.orElse(new SteamGameDetails());
//
//		details.setAppid(game.getGameId());
//		details.setSteamGame(game);
//
//		try {
//			if (detailsRepository.existsByAppid(game.getGameId())) {
//				details.setStatus("SUCCESS"); // Already exists, mark as success
//				detailsRepository.save(details);
//				return true;
//			}
//
//			String url = "https://store.steampowered.com/api/appdetails?appids=" + game.getGameId();
//			String response;
//
//			try {
//				response = restTemplate.getForObject(url, String.class);
//			} catch (HttpClientErrorException.TooManyRequests e) {
//				System.out.println("429 Too Many Requests for AppID " + game.getGameId() + ", skipping...");
//				details.setStatus("FAILED");
//				detailsRepository.save(details);
//				return false;
//			}
//
//			if (response == null || response.isBlank()) {
//				details.setStatus("FAILED");
//				detailsRepository.save(details);
//				return false;
//			}
//
//			JSONObject root = new JSONObject(response);
//			JSONObject appData = root.optJSONObject(String.valueOf(game.getGameId()));
//			if (appData == null || !appData.optBoolean("success")) {
//				details.setStatus("FAILED");
//				detailsRepository.save(details);
//				return false;
//			}
//
//			JSONObject data = appData.optJSONObject("data");
//			if (data == null) {
//				details.setStatus("FAILED");
//				detailsRepository.save(details);
//				return false;
//			}
//
//			// Fill details
//			details.setName(data.optString("name"));
//			details.setType(data.optString("type"));
//			details.setShortDescription(truncate(data.optString("short_description"), 5000));
//			details.setHeaderImage(data.optString("header_image"));
//			details.setCapsuleImage(data.optString("capsule_image"));
//			details.setCapsuleImageV5(data.optString("capsule_imagev5"));
//			details.setPcRequirements(formatRequirementsJson(data.optJSONObject("pc_requirements")));
//			details.setMacRequirements(formatRequirementsJson(data.optJSONObject("mac_requirements")));
//			details.setLinuxRequirements(formatRequirementsJson(data.optJSONObject("linux_requirements")));
//			details.setRawJson(data.toString());
//
//			// Set success status
//			details.setStatus("SUCCESS");
//
//			detailsRepository.save(details);
//			return true;
//
//		} catch (Exception e) {
//			System.err.println("⚠️ Error fetching AppID " + game.getGameId() + ": " + e.getMessage());
//			details.setStatus("FAILED");
//			detailsRepository.save(details);
//			return false;
//		}
//	}
//
//	private String formatRequirementsJson(JSONObject req) {
//		if (req == null) return "{}";
//
//		JSONObject formatted = new JSONObject();
//		String minimum = req.optString("minimum");
//		if (minimum != null && !minimum.isBlank()) {
//			formatted.put("minimum", minimum.replaceAll("<[^>]*>", "").trim());
//		}
//		String recommended = req.optString("recommended");
//		if (recommended != null && !recommended.isBlank()) {
//			formatted.put("recommended", recommended.replaceAll("<[^>]*>", "").trim());
//		}
//		return formatted.toString();
//	}
//
//	private String truncate(String text, int maxLength) {
//		if (text == null) return "";
//		return text.length() > maxLength ? text.substring(0, maxLength) : text;
//	}
//}
//

package com.example.Game_Details.Service;

import com.example.Game_Details.Entity.SteamGameDetails;
import com.example.Game_Details.Entity.SteamGames;
import com.example.Game_Details.Repository.SteamGameDetailsRepository;
import com.example.Game_Details.Repository.SteamGameRepository;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.concurrent.*;

@Service
public class SteamGameDetailsUpdateService {

	private final SteamGameRepository gameRepository;
	private final SteamGameDetailsRepository detailsRepository;
	private final RestTemplate restTemplate;
	private final ExecutorService executor = Executors.newSingleThreadExecutor();

	private static final long API_TIMEOUT_MS = 5000; // 5 seconds per request
	private static final long RATE_LIMIT_DELAY_MS = 1500; // delay between requests

	public SteamGameDetailsUpdateService(SteamGameRepository gameRepository,
										 SteamGameDetailsRepository detailsRepository,
										 RestTemplate restTemplate) {
		this.gameRepository = gameRepository;
		this.detailsRepository = detailsRepository;
		this.restTemplate = restTemplate;
	}

	public void fetchMissingGames() {
		List<SteamGames> missingGames = detailsRepository.findGamesNotInDetails();
		System.out.println("Total missing games: " + missingGames.size());

		for (SteamGames game : missingGames) {
			try {
				Future<Boolean> future = executor.submit(() -> fetchAndSaveGame(game));
				boolean success = future.get(API_TIMEOUT_MS, TimeUnit.MILLISECONDS);

				if (!success) {
					System.out.println("Skipping AppID " + game.getGameId() + " due to failure or invalid response.");
				}
			} catch (TimeoutException e) {
				System.out.println("⏱ Timeout for AppID " + game.getGameId() + ", skipping...");
			} catch (Exception e) {
				System.out.println("⚠ Error for AppID " + game.getGameId() + ": " + e.getMessage());
			}

			try {
				Thread.sleep(RATE_LIMIT_DELAY_MS);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		}

		System.out.println("Finished fetching all missing games!");
		executor.shutdown();
	}

	private boolean fetchAndSaveGame(SteamGames game) {
		SteamGameDetails details = detailsRepository.findByAppid(game.getGameId())
				.orElse(new SteamGameDetails());

		details.setAppid(game.getGameId());
		details.setSteamGame(game);

		try {
			String url = "https://store.steampowered.com/api/appdetails?appids=" + game.getGameId();
			String response;

			try {
				response = restTemplate.getForObject(url, String.class);
			} catch (HttpClientErrorException.TooManyRequests e) {
				System.out.println("429 Too Many Requests for AppID " + game.getGameId() + ", skipping...");
				details.setStatus("FAILED");
				detailsRepository.save(details);
				return false;
			}

			if (response == null || response.isBlank()) {
				System.out.println("⚠ Empty response for AppID " + game.getGameId());
				return handleBackupApi(details, game.getName());
			}

			JSONObject root = new JSONObject(response);
			JSONObject appData = root.optJSONObject(String.valueOf(game.getGameId()));
			if (appData == null || !appData.optBoolean("success")) {
				System.out.println("⚠ Invalid or failed Steam data for AppID " + game.getGameId());
				return handleBackupApi(details, game.getName());
			}

			JSONObject data = appData.optJSONObject("data");
			if (data == null) {
				System.out.println("⚠ No data object found for AppID " + game.getGameId());
				return handleBackupApi(details, game.getName());
			}

			// Fill from Steam API
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
			detailsRepository.save(details);
			return true;

		} catch (Exception e) {
			System.err.println("⚠️ Steam API error for " + game.getName() + ": " + e.getMessage());
			return handleBackupApi(details, game.getName());
		}
	}

	private boolean handleBackupApi(SteamGameDetails details, String gameName) {
		try {
			String slugName = gameName
					.replaceAll("[:,'\"!@#$%^&*()\\[\\]{}<>?/\\\\|+=]", "")
					.trim()
					.replaceAll("\\s+", "-");

			String backupUrl = "http://localhost:8080/api/game/" + slugName;
			System.out.println("🔁 Trying backup API: " + backupUrl);

			String backupResponse = restTemplate.getForObject(backupUrl, String.class);
			if (backupResponse == null || backupResponse.isBlank()) {
				details.setStatus("FAILED");
				detailsRepository.save(details);
				return false;
			}

			if (backupResponse.contains("error") || backupResponse.contains("Status=404")) {
				details.setStatus("FAILED");
				detailsRepository.save(details);
				return false;
			}

			JSONObject backupData = new JSONObject(backupResponse);

			// fill data from backup API
			String title = backupData.optString("title", "");
			if (title.toLowerCase().contains("system requirements")) {
				title = title.replace("System Requirements", "").trim();
			}

			details.setName(title);
			details.setShortDescription(null);
			details.setHeaderImage(backupData.optString("imageUrl", ""));
			details.setCapsuleImage(null);
			details.setCapsuleImageV5(null);
			details.setPcRequirements(new JSONObject()
					.put("minimumRequirements", backupData.optJSONObject("minimumRequirements"))
					.put("recommendedRequirements", backupData.optJSONObject("recommendedRequirements"))
					.toString());
			details.setMacRequirements(null);
			details.setLinuxRequirements(null);
			details.setRawJson(backupResponse);
			details.setStatus("SUCCESS");
			detailsRepository.save(details);

			System.out.println("✅ Backup API success for " + title);
			return true;

		} catch (Exception e) {
			System.err.println("❌ Backup API failed for " + gameName + ": " + e.getMessage());
			details.setStatus("FAILED");
			detailsRepository.save(details);
			return false;
		}
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

	private String truncate(String text, int maxLength) {
		if (text == null) return "";
		return text.length() > maxLength ? text.substring(0, maxLength) : text;
	}
}
