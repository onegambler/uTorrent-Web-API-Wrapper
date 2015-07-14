package com.utorrent.webapiwrapper.core;

import com.utorrent.webapiwrapper.core.entities.*;
import com.utorrent.webapiwrapper.restclient.ConnectionParams;
import com.utorrent.webapiwrapper.restclient.RESTClient;
import com.utorrent.webapiwrapper.restclient.Request;
import org.apache.http.entity.ContentType;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;

/**
 * uTorrent WebUI API interface.
 * It allows to interact with uTorrent through its web API.
 *
 * @author Roberto Magale
 *         10/apr/2012
 */
public interface UTorrentWebAPIClient extends Closeable {

    ContentType APPLICATION_X_BIT_TORRENT_CONTENT_TYPE = ContentType.create("application/x-bittorrent");

    RequestResult addTorrent(String url) throws IOException;

    RequestResult addTorrent(File torrentFile) throws IOException;

    Torrent getTorrent(String torrentHash) throws IOException;

    TorrentFileList getTorrentFiles(List<String> torrentHashes);

    TorrentProperties getTorrentProperties(List<String> torrentHash);

    RequestResult startTorrent(List<String> hashes);

    RequestResult stopTorrent(List<String> hashes);

    RequestResult pauseTorrent(List<String> hashes);

    RequestResult forceStartTorrent(List<String> hashes);

    RequestResult unpauseTorrent(List<String> hashes);

    RequestResult recheckTorrent(List<String> hashes);

    RequestResult removeTorrent(List<String> hashes);

    RequestResult removeDataTorrent(List<String> hashes);

    RequestResult setTorrentFilePriority(String hash, Priority priority, List<Integer> fileIndices);

    RequestResult setClientSetting(String settingName, String settingValue);

    RequestResult setClientSetting(List<Request.QueryParam> settings);

    Set<Torrent> getTorrentList();

    RequestResult setClientSetting(SettingsKey settingKey, String settingValue);

    ClientSettings getClientSettings();

    static UTorrentWebAPIClient getClient(ConnectionParams connectionParams) {

        return new UTorrentWebAPIClientImpl(new RESTClient(connectionParams), connectionParams, new MessageParser());
    }
}
