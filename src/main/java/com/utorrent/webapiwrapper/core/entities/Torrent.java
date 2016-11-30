package com.utorrent.webapiwrapper.core.entities;

import lombok.Builder;
import lombok.Data;

import java.nio.file.Path;
import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

@Data
@Builder
public class Torrent {

    private Set<TorrentStatus> statuses;
    private String hash;
    private String name;
    private long size;
    private float progress;
    private long downloaded;
    private long uploaded;
    private float ratio;
    private long uploadSpeed;
    private long downloadSpeed;
    private Duration eta;
    private String label;
    private int peersConnected;
    private int peersInSwarm;
    private int seedsConnected;
    private int seedsInSwarm;
    private long availability;
    private long torrentQueueOrder;
    private long remaining;
    private int streamId;
    private String statusMessage;
    private final Instant dateAdded;
    private final Instant dateCompleted;
    private Path path;
    private String downloadURL;
    private String rssFeedURL;
    private String appUpdateURL;

    public enum TorrentStatus {
        STARTED(1),
        CHECKING(2),
        START_AFTER_CHECK(4),
        CHECKED(8),
        ERROR(16),
        PAUSED(32),
        QUEUED(64),
        LOADED(128);

        private int mask;

        TorrentStatus(int mask) {
            this.mask = mask;
        }

        private int getMask() {
            return mask;
        }

        public static Set<TorrentStatus> decodeStatus(int maskedStatus) {
            return Arrays.stream(values())
                    .filter(torrentStatus -> (torrentStatus.getMask() & maskedStatus) != 0)
                    .collect(toSet());
        }
    }
}
