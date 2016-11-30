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
import java.util.Optional;
import java.util.Set;

/**
 * uTorrent WebUI API interface.
 * It allows to interact with uTorrent through its web API.
 *
 * @author Roberto Magale
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
     * @return a set of TorrentFileList objects
     */
    Set<TorrentFileList> getTorrentFiles(List<String> torrentHashes);

    /**
     * It queries uTorrent for the specific torrent files and returns a list representing
     * all the information related to the torrent files
     *
     * @param torrentHash torrent hash to query
     * @return an optionalTorrentFileList object
     */
    Optional<TorrentFileList> getTorrentFiles(String torrentHash);

    /**
     * It queries uTorrent for the specific torrent properties and returns an object representing
     * its properties
     *
     * @param torrentHashes the torrent hash known to uTorrent
     * @return a TorrentProperties object
     */
    Set<TorrentProperties> getTorrentProperties(List<String> torrentHashes);

    /**
     * It queries uTorrent for the specific torrent properties and returns an object representing
     * its properties
     *
     * @param torrentHash the torrent hash known to uTorrent
     * @return a TorrentProperties object
     */
    Optional<TorrentProperties> getTorrentProperties(String torrentHash);

    /**
     *
     * It starts a torrent
     *
     * @param torrentHashes a list of torrent hashes to start
     * @return a result indicating whether the call completed with success or a fail
     */
    RequestResult startTorrent(List<String> torrentHashes);

    RequestResult startTorrent(String hash);

    /**
     * It stops a torrent
     *
     * @param torrentHashes a list of torrent hashes to stop
     * @return a result indicating whether the call completed with success or a fail
     */
    RequestResult stopTorrent(List<String> torrentHashes);

    /**
     * It stops a torrent
     *
     * @param hash torrent hash to stop
     * @return a result indicating whether the call completed with success or a fail
     */
    RequestResult stopTorrent(String hash);

    /**
     * It pauses a torrent
     *
     * @param torrentHashes a list of torrent hashes to pause
     * @return a result indicating whether the call completed with success or a fail
     */
    RequestResult pauseTorrent(List<String> torrentHashes);

    /**
     * It pauses a torrent
     *
     * @param hash torrent hash to pause
     * @return a result indicating whether the call completed with success or a fail
     */
    RequestResult pauseTorrent(String hash);

    /**
     * It forces a torrent to start
     *
     * @param torrentHashes a list of torrent hashes to start
     * @return a result indicating whether the call completed with success or a fail
     */
    RequestResult forceStartTorrent(List<String> torrentHashes);

    /**
     * It forces a torrent to start
     *
     * @param hash torrent hash to start
     * @return a result indicating whether the call completed with success or a fail
     */
    RequestResult forceStartTorrent(String hash);

    /**
     * It unpause a torrent
     *
     * @param torrentHashes a list of torrent hashes to unpause
     * @return a result indicating whether the call completed with success or a fail
     */
    RequestResult unpauseTorrent(List<String> torrentHashes);

    /**
     * It unpause a torrent
     *z
     * @param hash torrent hash to unpause
     * @return a result indicating whether the call completed with success or a fail
     */
    RequestResult unpauseTorrent(String hash);

    /**
     * It rechecks a torrent
     *
     * @param torrentHashes a list of torrent hashes to recheck
     * @return a result indicating whether the call completed with success or a fail
     */
    RequestResult recheckTorrent(List<String> torrentHashes);

    /**
     * It rechecks a torrent
     *
     * @param hash torrent hash to recheck
     * @return a result indicating whether the call completed with success or a fail
     */
    RequestResult recheckTorrent(String hash);

    /**
     * It removes a torrent
     *
     * @param torrentHashes a list of torrent hashes to remove
     * @return a result indicating whether the call completed with success or a fail
     */
    RequestResult removeTorrent(List<String> torrentHashes);

    /**
     * It removes a torrent
     *
     * @param hash torrent hash to remove
     * @return a result indicating whether the call completed with success or a fail
     */
    RequestResult removeTorrent(String hash);

    /**
     * It removes a torrent with its data
     *
     * @param torrentHashes a list of torrent hashes to remove with their data
     * @return a result indicating whether the call completed with success or a fail
     */
    RequestResult removeDataTorrent(List<String> torrentHashes);

    /**
     * It removes a torrent with its data
     *
     * @param hash torrent hash to remove to remove with its data
     * @return a result indicating whether the call completed with success or a fail
     */
    RequestResult removeDataTorrent(String hash);

    /**
     * This action tells �Torrent to move the specified torrent to the bottom of the queue.
     *
     * @param hash the list of torrent hashes to move
     * @return a result indicating whether the call completed with success or a fail
     */
    RequestResult queueBottomTorrent(List<String> hash);

    /**
     * This action tells �Torrent to move the specified torrent to the bottom of the queue.
     *
     * @param hash the torrent hash to move
     * @return a result indicating whether the call completed with success or a fail
     */
    RequestResult queueBottomTorrent(String hash);

    /**
     * This action tells �Torrent to move the specified torrent one position up the queue.
     *
     * @param hash the list of torrent hashes to move
     * @return a result indicating whether the call completed with success or a fail
     */
    RequestResult queueUpTorrent(List<String> hash);

    /**
     * This action tells �Torrent to move the specified torrent one position up the queue
     *
     * @param hash the torrent hash to move
     * @return a result indicating whether the call completed with success or a fail
     */
    RequestResult queueUpTorrent(String hash);

    /**
     * This action tells �Torrent to move the specified torrent one position down the queue.
     *
     * @param hash the list of torrent hashes to move
     * @return a result indicating whether the call completed with success or a fail
     */
    RequestResult queueDownTorrent(List<String> hash);

    /**
     * This action tells �Torrent to move the specified torrent to the bottom of the queue.
     *
     * @param hash the torrent hash to move
     * @return a result indicating whether the call completed with success or a fail
     */
    RequestResult queueDownTorrent(String hash);

    /**
     * This action tells �Torrent to move the specified torrent to the top of the queue.
     *
     * @param hash the list of torrent hashes to move
     * @return a result indicating whether the call completed with success or a fail
     */
    RequestResult queueTopTorrent(List<String> hash);

    /**
     * This action tells �Torrent to move the specified torrent to the top of the queue.
     *
     * @param hash the torrent hash to move
     * @return a result indicating whether the call completed with success or a fail
     */
    RequestResult queueTopTorrent(String hash);

    /**
     * This action sets the priority for the specified file(s) in the torrent job.
     * A file is specified using the zero-based index of the file in the inside the list returned by "getfiles".
     * Only one priority level may be specified on each call to this action.
     * The possible priority levels are {@code DO_NOT_DOWNLOAD, LOW_PRIORITY, NORMAL_PRIORITY,HIGH_PRIORITY}.
     *
     * @param hash the torrent hash to set the priority to
     * @param priority the priority to set
     * @param fileIndices the list of file indices
     *
     * @return a result indicating whether the call completed with success or a fail
     */
    RequestResult setTorrentFilePriority(String hash, Priority priority, List<Integer> fileIndices);

    /**
     * This sets the specified property to the specified value for the torrent job.
     * Each v value is used as the value for the s property specified immediately before it.
     *
     * @param settingName setting name in string
     * @param settingValue value to set the specified setting
     * @return a result indicating whether the call completed with success or a fail
     */
    RequestResult setClientSetting(String settingName, String settingValue);

    /**
     * This sets the specified property to the specified value for the torrent job.
     * Multiple settings may be set simultaneously by with this action.
     * Each v value is used as the value for the s property specified immediately before it.
     *
     * @param settings a list of settings to change on the client
     * @return a result indicating whether the call completed with success or a fail
     */
    RequestResult setClientSetting(List<Request.QueryParam> settings);

    /**
     * It returns a set of torrent objects containing all the torrents currently
     * handled by the torrent client
     *
     * @return all torrents currently handled by the client
     */
    Set<Torrent> getAllTorrents();

    /**
     * This sets the specified property to the specified value for the torrent job.
     * Each v value is used as the value for the s property specified immediately before it.
     *
     * @param settingKey the setting key to update
     * @param settingValue the value to update the setting to
     * @return a result indicating whether the call completed with success or a fail
     */
    RequestResult setClientSetting(SettingsKey settingKey, String settingValue);

    /**
     * It returns the client settings.
     *
     * @return an object representing the client settings for the specified uTorrent instance.
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