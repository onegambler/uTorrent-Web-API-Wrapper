package com.utorrent.webapiwrapper.core;

import com.google.gson.JsonSyntaxException;
import com.utorrent.webapiwrapper.core.entities.AssertEntities;
import com.utorrent.webapiwrapper.core.entities.ClientSettings;
import com.utorrent.webapiwrapper.core.entities.Priority;
import com.utorrent.webapiwrapper.core.entities.TorrentFileList;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

public class MessageParserTest {

    @Test
    public void whenJSONMessageIsPassedThenParseItAsTorrentFileListSnapshot() throws Exception {

        TorrentFileList torrentFileList = messageParser.parseAsTorrentFileList(TORRENT_FILE_LIST_MESSAGE);
        assertEquals(HASH, torrentFileList.getHash());
        assertTrue(torrentFileList.getFiles().size() == 2);
        AssertEntities.assertEquals(torrentFileList.getFiles().get(0), FILE_1_NAME, FILE_1_SIZE, FILE_1_DOWNLOADED, FILE_1_PRIORITY);
        AssertEntities.assertEquals(torrentFileList.getFiles().get(1), FILE_2_NAME, FILE_2_SIZE, FILE_2_DOWNLOADED, FILE_2_PRIORITY);
    }

    @Test(expected = JsonSyntaxException.class)
    public void whenJSONMessageIsMalformedThenThrowAMalformedJsonException() throws Exception {
        String MALFORMED_MESSAGE = "{\"build\": " + BUILD_NUMBER + ",\"files\":[\"33FF\",[[\"File 1\", 2" + INT_SETTING_VALUE + ", 30, 0],[\"File 2\", 40, 50, 1]}";
        messageParser.parseAsTorrentFileList(MALFORMED_MESSAGE);
    }


    @Test
    public void whenJSONMessageIsPassedThenParseItAsClientSettings() throws Exception {
        ClientSettings clientSettings = messageParser.parseAsClientSettings(CLIENT_SETTINGS);

        assertEquals(3, clientSettings.getAllSettings().size());

        ClientSettings.Setting setting = clientSettings.getSetting(INT_SETTING);
        assertEquals(ClientSettings.SettingType.INTEGER, setting.getType());
        assertEquals(INT_SETTING_VALUE, setting.getValue());

        setting = clientSettings.getSetting(STRING_SETTING);
        assertEquals(ClientSettings.SettingType.STRING, setting.getType());
        assertEquals(STRING_SETTING_VALUE, setting.getValue());

        setting = clientSettings.getSetting(BOOLEAN_SETTING);
        assertEquals(ClientSettings.SettingType.BOOLEAN, setting.getType());
        assertEquals(BOOLEAN_SETTING_VALUE, setting.getValue());
    }

    @Test
    public void testParseAsTorrentProperties() throws Exception {

    }

    public static final int BUILD_NUMBER = 1111;
    public static final String INT_SETTING = "int_setting";
    public static final String INT_SETTING_VALUE = "5";
    public static final String STRING_SETTING = "string_setting";
    public static final String STRING_SETTING_VALUE = "value";
    public static final String BOOLEAN_SETTING = "boolean_setting";
    public static final String BOOLEAN_SETTING_VALUE = "true";
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
    private final String TORRENT_FILE_LIST_MESSAGE = "{\"build\": " + BUILD_NUMBER + ",\"files\":[\"" + HASH + "\",[[\"" + FILE_1_NAME + "\", " +
            FILE_1_SIZE + ", " + FILE_1_DOWNLOADED + ", " + FILE_1_PRIORITY.getValue() + "],[\"" + FILE_2_NAME + "\", " + FILE_2_SIZE + ", " +
            FILE_2_DOWNLOADED + ", " + FILE_2_PRIORITY.getValue() + "]]]}";
    private final String CLIENT_SETTINGS = "{\"build\": " + BUILD_NUMBER + ", \"settings\": [[\"" + INT_SETTING + "\", 0, \"" + INT_SETTING_VALUE + "\"]," +
            "[\"" + STRING_SETTING + "\", 2, \"" + STRING_SETTING_VALUE + "\"],[\"" + BOOLEAN_SETTING + "\", 1, \"" + BOOLEAN_SETTING_VALUE + "\"]]}";
}