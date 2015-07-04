package com.utorrent.webapiwrapper.core;

import com.google.gson.JsonSyntaxException;
import com.utorrent.webapiwrapper.core.entities.AssertEntities;
import com.utorrent.webapiwrapper.core.entities.Priority;
import com.utorrent.webapiwrapper.core.entities.TorrentFileList;
import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class MessageParserTest extends TestCase {

    private final int FILE_1_SIZE = 20;
    private final int FILE_1_DOWNLOADED = 30;
    private final Priority FILE_1_PRIORITY = Priority.NORMAL_PRIORITY;
    private final Priority FILE_2_PRIORITY = Priority.DO_NOT_DOWNLOAD;
    private final int FILE_2_SIZE = 50;
    private final int FILE_2_DOWNLOADED = 40;
    private final String FILE_1_NAME = "File 1";
    private final String FILE_2_NAME = "File 2";
    private final String HASH = "33FF";
    private MessageParser messageParser = new MessageParser();
    private final String TORRENT_FILE_LIST_MESSAGE = "{\"build\": 1111,\"files\":[\"" + HASH + "\",[[\"" + FILE_1_NAME + "\", " +
            FILE_1_SIZE + ", " + FILE_1_DOWNLOADED + ", " + FILE_1_PRIORITY.getValue() + "],[\"" + FILE_2_NAME + "\", " + FILE_2_SIZE + ", " +
            FILE_2_DOWNLOADED + ", " + FILE_2_PRIORITY.getValue() + "]]]}";


    @Test
    public void whenJSONMessageIsPassedThenParseItAsTorrentListSnapshot() throws Exception {

        TorrentFileList torrentFileList = messageParser.parseAsTorrentFileList(TORRENT_FILE_LIST_MESSAGE);
        assertEquals(HASH, torrentFileList.getHash());
        assertTrue(torrentFileList.getFiles().size() == 2);
        AssertEntities.assertEquals(torrentFileList.getFiles().get(0), FILE_1_NAME, FILE_1_SIZE, FILE_1_DOWNLOADED, FILE_1_PRIORITY);
        AssertEntities.assertEquals(torrentFileList.getFiles().get(1), FILE_2_NAME, FILE_2_SIZE, FILE_2_DOWNLOADED, FILE_2_PRIORITY);
    }

    @Test(expected = JsonSyntaxException.class)
    public void whenJSONMessageIsMalformedThenThrowAMalformedJsonException() throws Exception {
        String MALFORMED_MESSAGE = "{\"build\": 11111,\"files\":[\"33FF\",[[\"File 1\", 20, 30, 0],[\"File 2\", 40, 50, 1]}";
        messageParser.parseAsTorrentFileList(MALFORMED_MESSAGE);
    }

    @Test
    public void testParseAsTorrentFileList() throws Exception {
        return;
    }

    @Test
    public void testParseAsClientSettings() throws Exception {

    }

    @Test
    public void testParseAsTorrentProperties() throws Exception {

    }


}