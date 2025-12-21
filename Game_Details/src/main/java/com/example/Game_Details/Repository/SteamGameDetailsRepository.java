package com.example.Game_Details.Repository;

import com.example.Game_Details.Entity.SteamGameDetails;
import com.example.Game_Details.Entity.SteamGames;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SteamGameDetailsRepository extends JpaRepository<SteamGameDetails , Long> {
	boolean existsByAppid(Long appid);
	Optional<SteamGameDetails> findByAppid(Long appid);

//	@Query("SELECT g FROM SteamGames g WHERE g.gameId not IN (SELECT d.appid FROM SteamGameDetails d)")
	@Query("SELECT g FROM SteamGames g WHERE g.gameId IN (SELECT d.appid FROM SteamGameDetails d where d.status = \"FAILED\")")
	List<SteamGames> findGamesNotInDetails();

}
