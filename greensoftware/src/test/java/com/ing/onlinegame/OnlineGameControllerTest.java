package com.ing.onlinegame;

import com.ing.model.onlinegame.Clan;
import com.ing.model.onlinegame.Players;
import io.micronaut.test.annotation.MockBean;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@MicronautTest
public class OnlineGameControllerTest {

    private static final int COLLECTION_SIZE = 3;
    private final EasyRandom easyRandom = new EasyRandom();

    @Inject
    private OnlineGameService onlineGameService;

    @Inject
    private OnlineGameController onlineGameController;

    @Test
    public void When_OnlineGameControllerCalled_Expect_OnlineGameServiceExecuted() {
        Players players = easyRandom.nextObject(Players.class);
        List<Clan> clans = easyRandom.objects(Clan.class, COLLECTION_SIZE).toList();
        List<List<Clan>> matchmakingClans = List.of(clans);

        when(onlineGameService.computeMatchmaking(players)).thenReturn(matchmakingClans);

        List<List<Clan>> result =  onlineGameController.calculate(players);
        verify(onlineGameService).computeMatchmaking(players);
        assertThat(result).isEqualTo(matchmakingClans);
    }

    @Test
    public void When_OnlineGameControllerCalled_And_OnlineGameServiceThrowUncheckException_Expect_Rethrow() {
        Players players = easyRandom.nextObject(Players.class);
        when(onlineGameService.computeMatchmaking(players)).thenThrow(RuntimeException.class);

        assertThrows(RuntimeException.class,
                () -> onlineGameController.calculate(players));
    }
    @MockBean(OnlineGameService.class)
    OnlineGameService onlineGameService() {
        return mock(OnlineGameService.class);
    }
}
