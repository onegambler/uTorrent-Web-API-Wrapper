package com.utorrent.webapiwrapper.core;

import com.google.gson.JsonSyntaxException;
import com.utorrent.webapiwrapper.core.entities.*;
import com.utorrent.webapiwrapper.utils.IOUtils;
import org.junit.Test;

import java.io.InputStream;
import java.time.Duration;



import static java.util.Objects.requireNonNull;
import static org.assertj.core.api.Assertions.assertThat;

public class MessageParserTest {

    private final String HASH = "HASH";

    private final MessageParser messageParser = new MessageParser();

    @Test
    public void whenJSONMessageIsPassedThenParseItAsTorrentFileListSnapshot() throws Exception {

        TorrentFileList torrentFileList = messageParser.parseAsTorrentFileList(getTestMessage("com/utorrent/webapiwrapper/core/torrent.list.json"));
        assertThat(torrentFileList.getHash()).isEqualTo(HASH);
        assertThat(torrentFileList.getFiles()).hasSize(2);
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

        assertThat(clientSettings.getAllSettings()).hasSize(3);

        ClientSettings.Setting setting = clientSettings.getSetting("int_setting");
        assertThat(setting.getType()).isEqualTo(ClientSettings.SettingType.INTEGER);
        assertThat(setting.getValue()).isEqualTo("6");

        setting = clientSettings.getSetting("string_setting");
        assertThat(setting.getType()).isEqualTo(ClientSettings.SettingType.STRING);
        assertThat(setting.getValue()).isEqualTo("string");


        setting = clientSettings.getSetting("boolean_setting");
        assertThat(setting.getType()).isEqualTo(ClientSettings.SettingType.BOOLEAN);
        assertThat(setting.getValue()).isEqualTo("true");
    }

    @Test
    public void whenJSONMessageIsPassedThenParseItAsTorrentProperties() throws Exception {
        String message = getTestMessage("com/utorrent/webapiwrapper/core/torrent.properties.json");
        TorrentProperties properties = messageParser.parseAsTorrentProperties(message);
        assertThat(properties).isNotNull();
        assertThat(properties.getHash()).isEqualTo(HASH);
        assertThat(properties.getTrackers()).containsExactly("http://tracker.com");
        assertThat(properties.getUploadRate()).isEqualTo(1);
        assertThat(properties.getDownloadRate()).isEqualTo(2);
        assertThat(properties.getSuperSeed()).isEqualTo(TorrentProperties.State.ENABLED);
        assertThat(properties.getUseDHT()).isEqualTo(TorrentProperties.State.ENABLED);
        assertThat(properties.getUsePEX()).isEqualTo(TorrentProperties.State.NOT_ALLOWED);
        assertThat(properties.getSeedOverride()).isEqualTo(TorrentProperties.State.DISABLED);
        assertThat(properties.getSeedRatio()).isEqualTo(7);
        assertThat(properties.getSeedTime()).isEqualTo(Duration.ofSeconds(8));
        assertThat(properties.getUploadSlots()).isEqualTo(9);
    }

    private String getTestMessage(String fileName) throws Exception {
        InputStream resource = getClass().getClassLoader().getResourceAsStream(fileName);
        requireNonNull(resource);
        return IOUtils.readFileFully(resource);
    }

}