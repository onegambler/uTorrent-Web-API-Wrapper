package com.utorrent.webapiwrapper.core;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;

import com.utorrent.webapiwrapper.core.entities.*;
import com.utorrent.webapiwrapper.restclient.Request;

/**
 * uTorrent WebUI API interface. 
 * It allows to interact with uTorrent through its web API.
 * 
 * @author Roberto Magale
 * 10/apr/2012
 */
 interface UTorrentWebAPIClient extends Closeable {

	 RequestResult addTorrent(String url) throws IOException;
	
	 RequestResult addTorrent(File torrentFile) throws IOException;

	 Torrent getTorrent(String torrentHash) throws IOException;

	 TorrentFileList getTorrentFiles(List<String> torrentHashes);
	
	 String getTorrentProperties(List<String> torrentHash);
	
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
}
