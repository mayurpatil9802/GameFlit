package com.example.Game_Details.Entity;

import jakarta.persistence.*;

@Entity
@Table(name = "steam_game_details")
public class SteamGameDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "appid", unique = true)
	private Long appid;

	@Column(columnDefinition = "TEXT")
	private String name;

	private String type;

	@Column(columnDefinition = "TEXT")
	private String shortDescription;

	@Column(columnDefinition = "TEXT")
	private String headerImage;

	@Column(columnDefinition = "TEXT")
	private String capsuleImage;

	@Column(columnDefinition = "TEXT")
	private String capsuleImageV5;

	@Column(columnDefinition = "LONGTEXT")
	private String pcRequirements;

	@Column(columnDefinition = "LONGTEXT")
	private String macRequirements;

	@Column(columnDefinition = "LONGTEXT")
	private String linuxRequirements;

	@Column(columnDefinition = "LONGTEXT")
	private String rawJson;

	// ✅ Foreign key to SteamGames
	@ManyToOne
	@JoinColumn(name = "steam_game_id")
	private SteamGames steamGame;

	// ✅ New status column
	@Column(length = 20)
	private String status; // e.g., SUCCESS, FAILED

	public SteamGameDetails() {
		this.status = "PENDING"; // default
	}

	public SteamGameDetails(Long id, Long appid, String name, String type, String shortDescription, String headerImage,
							String capsuleImage, String capsuleImageV5, String pcRequirements, String macRequirements,
							String linuxRequirements, String rawJson, SteamGames steamGame, String status) {
		this.id = id;
		this.appid = appid;
		this.name = name;
		this.type = type;
		this.shortDescription = shortDescription;
		this.headerImage = headerImage;
		this.capsuleImage = capsuleImage;
		this.capsuleImageV5 = capsuleImageV5;
		this.pcRequirements = pcRequirements;
		this.macRequirements = macRequirements;
		this.linuxRequirements = linuxRequirements;
		this.rawJson = rawJson;
		this.steamGame = steamGame;
		this.status = status != null ? status : "PENDING";
	}

	// --- Getters & Setters ---
	public Long getId() { return id; }
	public void setId(Long id) { this.id = id; }

	public Long getAppid() { return appid; }
	public void setAppid(Long appid) { this.appid = appid; }

	public String getName() { return name; }
	public void setName(String name) { this.name = name; }

	public String getType() { return type; }
	public void setType(String type) { this.type = type; }

	public String getShortDescription() { return shortDescription; }
	public void setShortDescription(String shortDescription) { this.shortDescription = shortDescription; }

	public String getHeaderImage() { return headerImage; }
	public void setHeaderImage(String headerImage) { this.headerImage = headerImage; }

	public String getCapsuleImage() { return capsuleImage; }
	public void setCapsuleImage(String capsuleImage) { this.capsuleImage = capsuleImage; }

	public String getCapsuleImageV5() { return capsuleImageV5; }
	public void setCapsuleImageV5(String capsuleImageV5) { this.capsuleImageV5 = capsuleImageV5; }

	public String getPcRequirements() { return pcRequirements; }
	public void setPcRequirements(String pcRequirements) { this.pcRequirements = pcRequirements; }

	public String getMacRequirements() { return macRequirements; }
	public void setMacRequirements(String macRequirements) { this.macRequirements = macRequirements; }

	public String getLinuxRequirements() { return linuxRequirements; }
	public void setLinuxRequirements(String linuxRequirements) { this.linuxRequirements = linuxRequirements; }

	public String getRawJson() { return rawJson; }
	public void setRawJson(String rawJson) { this.rawJson = rawJson; }

	public SteamGames getSteamGame() { return steamGame; }
	public void setSteamGame(SteamGames steamGame) { this.steamGame = steamGame; }

	public String getStatus() { return status; }
	public void setStatus(String status) { this.status = status; }

	@Override
	public String toString() {
		return "SteamGameDetails{" +
				"id=" + id +
				", appid=" + appid +
				", name='" + name + '\'' +
				", type='" + type + '\'' +
				", shortDescription='" + shortDescription + '\'' +
				", headerImage='" + headerImage + '\'' +
				", capsuleImage='" + capsuleImage + '\'' +
				", capsuleImageV5='" + capsuleImageV5 + '\'' +
				", pcRequirements='" + pcRequirements + '\'' +
				", macRequirements='" + macRequirements + '\'' +
				", linuxRequirements='" + linuxRequirements + '\'' +
				", rawJson='" + rawJson + '\'' +
				", steamGame=" + steamGame +
				", status='" + status + '\'' +
				'}';
	}
}
