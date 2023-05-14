package com.ing.onlinegame.engine;

import com.ing.model.onlinegame.Players;
import com.ing.onlinegame.engine.collection.TreePriorityQueueClansCollection;
import io.micronaut.context.annotation.Factory;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

@Factory
public class ClanPickerEngineFactory {

    @Inject
    private ClanPickerEngineUtil clanPickerEngineUtil;

    @Singleton
    public ClanPickerEngine buildClanPicker(Players players) {
        return new ClanPickerEngine(players, new TreePriorityQueueClansCollection(players, clanPickerEngineUtil));
    }
}
