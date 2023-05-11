package com.ing.onlinegame.engine.collection;

import com.ing.model.onlinegame.Clan;
import com.ing.model.onlinegame.Players;
import com.ing.onlinegame.engine.ClanPickerEngineUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class TreePriorityQueueClansCollection {

    private static final Logger LOGGER = LoggerFactory.getLogger(TreePriorityQueueClansCollection.class);
    int currentSize = 0;
    private final ClanPickerEngineUtil clanPickerEngineUtil;
    TreeMap<Integer, PriorityQueue<Clan>> clansCollection = new TreeMap<>(Comparator.naturalOrder());

    public TreePriorityQueueClansCollection(Players players, ClanPickerEngineUtil clanPickerEngineUtil) {
        LOGGER.debug("TreePriorityQueueClansCollection | Creating collection for {} clans.", players.getClans().size());
        this.clanPickerEngineUtil = clanPickerEngineUtil;

        players.getClans().forEach(
                this::addClanToCollection);
    }

    public boolean isNotEmpty() {
        return currentSize > 0;
    }

    public Optional<Clan> poolTheStrongestClanInCollection() {
        LOGGER.debug("TreePriorityQueueClansCollection | Pooling the strongest clan from whole collection");
        PriorityQueue<Clan> entryOfPriorityQueue = clansCollection.get(findQueueKeyWithTheStrongestClan());
        return poolCounterWrapper(Optional.ofNullable(entryOfPriorityQueue.poll()));
    }

    public Optional<Clan> poolTheStrongestClanForAvailableSize(int availableSize) {
        LOGGER.debug("TreePriorityQueueClansCollection | Pooling the strongest clan with size {}.", availableSize);
        PriorityQueue<Clan> priorityQueue = clansCollection.get(availableSize);
        return priorityQueue != null ? poolCounterWrapper(Optional.ofNullable(priorityQueue.poll())) : Optional.empty();
    }

    private Optional<Clan> poolCounterWrapper(Optional<Clan> optionalClan) {
        if (optionalClan.isPresent()) {
            currentSize--;
        }
        return optionalClan;
    }

    private void addClanToCollection(Clan clan) {
        LOGGER.debug("TreePriorityQueueClansCollection | Adding Clan of power {} and {} players to collection.",
                clan.getPoints(), clan.getNumberOfPlayers());
        PriorityQueue<Clan> priorityQueue = clansCollection.get(clan.getNumberOfPlayers());
        if (priorityQueue == null) {
            priorityQueue = createPriorityQueueWithCorrespondingNumberOfPlayers(clan.getNumberOfPlayers());
        }
        priorityQueue.add(clan);
        currentSize++;
    }

    private PriorityQueue<Clan> createPriorityQueueWithCorrespondingNumberOfPlayers(int clanSize) {
        LOGGER.debug("TreePriorityQueueClansCollection | Creating new queue for clans with size: {}", clanSize);
        PriorityQueue<Clan> newPriorityQueue = new PriorityQueue<>(clanPickerEngineUtil.priorityClanDescendingOrdering);
        clansCollection.put(clanSize, newPriorityQueue);
        return newPriorityQueue;
    }

    private int findQueueKeyWithTheStrongestClan() {
        AtomicInteger keyWithTheQueueThatContainsTheStrongestClan = new AtomicInteger();
        AtomicReference<Clan> strongestClan = new AtomicReference<>(null);
        clansCollection.forEach(
                (key, queue) -> {
                    if (queue.isEmpty()) {
                        // NOP: No stream or straight forward filter out for empty queues
                    } else if (strongestClan.get() == null
                            || clanPickerEngineUtil.isLeftClanStronger(queue.peek(), strongestClan.get())) {
                        strongestClan.set(queue.peek());
                        keyWithTheQueueThatContainsTheStrongestClan.set(key);
                    }
                });
        return keyWithTheQueueThatContainsTheStrongestClan.get();
    }
}
