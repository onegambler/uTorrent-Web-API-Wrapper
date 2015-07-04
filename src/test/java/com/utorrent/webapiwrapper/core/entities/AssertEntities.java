package com.utorrent.webapiwrapper.core.entities;

import org.junit.Assert;

public class AssertEntities {

    private AssertEntities(){}

    public static void assertEquals(TorrentFileList.File file, String name, int size, int downloaded, Priority priority) {
        Assert.assertEquals(name, file.getName());
        Assert.assertEquals(size, file.getSize());
        Assert.assertEquals(downloaded, file.getDownloaded());
        Assert.assertEquals(priority, file.getPriority());
    }

    private void assertEquals(Torrent expectedTorrent, Torrent torrentToTest) {
        Assert.assertEquals(expectedTorrent.getName(), torrentToTest.getName());
        Assert.assertEquals(expectedTorrent.getHash(), torrentToTest.getHash());
        Assert.assertEquals(expectedTorrent.getLabel(), torrentToTest.getLabel());
        Assert.assertEquals(expectedTorrent.getTorrentQueueOrder(), torrentToTest.getTorrentQueueOrder());
        Assert.assertEquals(expectedTorrent.getAvailability(), torrentToTest.getAvailability());
        Assert.assertEquals(expectedTorrent.getDownloaded(), torrentToTest.getDownloaded());
        Assert.assertEquals(expectedTorrent.getDownloadSpeed(), torrentToTest.getDownloadSpeed());
        Assert.assertEquals(expectedTorrent.getPeersConnected(), torrentToTest.getPeersConnected());
        Assert.assertEquals(expectedTorrent.getPeersInSwarm(), torrentToTest.getPeersInSwarm());
        Assert.assertEquals(expectedTorrent.getPath(), torrentToTest.getPath());
        Assert.assertEquals(expectedTorrent.getEta(), torrentToTest.getEta());
        Assert.assertEquals(expectedTorrent.getRatio(), torrentToTest.getRatio());
        Assert.assertEquals(expectedTorrent.getRemaining(), torrentToTest.getRemaining());
        Assert.assertEquals(expectedTorrent.getStatuses(), torrentToTest.getStatuses());
        Assert.assertEquals(expectedTorrent.getProgress(), torrentToTest.getProgress(), 0d);
        Assert.assertEquals(expectedTorrent.getSize(), torrentToTest.getSize());
        Assert.assertEquals(expectedTorrent.getUploaded(), torrentToTest.getUploaded());
        Assert.assertEquals(expectedTorrent.getUploadSpeed(), torrentToTest.getUploadSpeed());
        Assert.assertEquals(expectedTorrent.getStatuses(), torrentToTest.getStatuses());
    }
}
