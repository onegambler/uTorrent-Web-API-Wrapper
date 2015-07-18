package com.utorrent.webapiwrapper.core;

import com.google.gson.*;
import com.utorrent.webapiwrapper.core.entities.*;
import com.utorrent.webapiwrapper.core.entities.Torrent.TorrentStatus;
import com.utorrent.webapiwrapper.core.entities.TorrentFileList.File;
import com.utorrent.webapiwrapper.core.entities.TorrentProperties.State;

import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.TemporalUnit;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

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
                .hash(jsonTorrentMessage.get(0).getAsString())
                .statuses(TorrentStatus.decodeStatus(jsonTorrentMessage.get(1).getAsInt()))
                .name(jsonTorrentMessage.get(2).getAsString())
                .size(jsonTorrentMessage.get(3).getAsLong())
                .progress(jsonTorrentMessage.get(4).getAsLong() / 10)
                .downloaded(jsonTorrentMessage.get(5).getAsLong())
                .uploaded(jsonTorrentMessage.get(6).getAsLong())
                .ratio(jsonTorrentMessage.get(7).getAsFloat() / 1000)
                .uploadSpeed(jsonTorrentMessage.get(8).getAsLong())
                .downloadSpeed(jsonTorrentMessage.get(9).getAsLong())
                .eta(Duration.ofSeconds(jsonTorrentMessage.get(10).getAsLong()))
                .label(jsonTorrentMessage.get(11).getAsString())
                .peersConnected(jsonTorrentMessage.get(12).getAsInt())
                .peersInSwarm(jsonTorrentMessage.get(13).getAsInt())
                .seedsConnected(jsonTorrentMessage.get(14).getAsInt())
                .seedsInSwarm(jsonTorrentMessage.get(15).getAsInt())
                .availability(jsonTorrentMessage.get(16).getAsLong())
                .torrentQueueOrder(jsonTorrentMessage.get(17).getAsLong())
                .remaining(jsonTorrentMessage.get(18).getAsLong())
                .statusInString(jsonTorrentMessage.get(21).getAsString())
                .torrentNumber(jsonTorrentMessage.get(22).getAsInt())
                .dateAdded(Instant.ofEpochSecond(jsonTorrentMessage.get(23).getAsLong()));

        if (jsonTorrentMessage.get(24).getAsLong() > 0) {
            torrentBuilder.dateCompleted(Instant.ofEpochSecond(jsonTorrentMessage.get(24).getAsLong()));
        }

        torrentBuilder.path(Paths.get(jsonTorrentMessage.get(26).getAsString()));

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
}
