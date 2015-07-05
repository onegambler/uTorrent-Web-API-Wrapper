package com.utorrent.webapiwrapper.core.entities;

import lombok.Builder;
import lombok.Data;

import java.time.Duration;

@Data
@Builder
public class TorrentProperties {

    private String hash;
    private String[] trackers;
    private int uploadRate;
    private int downloadRate;
    private State superSeed;
    private State useDHT;
    private State usePEX;
    private State seedOverride;
    private int seedRatio;
    private Duration seedTime;
    private int uploadSlots;

    public enum State {
        NOT_ALLOWED(-1), DISABLED(0), ENABLED(1);

        private int value;

        State(int value) {
            this.value = value;
        }

        public static State getStateByValue(int value) {
            for(State state : State.values()) {
                if(state.value == value) {
                    return state;
                }
            }

            throw new IllegalArgumentException("No State with value " + value);
        }
    }
}

