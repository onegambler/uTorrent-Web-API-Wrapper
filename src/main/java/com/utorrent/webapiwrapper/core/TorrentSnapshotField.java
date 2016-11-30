package com.utorrent.webapiwrapper.core;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

public enum TorrentSnapshotField {
    TORRENT_HASH(0) ,
    TORRENT_STATUS(1),
    TORRENT_NAME(2),
    TORRENT_SIZE(3),
    TORRENT_PROGRESS(4),
    TORRENT_DOWNLOADED(5),
    TORRENT_UPLOADED(6),
    TORRENT_RATIO(7),
    TORRENT_UPSPEED(8),
    TORRENT_DOWNSPEED(9),
    TORRENT_ETA(10),
    TORRENT_LABEL(11),
    TORRENT_PEERS_CONNECTED(12),
    TORRENT_PEERS_SWARM(13),
    TORRENT_SEEDS_CONNECTED(14),
    TORRENT_SEEDS_SWARM(15),
    TORRENT_AVAILABILITY(16),
    TORRENT_QUEUE_POSITION(17),
    TORRENT_REMAINING(18),
    TORRENT_DOWNLOAD_URL(19),
    TORRENT_RSS_FEED_URL(20),
    TORRENT_STATUS_MESSAGE(21),
    TORRENT_STREAM_ID(22),
    TORRENT_DATE_ADDED(23),
    TORRENT_DATE_COMPLETED(24),
    TORRENT_APP_UPDATE_URL(25),
    TORRENT_SAVE_PATH(26);

    private int index;

    TorrentSnapshotField(int index) {
        this.index = index;
    }

    public String getAsString(JsonArray jsonArray) {
        return getElement(jsonArray).getAsString();
    }

    public long getAsLong(JsonArray jsonArray) {
        return getElement(jsonArray).getAsLong();
    }

    public int getAsInt(JsonArray jsonArray) {
        return getElement(jsonArray).getAsInt();
    }

    public float getAsFloat(JsonArray jsonArray) {
        return getElement(jsonArray).getAsFloat();
    }

    private JsonElement getElement(JsonArray jsonArray) {
        return jsonArray.get(this.index);
    }
}
