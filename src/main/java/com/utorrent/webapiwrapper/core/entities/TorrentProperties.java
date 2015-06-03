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
    private boolean superSeed;
    private boolean useDHT;
    private boolean usePEX;
    private boolean seedOverride;
    private int seedRatio;
    private Duration seedTime;
    private int uploadSlots;
}

