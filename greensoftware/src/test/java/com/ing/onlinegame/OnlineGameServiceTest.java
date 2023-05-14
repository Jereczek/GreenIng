package com.ing.onlinegame;

import com.ing.model.onlinegame.Clan;
import com.ing.model.onlinegame.Players;
import com.ing.onlinegame.engine.ClanPickerEngine;
import com.ing.onlinegame.engine.ClanPickerEngineFactory;
import io.micronaut.test.annotation.MockBean;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@MicronautTest
public class OnlineGameServiceTest {
    private static final int COLLECTION_SIZE = 3;
    private final EasyRandom easyRandom = new EasyRandom();
    @Inject
    private ClanPickerEngineFactory clanPickerEngineFactory;

    @Inject
    private OnlineGameService onlineGameService;

    @Test
    public void When_OnlineGameServiceComputeMatchmaking_Expect_GetDedicatedEngineAndComputeMatchmaking() {
        Players players = easyRandom.nextObject(Players.class);
        LinkedList<List<Clan>> matchmakingClans = prepareAndMockMatchmakingClansInClanPickerEngine();

        ClanPickerEngine clanPickerEngine = mock(ClanPickerEngine.class);
        when(clanPickerEngineFactory.buildClanPicker(players)).thenReturn(clanPickerEngine);
        when(clanPickerEngine.getMatchmakingResult()).thenReturn(matchmakingClans);

        List<List<Clan>> result = onlineGameService.computeMatchmaking(players);

        verify(clanPickerEngineFactory).buildClanPicker(players);
        verify(clanPickerEngine).computeMatchmaking();
        verify(clanPickerEngine).getMatchmakingResult();
        assertThat(result).isEqualTo(matchmakingClans);
        verifyNoMoreInteractions(clanPickerEngineFactory);
    }

    private LinkedList<List<Clan>> prepareAndMockMatchmakingClansInClanPickerEngine() {
        List<Clan> clans = easyRandom.objects(Clan.class, COLLECTION_SIZE).toList();
        LinkedList<List<Clan>> matchmakingClans = new LinkedList<>();
        matchmakingClans.add(clans);
        return matchmakingClans;
    }

    @MockBean(ClanPickerEngineFactory.class)
    ClanPickerEngineFactory clanPickerEngineFactory() {
        return mock(ClanPickerEngineFactory.class);
    }
}
