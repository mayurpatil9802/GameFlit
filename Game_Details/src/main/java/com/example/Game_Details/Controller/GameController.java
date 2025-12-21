package com.example.Game_Details.Controller;


import com.example.Game_Details.Service.ScraperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/game")
public class GameController {
	@Autowired
	private ScraperService scraperService;

	@GetMapping("/{slug}")
	public Map<String, Object> getGameDetails(@PathVariable String slug) {
		return scraperService.fetchGameDetails(slug);
	}
}
