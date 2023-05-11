package com.ing.onlinegame;

import com.ing.model.onlinegame.Clan;
import com.ing.model.onlinegame.Players;

import com.ing.onlinegame.engine.ClanPickerEngine;
import com.ing.onlinegame.engine.ClanPickerEngineFactory;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Singleton
public class OnlineGameService {
    private static final Logger LOGGER = LoggerFactory.getLogger(OnlineGameService.class);

    @Inject
    private ClanPickerEngineFactory clanPickerEngineFactory;

    public List<List<Clan>> computeMatchmaking(Players players) {
        LOGGER.debug("OnlineGameService | Matchmaking for {} Clans with group count {} started.",
                players.getClans().size(), players.getGroupCount());
        ClanPickerEngine clanPickerEngine = clanPickerEngineFactory.buildClanPicker(players);
        clanPickerEngine.computeMatchmaking();
        LOGGER.debug("OnlineGameService | Matchmaking for {} Clans with group count {} finished.",
                players.getClans().size(), players.getGroupCount());
        return clanPickerEngine.getMatchmakingResult();
    }
}
