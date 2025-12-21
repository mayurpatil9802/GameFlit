package com.example.Game_Details.Entity;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "steam_games")
public class SteamGames {
	@Id
	private Long gameId;
	private String name;

	public SteamGames(Long gameId, String name) {
		this.gameId = gameId;
		this.name = name;
	}

	public SteamGames() {
	}

	public Long getGameId() {
		return gameId;
	}

	public void setGameId(Long gameId) {
		this.gameId = gameId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "SteamGames{" +
				"gameId=" + gameId +
				", name='" + name + '\'' +
				'}';
	}
}
