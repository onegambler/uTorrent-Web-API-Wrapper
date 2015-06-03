package com.utorrent.webapiwrapper.core;

import com.utorrent.webapiwrapper.core.entities.Torrent;
import com.utorrent.webapiwrapper.core.entities.TorrentListSnapshot;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;

public class TorrentsCache {

    private final Map<String, Torrent> torrentsMap;

    private String cachedID;

    public TorrentsCache() {
        this.torrentsMap = new HashMap<>();
        this.cachedID = null;
    }

    public String getCachedID() {
        return cachedID;
    }

    public void addTorrent(Torrent torrent) {
        this.torrentsMap.put(torrent.getHash(), torrent);
    }

    public void removeTorrent(String hash) {
        this.torrentsMap.remove(hash);
    }

    public Torrent getTorrent(String hash) {
        return torrentsMap.get(hash);
    }

    public Set<Torrent> getTorrentList() {
        return torrentsMap.values().stream().collect(Collectors.toSet());
    }

    public void updateCache(TorrentListSnapshot torrentListSnapshot) {
        requireNonNull(torrentListSnapshot, "TorrentListSnapshot cannot be null");
        torrentListSnapshot.getTorrentsToAdd().forEach(this::addTorrent);
        torrentListSnapshot.getTorrentToRemoveHashes().forEach(this::removeTorrent);
        this.cachedID = torrentListSnapshot.getCacheID();
    }
}
