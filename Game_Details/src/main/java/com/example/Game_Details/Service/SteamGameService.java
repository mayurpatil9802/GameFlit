package com.example.Game_Details.Service;


import com.example.Game_Details.Entity.SteamGames;
import com.example.Game_Details.Repository.SteamGameRepository;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Service
public class SteamGameService {

	private final SteamGameRepository steamGameRepository;

	public SteamGameService(SteamGameRepository steamGameRepository) {
		this.steamGameRepository = steamGameRepository;
	}

	@Async
	public void saveSteamAppsToDatabaseAsync() {
		saveSteamAppsToDatabase(); // run in background
	}

	public void saveSteamAppsToDatabase() {
		String apiUrl = "https://api.steampowered.com/ISteamApps/GetAppList/v2/";
		System.out.println("Fetching large Steam app list...");

		try (InputStream inputStream = new URL(apiUrl).openStream()) {
			JsonFactory factory = new JsonFactory();
			JsonParser parser = factory.createParser(inputStream);

			List<SteamGames> batch = new ArrayList<>();
			int batchSize = 1000;
			int count = 0;

			// Move to the "apps" array
			while (!parser.isClosed()) {
				JsonToken token = parser.nextToken();

				if (JsonToken.FIELD_NAME.equals(token) && "apps".equals(parser.getCurrentName())) {
					parser.nextToken(); // move to START_ARRAY
					break;
				}
			}

			// Parse each object in the "apps" array
			while (parser.nextToken() == JsonToken.START_OBJECT) {
				Long appId = null;
				String name = null;

				while (parser.nextToken() != JsonToken.END_OBJECT) {
					String fieldName = parser.getCurrentName();
					parser.nextToken();
					if ("appid".equals(fieldName)) {
						appId = parser.getLongValue();
					} else if ("name".equals(fieldName)) {
						name = parser.getValueAsString();
					}
				}

				if (appId != null && name != null && !name.isBlank()) {
					batch.add(new SteamGames(appId, name));
					count++;
				}

				// Save in batches
				if (batch.size() >= batchSize) {
					steamGameRepository.saveAll(batch);
					batch.clear();
					System.out.println("Saved " + count + " records...");
				}
			}

			// Save any remaining records
			if (!batch.isEmpty()) {
				steamGameRepository.saveAll(batch);
				System.out.println("Final batch saved. Total records: " + count);
			}

			System.out.println("✅ All Steam app data stored successfully!");
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Error fetching or storing Steam data: " + e.getMessage());
		}
	}

	// 🧩 Fetch data from Steam API
	public List<SteamGames> fetchSteamApps() throws Exception {
		String apiUrl = "https://api.steampowered.com/ISteamApps/GetAppList/v2/";
		URL url = new URL(apiUrl);

		BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
		StringBuilder response = new StringBuilder();
		String inputLine;

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();

		JSONObject json = new JSONObject(response.toString());
		JSONArray apps = json.getJSONObject("applist").getJSONArray("apps");

		List<SteamGames> appList = new ArrayList<>();

		for (int i = 0; i < apps.length(); i++) {
			JSONObject appObj = apps.getJSONObject(i);
			SteamGames app = new SteamGames();
			app.setGameId(appObj.getLong("appid"));
			app.setName(appObj.optString("name", ""));
			appList.add(app);
		}

		return appList;
	}

	// 💾 Store all apps in MySQL

//	public void saveSteamAppsToDatabase() {
//		String url = "https://api.steampowered.com/ISteamApps/GetAppList/v2/";
//		Map response = restTemplate.getForObject(url, Map.class);
//		Map applist = (Map) response.get("applist");
//		List<Map<String, Object>> apps = (List<Map<String, Object>>) applist.get("apps");
//
//		List<SteamGames> steamGames = apps.stream()
//				.map(app -> new SteamGames(
//						(Long) app.get("appid"),
//						(String) app.get("name")
//				))
//				.toList();
//
//		steamGameRepository.saveAll(steamGames);
//	}

	// 📂 Retrieve all apps from DB
	public List<SteamGames> getAllApps() {
		return steamGameRepository.findAll();
	}

	// 🔍 Find by appid
	public SteamGames getAppById(Long appid) {
		return steamGameRepository.findById(appid).orElse(null);
	}
}
