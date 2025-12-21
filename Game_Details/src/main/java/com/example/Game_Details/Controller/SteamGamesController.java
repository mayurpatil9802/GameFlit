package com.example.Game_Details.Controller;

import com.example.Game_Details.Entity.SteamGames;
import com.example.Game_Details.Service.SteamGameDetailsService;
import com.example.Game_Details.Service.SteamGameService;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/steam")
public class SteamGamesController {
	private  SteamGameService steamGameService;
	private  SteamGameDetailsService steamGameDetailsService;

	public void SteamAppController(SteamGameService steamAppService) {
		this.steamGameService = steamAppService;
	}

	public SteamGamesController(SteamGameService steamGameService) {
		this.steamGameService = steamGameService;
	}

	// 1️⃣ Fetch & Store All Steam Apps
	@PostMapping("/fetch")
	public String fetchAndStore() throws Exception {
		steamGameService.saveSteamAppsToDatabaseAsync();
		return "Steam apps saved successfully!";
	}

	// 2️⃣ Get All Stored Apps
	@GetMapping("/apps")
	public List<SteamGames> getAllApps() {
		return steamGameService.getAllApps();
	}

	// 3️⃣ Get Single App by ID
	@GetMapping("/apps/{appid}")
	public SteamGames getApp(@PathVariable Long appid) {
		return steamGameService.getAppById(appid);
	}

}
