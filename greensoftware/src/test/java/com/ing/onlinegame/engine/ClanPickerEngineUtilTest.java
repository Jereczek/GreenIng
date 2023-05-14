package com.ing.onlinegame.engine;

import com.google.common.collect.Ordering;
import com.ing.model.onlinegame.Clan;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@MicronautTest
public class ClanPickerEngineUtilTest {
    private final EasyRandom easyRandom = new EasyRandom();

    @Inject
    private ClanPickerEngineUtil clanPickerEngineUtil;

    @Test
    public void When_GetClanPriorityOrdering_Expect_ProperOrderingByDescendingPointsFirst() {
        Clan clan1 = prepareClan(easyRandom.nextInt(), 20);
        Clan clan2 = prepareClan(easyRandom.nextInt(), 10);
        Clan clan3 = prepareClan(easyRandom.nextInt(), 30);

        List<Clan> toSortByPoints = Arrays.asList(clan1, clan2, clan3);
        toSortByPoints.sort(clanPickerEngineUtil.getPriorityClanDescendingOrdering());

        Ordering<Clan> expectedOrder = Ordering.explicit(List.of(clan3, clan1, clan2));

        assertThat(expectedOrder.isOrdered(toSortByPoints)).isTrue();
    }

    @Test
    public void When_GetClanPriorityOrdering_Expect_ProperOrderingByAscendingPlayersSecond() {
        int sameNoOfPoints = easyRandom.nextInt();
        Clan clan1 = prepareClan(30, sameNoOfPoints);
        Clan clan2 = prepareClan(10, sameNoOfPoints);
        Clan clan3 = prepareClan(20, sameNoOfPoints);

        List<Clan> toSortByPlayers = Arrays.asList(clan1, clan2, clan3);
        toSortByPlayers.sort(clanPickerEngineUtil.getPriorityClanDescendingOrdering());

        Ordering<Clan> expectedOrder = Ordering.explicit(List.of(clan2, clan3, clan1));

        assertThat(expectedOrder.isOrdered(toSortByPlayers)).isTrue();
    }

    @Test
    public void When_GetClanPriorityOrdering_Expect_DoNotRemoveDuplicates() {
        int sameNoOfPoints = easyRandom.nextInt();
        int sameHeadCount = easyRandom.nextInt();
        Clan clan1 = prepareClan(sameHeadCount, sameNoOfPoints);
        Clan clan2 = prepareClan(sameHeadCount, sameNoOfPoints);
        Clan clan3 = prepareClan(sameHeadCount, sameNoOfPoints);

        List<Clan> clansWithDuplicates = Arrays.asList(clan1, clan2, clan3);
        clansWithDuplicates.sort(clanPickerEngineUtil.getPriorityClanDescendingOrdering());

        assertThat(clansWithDuplicates.size()).isEqualTo(3);
    }

    @Test
    public void When_IsLeftClanStronger_Expect_ComparePointsFirst() {
        Clan clan1 = prepareClan(easyRandom.nextInt(), 20);
        Clan clan2 = prepareClan(easyRandom.nextInt(), 10);
        assertThat(clanPickerEngineUtil.isLeftClanStronger(clan1, clan2)).isTrue();
    }

    @Test
    public void When_IsLeftClanStronger_Expect_CompareLowerHeadcountSecond() {
        int sameNoOfPoints = easyRandom.nextInt();
        Clan clan1 = prepareClan(10, sameNoOfPoints);
        Clan clan2 = prepareClan(20, sameNoOfPoints);
        assertThat(clanPickerEngineUtil.isLeftClanStronger(clan1, clan2)).isTrue();
    }

    @Test
    public void When_IsLeftClanStronger_Expect_EqualsIsFalse() {
        int sameNoOfPoints = easyRandom.nextInt();
        int sameHeadCount = easyRandom.nextInt();
        Clan clan1 = prepareClan(sameHeadCount, sameNoOfPoints);
        Clan clan2 = prepareClan(sameHeadCount, sameNoOfPoints);
        assertThat(clanPickerEngineUtil.isLeftClanStronger(clan1, clan2)).isFalse();
    }

    private Clan prepareClan(Integer noPlayersInClan, Integer points) {
        Clan clan = new Clan();
        clan.setPoints(points);
        clan.setNumberOfPlayers(noPlayersInClan);
        return clan;
    }

}
