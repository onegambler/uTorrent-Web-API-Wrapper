# uTorrent-Web-API-Wrapper
Java wrapper for the uTorrent Web Client API.

Project Description
This library allows java developers to easily access the uTorrent Web Client API and control it using Java.
It provides a clean facade object over the uTorrent JSON API, and supports automatic updating and caching of results.

It has been developer using Java 8 and implements all the operations allowed through the Web API:

* List torrents
* Add torrents via URL and File
* Operations on torrents (start, pause, stop, delete, etc...)
* Retrieve and update client properties
* Set files priority

Set up
-------

Enable the web UI in utorrent: http://help.utorrent.com/customer/portal/articles/1573941

How to use
-------

    ConnectionParams connectionParams = ConnectionParams.builder()
                .withScheme("http")
                .withCredentials("username", "password")
                .withAddress("host.com", 8080)
                .withTimeout(1500)
                .create();
    UTorrentWebAPIClient client = UTorrentWebAPIClient.getClient();

    Set<Torrent> torrents = client.getAllTorrents();

    client.addTorrent(new File("filePath"));

    client.stopTorrent(Collections.singleton("hash"));
