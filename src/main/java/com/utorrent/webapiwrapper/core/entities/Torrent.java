package com.utorrent.webapiwrapper.core.entities;

import lombok.Builder;
import lombok.Data;

import java.nio.file.Path;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

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
	private int torrentNumber;
	private String statusInString;
	private final Instant dateAdded;
	private final Instant dateCompleted;
	private Path path;

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

		public static Set<TorrentStatus> decodeStatus(int maskedStatus){
			Set<TorrentStatus> statuses = new HashSet<>();

			for(TorrentStatus torrentStatus : TorrentStatus.values()){
				if((torrentStatus.getMask() & maskedStatus) != 0){
					statuses.add(torrentStatus);
				}
			}

			return statuses;
		}
	}
}
