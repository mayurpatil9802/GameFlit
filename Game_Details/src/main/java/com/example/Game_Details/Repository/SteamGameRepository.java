package com.example.Game_Details.Repository;

import com.example.Game_Details.Entity.SteamGames;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SteamGameRepository extends JpaRepository<SteamGames , Long> {
}
