package com.utorrent.webapiwrapper.core;

import com.utorrent.webapiwrapper.core.entities.Torrent;
import com.utorrent.webapiwrapper.core.entities.TorrentListSnapshot;
import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(JUnit4.class)
public class TorrentsCacheTest extends TestCase {

    private final Torrent firstTorrent = Torrent.builder().hash(HASH_1).build();
    private final Torrent secondTorrent = Torrent.builder().hash(HASH_2).build();
    private final Torrent thirdTorrent = Torrent.builder().hash(HASH_3).build();

    @Test
    public void whenSnapshotAddsNewTorrentsCacheUpdatesCorrectly() throws Exception {
        TorrentsCache cache = new TorrentsCache();

        TorrentListSnapshot torrentListSnapshot = new TorrentListSnapshot();
        torrentListSnapshot.addTorrentToAdd(firstTorrent);
        torrentListSnapshot.addTorrentToAdd(secondTorrent);
        torrentListSnapshot.setCacheID("1");

        cache.updateCache(torrentListSnapshot);

        assertThat(cache.getTorrent(HASH_1)).isEqualTo(firstTorrent);
        assertThat(cache.getTorrent(HASH_2)).isEqualTo(secondTorrent);
        assertThat(cache.getTorrentList()).containsOnly(firstTorrent, secondTorrent);
        assertThat(cache.getCachedID()).isEqualTo("1");
    }

    @Test
    public void whenSnapshotRemovesTorrentsCacheUpdatesCorrectly() {

        TorrentsCache cache = new TorrentsCache();
        cache.addTorrent(firstTorrent);
        cache.addTorrent(secondTorrent);

        TorrentListSnapshot torrentListSnapshot = new TorrentListSnapshot();
        torrentListSnapshot.setCacheID("2");
        torrentListSnapshot.addTorrentToDelete(firstTorrent.getHash());
        torrentListSnapshot.addTorrentToDelete(secondTorrent.getHash());

        cache.updateCache(torrentListSnapshot);

        assertThat(cache.getTorrent(HASH_1)).isNull();
        assertThat(cache.getTorrent(HASH_2)).isNull();
        assertThat(cache.getTorrentList()).isEmpty();
        assertThat(cache.getCachedID()).isEqualTo("2");
    }

    @Test
    public void whenSnapshotAddsAndRemoveTorrentsCacheUpdatesCorrectly() {

        TorrentsCache cache = new TorrentsCache();
        cache.addTorrent(firstTorrent);
        cache.addTorrent(secondTorrent);

        TorrentListSnapshot torrentListSnapshot = new TorrentListSnapshot();
        torrentListSnapshot.setCacheID("3");
        torrentListSnapshot.addTorrentToDelete(firstTorrent.getHash());
        torrentListSnapshot.addTorrentToDelete(secondTorrent.getHash());
        torrentListSnapshot.addTorrentToAdd(thirdTorrent);

        cache.updateCache(torrentListSnapshot);

        assertThat(cache.getTorrent(HASH_1)).isNull();
        assertThat(cache.getTorrent(HASH_2)).isNull();
        assertThat(cache.getTorrent(HASH_3)).isSameAs(thirdTorrent);
        assertThat(cache.getTorrentList()).containsExactly(thirdTorrent);
        assertThat(cache.getCachedID()).isEqualTo("3");
    }

    public static final String HASH_1 = "hash_1";
    public static final String HASH_2 = "hash_2";
    public static final String HASH_3 = "hash_3";
}