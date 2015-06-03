package com.utorrent.webapiwrapper.core;

import com.google.gson.*;
import com.utorrent.webapiwrapper.core.entities.*;
import com.utorrent.webapiwrapper.core.entities.Torrent.TorrentStatus;
import com.utorrent.webapiwrapper.core.entities.TorrentFileList.File;

import java.nio.file.Paths;
import java.time.Duration;

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
        return Torrent.builder()
                .hash(jsonTorrentMessage.get(0).getAsString())
                .statuses(TorrentStatus.decodeStatus(jsonTorrentMessage.get(1).getAsInt()))
                .name(jsonTorrentMessage.get(2).getAsString())
                .size(jsonTorrentMessage.get(3).getAsLong())
                .progress(jsonTorrentMessage.get(4).getAsLong() / 10)
                .downloaded(jsonTorrentMessage.get(5).getAsLong())
                .uploaded(jsonTorrentMessage.get(6).getAsLong())
                .ratio(jsonTorrentMessage.get(7).getAsLong())
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
                .remaining(jsonTorrentMessage.get(5).getAsLong())
                .path(Paths.get(jsonTorrentMessage.get(2).getAsString())).build();
    }

    public TorrentFileList parseAsTorrentFileList(String jsonMessage) {
        JsonObject jsonFileList = jsonParser.fromJson(jsonMessage, JsonObject.class);
        JsonArray array = jsonFileList.getAsJsonArray("files");
        TorrentFileList torrentFile = new TorrentFileList();
        if (array != null) {
            torrentFile.setHash(array.get(0).getAsString());
            for (JsonElement torrentJson : array.get(1).getAsJsonArray()) {
                torrentFile.addFile(getTorrentFile(torrentJson.getAsJsonArray()));
            }
        }
        return torrentFile;

    }

    private File getTorrentFile(JsonArray torrentJson) {
        return File.builder()
                .name(torrentJson.get(0).getAsString())
                .size(torrentJson.get(1).getAsInt())
                .downloaded(torrentJson.get(2).getAsInt())
                .priority(Priority.getPriority(torrentJson.get(3).getAsInt()))
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

    public TorrentProperties parseAsTorrentProperties(String jsonMessage) {
        JsonObject jsonTorrentSettings = jsonParser.fromJson(jsonMessage, JsonObject.class).get("props").getAsJsonArray().get(0).getAsJsonObject();
        return TorrentProperties.builder()
                .hash(jsonTorrentSettings.get("hash").getAsString())
                .trackers(jsonTorrentSettings.get("trackers").getAsString().split("\\r\\n"))
                .uploadRate(jsonTorrentSettings.get("ulrate").getAsInt())
                .downloadRate(jsonTorrentSettings.get("dlrate").getAsInt())
                .superSeed(jsonTorrentSettings.get("superseed").getAsInt() == 1)
                .useDHT(jsonTorrentSettings.get("dht").getAsInt() == 1)
                .usePEX(jsonTorrentSettings.get("pex").getAsInt() == 1)
                .seedOverride(jsonTorrentSettings.get("seed_override").getAsInt() == 1)
                .seedRatio(jsonTorrentSettings.get("seed_ratio").getAsInt())
                .seedTime(Duration.ofSeconds(jsonTorrentSettings.get("seed_time").getAsInt()))
                .uploadSlots(jsonTorrentSettings.get("ulslots").getAsInt())
                .build();
    }
}
