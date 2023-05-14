package com.ing.onlinegame.engine;

import com.ing.model.onlinegame.Clan;
import com.ing.model.onlinegame.Players;
import io.micronaut.test.annotation.MockBean;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.spy;

@MicronautTest
public class ClanPickerEngineFactoryTest {
    private static final int MIN_CLAN_BOUND = 1;
    private static final int MAX_CLAN_BOUND = 10;
    private static final int MIN_GROUP_BOUND = 10;
    private static final int MAX_GROUP_BOUND = 10000;
    private final EasyRandom easyRandom = new EasyRandom();

    @Inject
    private ClanPickerEngineUtil clanPickerEngineUtil;

    @Inject
    private ClanPickerEngineFactory clanPickerEngineFactory;

    @Test
    public void When_ClanPickerEngineFactoryBuildClanPicker_Expect_NewEngineReturned() {
        Players players1 = preparePlayers();
        ClanPickerEngine clanPickerEngine1 = clanPickerEngineFactory.buildClanPicker(players1);

        Players players2 = preparePlayers();
        ClanPickerEngine clanPickerEngine2 = clanPickerEngineFactory.buildClanPicker(players2);

        assertThat(clanPickerEngine1).isNotEqualTo(clanPickerEngine2);
    }

    private Players preparePlayers() {
        Players players = new Players();
        players.setGroupCount(easyRandom.nextInt(MIN_GROUP_BOUND,MAX_GROUP_BOUND));
        players.setClans(List.of(prepareClan()));
        return players;
    }

    private Clan prepareClan() {
        Clan clan = new Clan();
        clan.setNumberOfPlayers(easyRandom.nextInt(MIN_CLAN_BOUND, MAX_CLAN_BOUND));
        clan.setPoints(easyRandom.nextInt());
        return clan;
    }

    @MockBean(ClanPickerEngineUtil.class)
    ClanPickerEngineUtil clanPickerEngineUtil() {
        return spy(ClanPickerEngineUtil.class);
    }
}