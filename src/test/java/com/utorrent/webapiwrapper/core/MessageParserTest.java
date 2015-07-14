package com.utorrent.webapiwrapper.core;

import com.google.gson.JsonSyntaxException;
import com.utorrent.webapiwrapper.core.entities.*;
import com.utorrent.webapiwrapper.utils.IOUtils;
import org.junit.Test;

import java.io.InputStream;
import java.time.Duration;

import static java.util.Objects.requireNonNull;
import static org.junit.Assert.*;

public class MessageParserTest {

    private final String HASH = "HASH";

    private final MessageParser messageParser = new MessageParser();

    @Test
    public void whenJSONMessageIsPassedThenParseItAsTorrentFileListSnapshot() throws Exception {

        TorrentFileList torrentFileList = messageParser.parseAsTorrentFileList(getTestMessage("com/utorrent/webapiwrapper/core/torrent.list.json"));
        assertEquals(HASH, torrentFileList.getHash());
        assertTrue(torrentFileList.getFiles().size() == 2);
        AssertEntities.assertEquals(torrentFileList.getFiles().get(0), "File_1", 1024, 27, Priority.NORMAL_PRIORITY);
        AssertEntities.assertEquals(torrentFileList.getFiles().get(1), "File_2", 2048, 54, Priority.DO_NOT_DOWNLOAD);
    }

    @Test(expected = JsonSyntaxException.class)
    public void whenJSONMessageIsMalformedThenThrowAMalformedJsonException() throws Exception {
        int BUILD_NUMBER = 1111;
        String INT_SETTING_VALUE = "5";
        String MALFORMED_MESSAGE = "{\"build\": " + BUILD_NUMBER + ",\"files\":[\"33FF\",[[\"File 1\", 2" + INT_SETTING_VALUE + ", 30, 0],[\"File 2\", 40, 50, 1]}";
        messageParser.parseAsTorrentFileList(MALFORMED_MESSAGE);
    }


    @Test
    public void whenJSONMessageIsPassedThenParseItAsClientSettings() throws Exception {
        ClientSettings clientSettings = messageParser.parseAsClientSettings(getTestMessage("com/utorrent/webapiwrapper/core/client.settings.json"));

        assertEquals(3, clientSettings.getAllSettings().size());

        ClientSettings.Setting setting = clientSettings.getSetting("int_setting");
        assertEquals(ClientSettings.SettingType.INTEGER, setting.getType());
        assertEquals("6", setting.getValue());

        setting = clientSettings.getSetting("string_setting");
        assertEquals(ClientSettings.SettingType.STRING, setting.getType());
        assertEquals("string", setting.getValue());

        setting = clientSettings.getSetting("boolean_setting");
        assertEquals(ClientSettings.SettingType.BOOLEAN, setting.getType());
        assertEquals("true", setting.getValue());
    }

    @Test
    public void whenJSONMessageIsPassedThenParseItAsTorrentProperties() throws Exception {
        String message = getTestMessage("com/utorrent/webapiwrapper/core/torrent.properties.json");
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
        InputStream resource = getClass().getClassLoader().getResourceAsStream(fileName);
        requireNonNull(resource);
        return IOUtils.readFileFully(resource);
    }

}