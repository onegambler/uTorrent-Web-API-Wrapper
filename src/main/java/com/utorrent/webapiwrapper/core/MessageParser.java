package com.utorrent.webapiwrapper.core;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.utorrent.webapiwrapper.core.entities.*;
import com.utorrent.webapiwrapper.core.entities.Torrent.TorrentStatus;
import com.utorrent.webapiwrapper.core.entities.TorrentFileList.File;
import com.utorrent.webapiwrapper.core.entities.TorrentProperties.State;

import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

import static com.utorrent.webapiwrapper.core.MessageParser.TorrentField.*;


public class MessageParser {

    private final Gson jsonParser = new Gson();

    public TorrentListSnapshot parseAsTorrentListSnapshot(String jsonMessage) {
        TorrentListSnapshot torrentListSnapshot = new TorrentListSnapshot();
        JsonObject jsonTorrentSnapshot = jsonParser.fromJson(jsonMessage, JsonObject.class);
        JsonArray array = jsonTorrentSnapshot.getAsJsonArray("torrents");
        if (array != null) {
            for (JsonElement torrentJson : array) {
                torrentListSnapshot.addTorrentToAdd(parseAsTorrent(torrentJson.getAsJsonArray()));
            }
        }

        array = jsonTorrentSnapshot.getAsJsonArray("torrentp");

        if (array != null) {
            for (JsonElement torrentJson : array) {
                torrentListSnapshot.addTorrentToAdd(parseAsTorrent(torrentJson.getAsJsonArray()));
            }
        }

        array = jsonTorrentSnapshot.getAsJsonArray("torrentm");
        if (array != null) {
            for (JsonElement hash : array) {
                torrentListSnapshot.addTorrentToDelete(hash.getAsString());
            }
        }

        torrentListSnapshot.setCacheID(jsonTorrentSnapshot.get("torrentc").getAsString());

        return torrentListSnapshot;
    }

    private Torrent parseAsTorrent(JsonArray jsonTorrentMessage) {

        Torrent.TorrentBuilder torrentBuilder = Torrent.builder()
                .hash(TORRENT_HASH.getAsString(jsonTorrentMessage))
                .statuses(TorrentStatus.decodeStatus(TORRENT_STATUS.getAsInt(jsonTorrentMessage)))
                .name(TORRENT_NAME.getAsString(jsonTorrentMessage))
                .size(TORRENT_SIZE.getAsLong(jsonTorrentMessage))
                .progress(TORRENT_PROGRESS.getAsLong(jsonTorrentMessage) / 10)
                .downloaded(TORRENT_DOWNLOADED.getAsLong(jsonTorrentMessage))
                .uploaded(TORRENT_UPLOADED.getAsLong(jsonTorrentMessage))
                .ratio(TORRENT_RATIO.getAsFloat(jsonTorrentMessage) / 1000)
                .uploadSpeed(TORRENT_UPSPEED.getAsLong(jsonTorrentMessage))
                .downloadSpeed(TORRENT_DOWNSPEED.getAsLong(jsonTorrentMessage))
                .eta(Duration.ofSeconds(TORRENT_ETA.getAsLong(jsonTorrentMessage)))
                .label(TORRENT_LABEL.getAsString(jsonTorrentMessage))
                .peersConnected(TORRENT_PEERS_CONNECTED.getAsInt(jsonTorrentMessage))
                .peersInSwarm(TORRENT_PEERS_SWARM.getAsInt(jsonTorrentMessage))
                .seedsConnected(TORRENT_SEEDS_CONNECTED.getAsInt(jsonTorrentMessage))
                .seedsInSwarm(TORRENT_SEEDS_SWARM.getAsInt(jsonTorrentMessage))
                .availability(TORRENT_AVAILABILITY.getAsLong(jsonTorrentMessage))
                .torrentQueueOrder(TORRENT_QUEUE_POSITION.getAsLong(jsonTorrentMessage))
                .remaining(TORRENT_REMAINING.getAsLong(jsonTorrentMessage))
                .statusMessage(TORRENT_STATUS_MESSAGE.getAsString(jsonTorrentMessage))
                .streamId(TORRENT_STREAM_ID.getAsInt(jsonTorrentMessage))
                .dateAdded(Instant.ofEpochSecond(TORRENT_DATE_ADDED.getAsLong(jsonTorrentMessage)))
                .downloadURL(TORRENT_DOWNLOAD_URL.getAsString(jsonTorrentMessage))
                .rssFeedURL(TORRENT_RSS_FEED_URL.getAsString(jsonTorrentMessage))
                .appUpdateURL(TORRENT_APP_UPDATE_URL.getAsString(jsonTorrentMessage));

        if (jsonTorrentMessage.get(24).getAsLong() > 0) {
            torrentBuilder.dateCompleted(Instant.ofEpochSecond(TORRENT_DATE_COMPLETED.getAsLong(jsonTorrentMessage)));
        }

        torrentBuilder.path(Paths.get(TORRENT_SAVE_PATH.getAsString(jsonTorrentMessage)));

        return torrentBuilder.build();

    }

