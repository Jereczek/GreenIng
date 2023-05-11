package com.ing.onlinegame.engine;

import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Ordering;
import com.ing.model.onlinegame.Clan;
import jakarta.inject.Singleton;

import java.util.Objects;

@Singleton
public class ClanPickerEngineUtil {

    public Ordering<Clan> priorityClanDescendingOrdering = new Ordering<>() {
        @Override
        public int compare(Clan left, Clan right) {
            return ComparisonChain.start()
                    .compare(
                            Objects.requireNonNull(right.getPoints()),
                            Objects.requireNonNull(left.getPoints()))
                    .compare(
                            Objects.requireNonNull(left.getNumberOfPlayers()),
                            Objects.requireNonNull(right.getNumberOfPlayers()))
                    .result();
        }
    };

    public boolean isLeftClanStronger(Clan left, Clan right) {
        int result = this.priorityClanDescendingOrdering.compare(left, right);
        return result < 0;
    }
}
