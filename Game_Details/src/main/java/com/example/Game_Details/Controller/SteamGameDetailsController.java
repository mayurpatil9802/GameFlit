package com.example.Game_Details.Controller;
import com.example.Game_Details.Service.SteamGameDetailsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SteamGameDetailsController {

	private final SteamGameDetailsService steamGameDetailsService;


	public SteamGameDetailsController(SteamGameDetailsService steamGameDetailsService) {
		this.steamGameDetailsService = steamGameDetailsService;
	}

	@GetMapping("/fetch-details")
	public String fetchDetails() throws InterruptedException {
		steamGameDetailsService.fetchAllInBatches(50); // batch size 50
		return "Fetching started in background!";
	}
}