    public Set<TorrentFileList> parseAsTorrentFileList(String jsonMessage) {
        JsonObject jsonFileList = jsonParser.fromJson(jsonMessage, JsonObject.class);
        JsonArray array = jsonFileList.getAsJsonArray("files");
        Set<TorrentFileList> torrentFiles = new HashSet<>();

        for (int i = 0; i < array.getAsJsonArray().size(); i++) {

            JsonArray files = array.getAsJsonArray();

            TorrentFileList torrentFile = new TorrentFileList();
            if (files != null) {
                torrentFile.setHash(files.get(i).getAsString());
                i++;
                for (JsonElement torrentJson : files.get(i).getAsJsonArray()) {
                    torrentFile.addFile(getTorrentFile(torrentJson.getAsJsonArray()));
                }
            }
            torrentFiles.add(torrentFile);

        }

        return torrentFiles;

    }

    private File getTorrentFile(JsonArray torrentJson) {
        return File.builder()
                .name(torrentJson.get(0).getAsString())
                .size(torrentJson.get(1).getAsLong())
                .downloaded(torrentJson.get(2).getAsLong())
                .priority(Priority.getPriority(torrentJson.get(3).getAsInt()))
                .startingPart(torrentJson.get(4).getAsLong())
                .numberOfParts(torrentJson.get(5).getAsLong())
                .streamable(torrentJson.get(6).getAsBoolean())
                .videoSpeed(torrentJson.get(7).getAsLong())
                .streamDuration(Duration.ofSeconds(torrentJson.get(7).getAsLong()))
                .videoWidth(torrentJson.get(9).getAsLong())
                .videoHeight(torrentJson.get(10).getAsLong())
                .build();
    }

    public ClientSettings parseAsClientSettings(String jsonMessage) {

        ClientSettings settings = new ClientSettings();

        JsonObject jsonSettings = jsonParser.fromJson(jsonMessage, JsonObject.class);
        JsonArray array = jsonSettings.get("settings").getAsJsonArray();

        JsonArray setting;
        for (JsonElement element : array) {
            setting = element.getAsJsonArray();
            settings.addSetting(setting.get(0).getAsString(), setting.get(1).getAsInt(), setting.get(2).getAsString());
        }

        return settings;
    }

    public Set<TorrentProperties> parseAsTorrentProperties(String jsonMessage) {
        JsonArray jsonTorrentSettings = jsonParser.fromJson(jsonMessage, JsonObject.class).get("props").getAsJsonArray();

        Set<TorrentProperties> properties = new HashSet<>();

        for (JsonElement element : jsonTorrentSettings) {
            JsonObject object = element.getAsJsonObject();

            TorrentProperties property = TorrentProperties.builder()
                    .hash(object.get("hash").getAsString())
                    .trackers(object.get("trackers").getAsString().split("\\r\\n"))
                    .uploadRate(object.get("ulrate").getAsInt())
                    .downloadRate(object.get("dlrate").getAsInt())
                    .superSeed(State.getStateByValue(object.get("superseed").getAsInt()))
                    .useDHT(State.getStateByValue(object.get("dht").getAsInt()))
                    .usePEX(State.getStateByValue(object.get("pex").getAsInt()))
                    .seedOverride(State.getStateByValue(object.get("seed_override").getAsInt()))
                    .seedRatio(object.get("seed_ratio").getAsInt())
                    .seedTime(Duration.ofSeconds(object.get("seed_time").getAsInt()))
                    .uploadSlots(object.get("ulslots").getAsInt())
                    .build();

            properties.add(property);
        }

        return properties;
    }

    public enum TorrentField {
        TORRENT_HASH(0),
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

        TorrentField(int index) {
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
}
