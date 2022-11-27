package com.game.service;

import com.game.model.Player;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface PlayerService {
    public void addPlayer(Player player);

    public void updatePlayer(Long id, Player player);

    public void removePlayer(Long id);

    public Optional<Player> getPlayerById(Long id);

    public List<Player> getPlayersList(Map<String, String> allRequestParams);

    public List<Player> getSortedAndOrderedPlayersList(Map<String, String> allRequestParams);
}
