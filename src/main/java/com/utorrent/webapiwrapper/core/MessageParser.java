package com.utorrent.webapiwrapper.core;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.utorrent.webapiwrapper.core.entities.ClientSettings;
import com.utorrent.webapiwrapper.core.entities.Priority;
import com.utorrent.webapiwrapper.core.entities.Torrent;
import com.utorrent.webapiwrapper.core.entities.Torrent.TorrentBuilder;
import com.utorrent.webapiwrapper.core.entities.Torrent.TorrentStatus;
import com.utorrent.webapiwrapper.core.entities.TorrentFileList;
import com.utorrent.webapiwrapper.core.entities.TorrentFileList.File;
import com.utorrent.webapiwrapper.core.entities.TorrentListSnapshot;
import com.utorrent.webapiwrapper.core.entities.TorrentProperties;
import com.utorrent.webapiwrapper.core.entities.TorrentProperties.State;

import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

import static com.utorrent.webapiwrapper.core.TorrentSnapshotField.TORRENT_APP_UPDATE_URL;
import static com.utorrent.webapiwrapper.core.TorrentSnapshotField.TORRENT_AVAILABILITY;
import static com.utorrent.webapiwrapper.core.TorrentSnapshotField.TORRENT_DATE_ADDED;
import static com.utorrent.webapiwrapper.core.TorrentSnapshotField.TORRENT_DATE_COMPLETED;
import static com.utorrent.webapiwrapper.core.TorrentSnapshotField.TORRENT_DOWNLOADED;
import static com.utorrent.webapiwrapper.core.TorrentSnapshotField.TORRENT_DOWNLOAD_URL;
import static com.utorrent.webapiwrapper.core.TorrentSnapshotField.TORRENT_DOWNSPEED;
import static com.utorrent.webapiwrapper.core.TorrentSnapshotField.TORRENT_ETA;
import static com.utorrent.webapiwrapper.core.TorrentSnapshotField.TORRENT_HASH;
import static com.utorrent.webapiwrapper.core.TorrentSnapshotField.TORRENT_LABEL;
import static com.utorrent.webapiwrapper.core.TorrentSnapshotField.TORRENT_NAME;
import static com.utorrent.webapiwrapper.core.TorrentSnapshotField.TORRENT_PEERS_CONNECTED;
import static com.utorrent.webapiwrapper.core.TorrentSnapshotField.TORRENT_PEERS_SWARM;
import static com.utorrent.webapiwrapper.core.TorrentSnapshotField.TORRENT_PROGRESS;
import static com.utorrent.webapiwrapper.core.TorrentSnapshotField.TORRENT_QUEUE_POSITION;
import static com.utorrent.webapiwrapper.core.TorrentSnapshotField.TORRENT_RATIO;
import static com.utorrent.webapiwrapper.core.TorrentSnapshotField.TORRENT_REMAINING;
import static com.utorrent.webapiwrapper.core.TorrentSnapshotField.TORRENT_RSS_FEED_URL;
import static com.utorrent.webapiwrapper.core.TorrentSnapshotField.TORRENT_SAVE_PATH;
import static com.utorrent.webapiwrapper.core.TorrentSnapshotField.TORRENT_SEEDS_CONNECTED;
import static com.utorrent.webapiwrapper.core.TorrentSnapshotField.TORRENT_SEEDS_SWARM;
import static com.utorrent.webapiwrapper.core.TorrentSnapshotField.TORRENT_SIZE;
import static com.utorrent.webapiwrapper.core.TorrentSnapshotField.TORRENT_STATUS;
import static com.utorrent.webapiwrapper.core.TorrentSnapshotField.TORRENT_STATUS_MESSAGE;
import static com.utorrent.webapiwrapper.core.TorrentSnapshotField.TORRENT_STREAM_ID;
import static com.utorrent.webapiwrapper.core.TorrentSnapshotField.TORRENT_UPLOADED;
import static com.utorrent.webapiwrapper.core.TorrentSnapshotField.TORRENT_UPSPEED;
import static java.util.Objects.nonNull;


public class MessageParser {

