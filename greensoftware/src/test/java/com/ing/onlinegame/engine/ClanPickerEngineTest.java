package com.ing.onlinegame.engine;

import com.ing.model.onlinegame.Clan;
import com.ing.model.onlinegame.Players;
import com.ing.onlinegame.engine.collection.TreePriorityQueueClansCollection;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@MicronautTest
public class ClanPickerEngineTest {
    private final EasyRandom easyRandom = new EasyRandom();

    @Test
    public void When_ClanPickerEngineComputeMatchMakingAndCollectionIsEmpty_Expect_EmptyMatchmakingResult() {
        Players players = easyRandom.nextObject(Players.class);
        TreePriorityQueueClansCollection treePriorityQueueClansCollection = mock(TreePriorityQueueClansCollection.class);
        ClanPickerEngine clanPickerEngine = new ClanPickerEngine(players, treePriorityQueueClansCollection);
        clanPickerEngine.computeMatchmaking();
        when(treePriorityQueueClansCollection.isNotEmpty()).thenReturn(false);
        assertThat(clanPickerEngine.getMatchmakingResult()).isEmpty();
    }

    @Test
    public void When_ClanPickerEngineComputeMatchmaking_Expect_MatchmakingResultWithTheStrongestClan() {
        int clanSize = 1;
        int groupSize = 2;
        Players players = preparePlayersCollectionWithSize(clanSize, groupSize);
        Clan theStrongestClan = prepareClan(clanSize);
        TreePriorityQueueClansCollection treePriorityQueueClansCollection = mock(TreePriorityQueueClansCollection.class);
        when(treePriorityQueueClansCollection.isNotEmpty())
                .thenReturn(true)
                .thenReturn(false);
        when(treePriorityQueueClansCollection.poolTheStrongestClanInCollection()).thenReturn(Optional.of(theStrongestClan));

        ClanPickerEngine clanPickerEngine = new ClanPickerEngine(players, treePriorityQueueClansCollection);
        clanPickerEngine.computeMatchmaking();

        assertThat(clanPickerEngine.getMatchmakingResult().size()).isEqualTo(1);
        assertThat(clanPickerEngine.getMatchmakingResult().getFirst().size()).isEqualTo(clanSize);
        assertThat(clanPickerEngine.getMatchmakingResult().getFirst()).isEqualTo(List.of(theStrongestClan));
    }

    @Test
    public void When_ClanPickerEngineComputeMatchmakingAndThereIsMoreSpaceAvailable_Expect_PollExtraClanIntoMatchmakingGroup() {
        int clans = 2;
        int playersInsideClan = 1;
        int groupSize = 2;
        Players players = preparePlayersCollectionWithSize(groupSize, clans);
        Clan theStrongestClan = prepareClan(playersInsideClan);
        Clan remainingAvailableClan = prepareClan(playersInsideClan);
        TreePriorityQueueClansCollection treePriorityQueueClansCollection = mock(TreePriorityQueueClansCollection.class);
        when(treePriorityQueueClansCollection.isNotEmpty())
                .thenReturn(true)
                .thenReturn(false);
        when(treePriorityQueueClansCollection.poolTheStrongestClanInCollection()).thenReturn(Optional.of(theStrongestClan));
        when(treePriorityQueueClansCollection.poolTheStrongestClanForAvailableSize(
                groupSize - theStrongestClan.getNumberOfPlayers())).thenReturn(Optional.of(remainingAvailableClan));

        ClanPickerEngine clanPickerEngine = new ClanPickerEngine(players, treePriorityQueueClansCollection);
        clanPickerEngine.computeMatchmaking();

        assertThat(clanPickerEngine.getMatchmakingResult().size()).isEqualTo(1);
        assertThat(clanPickerEngine.getMatchmakingResult().getFirst().size()).isEqualTo(groupSize);
        assertThat(clanPickerEngine.getMatchmakingResult().getFirst()).isEqualTo(List.of(theStrongestClan, remainingAvailableClan));
    }

    @Test
    public void When_ClanPickerEngineComputeMatchmakingAndNoFullGroupPossible_Expect_PoolClanWithLessHeadCount() {
        int clans = 2;
        int playersInsideClan = 1;
        int groupSize = 5;
        Players players = preparePlayersCollectionWithSize(groupSize, clans);
        Clan theStrongestClan = prepareClan(playersInsideClan);
        Clan remainingAvailableClanFits = prepareClan(playersInsideClan);
        TreePriorityQueueClansCollection treePriorityQueueClansCollection = mock(TreePriorityQueueClansCollection.class);
        when(treePriorityQueueClansCollection.isNotEmpty())
                .thenReturn(true)
                .thenReturn(false);
        when(treePriorityQueueClansCollection.poolTheStrongestClanInCollection()).thenReturn(Optional.of(theStrongestClan));
        when(treePriorityQueueClansCollection.poolTheStrongestClanForAvailableSize(anyInt())).thenReturn(Optional.empty());
        when(treePriorityQueueClansCollection.poolTheStrongestClanForAvailableSize(playersInsideClan))
                .thenReturn(Optional.of(remainingAvailableClanFits))
                .thenReturn(Optional.empty());

        ClanPickerEngine clanPickerEngine = new ClanPickerEngine(players, treePriorityQueueClansCollection);
        clanPickerEngine.computeMatchmaking();


        assertThat(clanPickerEngine.getMatchmakingResult().size()).isEqualTo(1);
        assertThat(clanPickerEngine.getMatchmakingResult().getFirst().size()).isEqualTo(clans);
        assertThat(clanPickerEngine.getMatchmakingResult().getFirst()).isEqualTo(List.of(theStrongestClan, remainingAvailableClanFits));

        verify(treePriorityQueueClansCollection, atLeastOnce()).poolTheStrongestClanInCollection();
        verify(treePriorityQueueClansCollection, atLeastOnce()).poolTheStrongestClanForAvailableSize(4);
        verify(treePriorityQueueClansCollection, atLeastOnce()).poolTheStrongestClanForAvailableSize(3);
        verify(treePriorityQueueClansCollection, atLeastOnce()).poolTheStrongestClanForAvailableSize(2);
        verify(treePriorityQueueClansCollection, atLeastOnce()).poolTheStrongestClanForAvailableSize(1);
    }

    private Players preparePlayersCollectionWithSize(int groupSize, int noClans) {
        Players players = new Players();
        players.setClans(easyRandom.objects(Clan.class, noClans).toList());
        players.setGroupCount(groupSize);
        return players;
    }

    private Clan prepareClan(Integer noPlayersInClan) {
        Clan clan = new Clan();
        clan.setPoints(easyRandom.nextInt());
        clan.setNumberOfPlayers(noPlayersInClan);
        return clan;
    }
}
