package com.utorrent.webapiwrapper.core.entities;

import java.util.HashSet;
import java.util.Set;

public class TorrentListSnapshot {

    private Set<Torrent> torrentsToAdd;
    private Set<String> torrentToRemoveHashes;
    private String cacheID;

    public TorrentListSnapshot() {
        torrentsToAdd = new HashSet<>();
        torrentToRemoveHashes = new HashSet<>();
    }

    public void addTorrentToAdd(Torrent torrent) {
        torrentsToAdd.add(torrent);
    }

    public void addTorrentToDelete(String hash) {
        this.torrentToRemoveHashes.add(hash);
    }

    public Set<Torrent> getTorrentsToAdd() {
        return torrentsToAdd;
    }

    public Set<String> getTorrentToRemoveHashes() {
        return torrentToRemoveHashes;
    }

    public String getCacheID() {
        return cacheID;
    }

    public void setCacheID(String cacheID) {
        this.cacheID = cacheID;
    }
}
