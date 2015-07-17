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

    /**
     * Add a torrent to uTorrent
     *
     * @param url the torrent url to add
     * @return a result indicating whether the call completed with success or a fail
     * @throws IOException
     */
    RequestResult addTorrent(String url) throws IOException;

    /**
     * Add a torrent to uTorrent
     *
     * @param torrentFile the torrent file to add
     * @return a result indicating whether the call completed with success or a fail
     * @throws IOException
     */
    RequestResult addTorrent(File torrentFile) throws IOException;

    /**
     * It queries uTorrent for the specific torrent and returns an object representing
     * all the information related to the torrent
     *
     * @param torrentHash the torrent hash known to uTorrent
     * @return a torrent object
     * @throws IOException
     */
    Torrent getTorrent(String torrentHash) throws IOException;

    /**
     * It queries uTorrent for the specific torrent files and returns a list representing
     * all the information related to the torrent files
     *
     * @param torrentHashes a list of torrent hashes to query
     * @return a TorrentFileList object
     */
    TorrentFileList getTorrentFiles(List<String> torrentHashes);

    /**
     * It queries uTorrent for the specific torrent properties and returns an object representing
     * its properties
     *
     * @param torrentHash the torrent hash known to uTorrent
     * @return a TorrentProperties object
     */
    TorrentProperties getTorrentProperties(List<String> torrentHash);

    /**
     *
     * @param torrentHashes a list of torrent hashes to query
     * @return
     */
    RequestResult startTorrent(List<String> torrentHashes);

    /**
     *
     * @param torrentHashes
     * @return
     */
    RequestResult stopTorrent(List<String> torrentHashes);

    /**
     *
     * @param torrentHashes
     * @return
     */
    RequestResult pauseTorrent(List<String> torrentHashes);

    /**
     *
     * @param torrentHashes
     * @return
     */
    RequestResult forceStartTorrent(List<String> torrentHashes);

    /**
     *
     * @param torrentHashes
     * @return
     */
    RequestResult unpauseTorrent(List<String> torrentHashes);

    /**
     *
     * @param torrentHashes
     * @return
     */
    RequestResult recheckTorrent(List<String> torrentHashes);

    /**
     *
     * @param torrentHashes
     * @return
     */
    RequestResult removeTorrent(List<String> torrentHashes);

    /**
     *
     * @param torrentHashes
     * @return
     */
    RequestResult removeDataTorrent(List<String> torrentHashes);

    /**
     *
     * @param hash
     * @param priority
     * @param fileIndices
     * @return
     */
    RequestResult setTorrentFilePriority(String hash, Priority priority, List<Integer> fileIndices);

    /**
     *
     * @param settingName
     * @param settingValue
     * @return
     */
    RequestResult setClientSetting(String settingName, String settingValue);

    /**
     *
     * @param settings
     * @return
     */
    RequestResult setClientSetting(List<Request.QueryParam> settings);

    /**
     *
     * @return
     */
    Set<Torrent> getAllTorrents();

    /**
     *
     * @param settingKey
     * @param settingValue
     * @return
     */
    RequestResult setClientSetting(SettingsKey settingKey, String settingValue);

    /**
     *
     * @return
     */
    ClientSettings getClientSettings();

    /**
     * Create an instance of a UTorrentWebAPIClient implementation passing the
     * connection parameters
     *
     * @param connectionParams parameters to pass to the client
     * @return the UTorrentWebAPIClient implementation
     */
    static UTorrentWebAPIClient getClient(ConnectionParams connectionParams) {

        return new UTorrentWebAPIClientImpl(new RESTClient(connectionParams), connectionParams, new MessageParser());
    }
}
