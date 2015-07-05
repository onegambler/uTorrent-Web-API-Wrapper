package com.utorrent.webapiwrapper.core;

import com.google.gson.JsonSyntaxException;
import com.utorrent.webapiwrapper.core.entities.*;
import com.utorrent.webapiwrapper.utils.IOUtils;
import org.junit.Test;

import java.net.URL;
import java.nio.file.Paths;
import java.time.Duration;

import static java.util.Objects.requireNonNull;
import static junit.framework.Assert.*;
import static org.junit.Assert.assertArrayEquals;

public class MessageParserTest {

    private final MessageParser messageParser = new MessageParser();

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
    public void whenJSONMessageIsPassedThenParseItAsTorrentProperties() throws Exception {
        String message = getTestMessage("test/torrent.properties.json");
        TorrentProperties properties = messageParser.parseAsTorrentProperties(message);
        assertNotNull(properties);
        assertEquals(HASH, properties.getHash());
        assertArrayEquals(new String[]{"http://tracker.com"}, properties.getTrackers());
        assertEquals(1, properties.getUploadRate());
        assertEquals(2, properties.getDownloadRate());
        assertEquals(TorrentProperties.State.ENABLED, properties.getSuperSeed());
        assertEquals(TorrentProperties.State.ENABLED, properties.getUseDHT());
        assertEquals(TorrentProperties.State.NOT_ALLOWED, properties.getUsePEX());
        assertEquals(TorrentProperties.State.DISABLED, properties.getSeedOverride());
        assertEquals(7, properties.getSeedRatio());
        assertEquals(Duration.ofSeconds(8), properties.getSeedTime());
        assertEquals(9, properties.getUploadSlots());


    }

    private String getTestMessage(String fileName) throws Exception {
        URL resource = getClass().getClassLoader().getResource("test/torrent.properties.json");
        requireNonNull(resource);
        return IOUtils.readFileFully(Paths.get(resource.toURI()).toFile());
    }

    private final int BUILD_NUMBER = 1111;
    private final String INT_SETTING = "int_setting";
    private final String INT_SETTING_VALUE = "5";
    private final String STRING_SETTING = "string_setting";
    private final String STRING_SETTING_VALUE = "value";
    private final String BOOLEAN_SETTING = "boolean_setting";
    private final String BOOLEAN_SETTING_VALUE = "true";
    private final int FILE_1_SIZE = 20;
    private final int FILE_1_DOWNLOADED = 30;
    private final Priority FILE_1_PRIORITY = Priority.NORMAL_PRIORITY;
    private final Priority FILE_2_PRIORITY = Priority.DO_NOT_DOWNLOAD;
    private final int FILE_2_SIZE = 50;
    private final int FILE_2_DOWNLOADED = 40;
    private final String FILE_1_NAME = "File 1";
    private final String FILE_2_NAME = "File 2";
    private final String HASH = "HASH";
    private final String TRACKERS = "trackers";
    private final String ULRATE = "ulrate";
    private final String UPLOAD_LIMIT = "UPLOAD LIMIT";
    private final String DOWNLOAD_RATE_PROPERTY = "dlrate";
    private final String SUPERSEED_PROPERTY = "superseed";
    private final String DHT_PROPERTY = "dht";
    private final String PEX_SETTING = "pex";
    private final String SEED_OVERRIDE_PROPERTY = "seed_override";
    private final String SEED_OVERRIDE_PROPERTY_VALUE = "21";
    private final String TORRENT_FILE_LIST_MESSAGE = "{\"build\": " + BUILD_NUMBER + ",\"files\":[\"" + HASH + "\",[[\"" + FILE_1_NAME + "\", " +
            FILE_1_SIZE + ", " + FILE_1_DOWNLOADED + ", " + FILE_1_PRIORITY.getValue() + "],[\"" + FILE_2_NAME + "\", " + FILE_2_SIZE + ", " +
            FILE_2_DOWNLOADED + ", " + FILE_2_PRIORITY.getValue() + "]]]}";
    private final String CLIENT_SETTINGS = "{\"build\": " + BUILD_NUMBER + ", \"settings\": [[\"" + INT_SETTING + "\", 0, \"" + INT_SETTING_VALUE + "\"]," +
            "[\"" + STRING_SETTING + "\", 2, \"" + STRING_SETTING_VALUE + "\"],[\"" + BOOLEAN_SETTING + "\", 1, \"" + BOOLEAN_SETTING_VALUE + "\"]]}";

    private final String TORRENT_PROPERTIES_MESSAGE_FILE = "/resources/test/torrent.properties.json";
}