    private static final int BUILD_221 = 25130;

    private final Gson jsonParser = new Gson();

    public TorrentListSnapshot parseAsTorrentListSnapshot(String jsonMessage) {
        TorrentListSnapshot torrentListSnapshot = new TorrentListSnapshot();
        JsonObject jsonTorrentSnapshot = jsonParser.fromJson(jsonMessage, JsonObject.class);
        int build = jsonTorrentSnapshot.get("build").getAsInt();
        JsonArray array = jsonTorrentSnapshot.getAsJsonArray("torrents");
        if (nonNull(array)) {
            for (JsonElement torrentJson : array) {
                torrentListSnapshot.addTorrentToAdd(parseAsTorrent(build, torrentJson.getAsJsonArray()));
            }
        }

        array = jsonTorrentSnapshot.getAsJsonArray("torrentp");

        if (nonNull(array)) {
            for (JsonElement torrentJson : array) {
                torrentListSnapshot.addTorrentToAdd(parseAsTorrent(build, torrentJson.getAsJsonArray()));
            }
        }

        array = jsonTorrentSnapshot.getAsJsonArray("torrentm");
        if (nonNull(array)) {
            for (JsonElement hash : array) {
                torrentListSnapshot.addTorrentToDelete(hash.getAsString());
            }
        }

        torrentListSnapshot.setCacheID(jsonTorrentSnapshot.get("torrentc").getAsString());

        return torrentListSnapshot;
    }

    private Torrent parseAsTorrent(int build, JsonArray jsonTorrentMessage) {
        TorrentBuilder torrentBuilder = Torrent.builder()
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
                .remaining(TORRENT_REMAINING.getAsLong(jsonTorrentMessage));


        if (isAtLeastBuild221(build)) {
            torrentBuilder
                    .statusMessage(TORRENT_STATUS_MESSAGE.getAsString(jsonTorrentMessage))
                    .streamId(TORRENT_STREAM_ID.getAsInt(jsonTorrentMessage))
                    .dateAdded(Instant.ofEpochSecond(TORRENT_DATE_ADDED.getAsLong(jsonTorrentMessage)))
                    .downloadURL(TORRENT_DOWNLOAD_URL.getAsString(jsonTorrentMessage))
                    .rssFeedURL(TORRENT_RSS_FEED_URL.getAsString(jsonTorrentMessage))
                    .appUpdateURL(TORRENT_APP_UPDATE_URL.getAsString(jsonTorrentMessage));

            long torrentCompletedDate = TORRENT_DATE_COMPLETED.getAsLong(jsonTorrentMessage);
            if (torrentCompletedDate > 0) {
                torrentBuilder.dateCompleted(Instant.ofEpochSecond(torrentCompletedDate));
            }

            torrentBuilder.path(Paths.get(TORRENT_SAVE_PATH.getAsString(jsonTorrentMessage)));
        }

        return torrentBuilder.build();
    }

    private boolean isAtLeastBuild221(int build) {
        return build >= BUILD_221;
    }

    public Set<TorrentFileList> parseAsTorrentFileList(String jsonMessage) {
        JsonObject jsonFileList = jsonParser.fromJson(jsonMessage, JsonObject.class);
        JsonArray array = jsonFileList.getAsJsonArray("files");
        Set<TorrentFileList> torrentFiles = new HashSet<>();

        JsonArray files = array.getAsJsonArray();
        if (nonNull(files)) {
            for (int i = 0; i < files.size(); i++) {
                TorrentFileList torrentFile = new TorrentFileList();
                torrentFile.setHash(files.get(i).getAsString());
                i++;
                for (JsonElement torrentJson : files.get(i).getAsJsonArray()) {
                    torrentFile.addFile(getTorrentFile(torrentJson.getAsJsonArray()));
                }
                torrentFiles.add(torrentFile);
            }
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
                .streamDuration(Duration.ofSeconds(torrentJson.get(8).getAsLong()))
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
        JsonArray jsonTorrentSettings = jsonParser
                .fromJson(jsonMessage, JsonObject.class)
                .get("props").getAsJsonArray();

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


}
