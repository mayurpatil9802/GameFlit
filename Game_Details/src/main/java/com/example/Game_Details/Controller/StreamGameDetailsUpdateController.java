package com.example.Game_Details.Controller;

import com.example.Game_Details.Service.SteamGameDetailsUpdateService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StreamGameDetailsUpdateController {
	private final SteamGameDetailsUpdateService updateService;

	public StreamGameDetailsUpdateController(SteamGameDetailsUpdateService updateService) {
		this.updateService = updateService;
	}

	@GetMapping("/fetch-missing-details")
	public String fetchMissingGameDetails() {
		updateService.fetchMissingGames();
		return "Fetching missing game details started!";
	}
}
