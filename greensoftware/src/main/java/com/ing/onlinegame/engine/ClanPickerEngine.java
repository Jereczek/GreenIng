package com.ing.onlinegame.engine;

import com.ing.model.onlinegame.Clan;
import com.ing.model.onlinegame.Players;
import com.ing.onlinegame.engine.collection.TreePriorityQueueClansCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class ClanPickerEngine {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClanPickerEngine.class);

    private final LinkedList<List<Clan>> matchmakingResult = new LinkedList<>();

    private final TreePriorityQueueClansCollection clansCollection;

    private final Players players;

    private final String engineId = String.valueOf(UUID.randomUUID());

    public ClanPickerEngine(Players players, TreePriorityQueueClansCollection treePriorityQueueClansCollection) {
        LOGGER.debug("EnginId: {} | ClanPickerEngine initialized.", engineId);
        this.players = players;
        this.clansCollection = treePriorityQueueClansCollection;
    }

    public void computeMatchmaking() {
        LOGGER.debug("EnginId: {} | Compute matchmaking started.", engineId);
        while (clansCollection.isNotEmpty()) {
            Optional<Clan> theStrongestClan = clansCollection.poolTheStrongestClanInCollection();
            if (theStrongestClan.isEmpty()) {
                break;
            }
            ArrayList<Clan> packedGroup = new ArrayList<>(players.getGroupCount());
            packedGroup.add(theStrongestClan.get());

            int currentGameHeadCount = theStrongestClan.get().getNumberOfPlayers();
            int remainingAvailableSlots = players.getGroupCount() - currentGameHeadCount;

            while(currentGameHeadCount < players.getGroupCount()) {
                Optional<Clan> nextClan = findAndPoolNextAvailableClan(remainingAvailableSlots);
                if (nextClan.isPresent()) {
                    packedGroup.add(nextClan.get());
                    currentGameHeadCount += nextClan.get().getNumberOfPlayers();
                } else {
                    break;
                }
            }
            addGroupToMatchmakingResult(packedGroup);
            LOGGER.debug("EnginId: {} | Packed group: {}", engineId, packedGroup);
        }
        LOGGER.debug("EnginId: {} | Compute matchmaking finished.", engineId);
    }

    public LinkedList<List<Clan>> getMatchmakingResult() {
        return matchmakingResult;
    }

    private Optional<Clan> findAndPoolNextAvailableClan(int remainingAvailableSlots) {
        LOGGER.debug("EnginId: {} | FindAndPoolNextAvailableClan for remaining {} available slots.",
                engineId, remainingAvailableSlots);
        if (remainingAvailableSlots <= 0) {
            return Optional.empty();
        }
        Optional<Clan> nextAvailableClanForGivenSize =
                clansCollection.poolTheStrongestClanForAvailableSize(remainingAvailableSlots);
        if (nextAvailableClanForGivenSize.isPresent()) {
            return nextAvailableClanForGivenSize;
        }
        return findAndPoolNextAvailableClan(--remainingAvailableSlots);
    }

    private void addGroupToMatchmakingResult(List<Clan> group) {
        matchmakingResult.add(group);
    }
}