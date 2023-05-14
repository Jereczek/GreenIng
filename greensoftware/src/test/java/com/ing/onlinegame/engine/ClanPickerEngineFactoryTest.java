package com.ing.onlinegame.engine;

import com.ing.model.onlinegame.Players;
import io.micronaut.test.annotation.MockBean;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

@MicronautTest
public class ClanPickerEngineFactoryTest {
    private final EasyRandom easyRandom = new EasyRandom();

    @Inject
    private ClanPickerEngineUtil clanPickerEngineUtil;

    @Inject
    private ClanPickerEngineFactory clanPickerEngineFactory;

    @Test
    public void When_ClanPickerEngineFactoryBuildClanPicker_Expect_NewEngineReturned() {
        Players players1 = easyRandom.nextObject(Players.class);
        ClanPickerEngine clanPickerEngine1 = clanPickerEngineFactory.buildClanPicker(players1);

        Players players2 = easyRandom.nextObject(Players.class);
        ClanPickerEngine clanPickerEngine2 = clanPickerEngineFactory.buildClanPicker(players2);

        assertThat(clanPickerEngine1).isNotEqualTo(clanPickerEngine2);
    }

    @MockBean(ClanPickerEngineUtil.class)
    ClanPickerEngineUtil clanPickerEngineUtil() {
        return mock(ClanPickerEngineUtil.class);
    }
}