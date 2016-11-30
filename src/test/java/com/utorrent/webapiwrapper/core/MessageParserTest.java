package com.utorrent.webapiwrapper.core;

import com.google.gson.JsonSyntaxException;
import com.utorrent.webapiwrapper.core.entities.ClientSettings;
import com.utorrent.webapiwrapper.core.entities.Torrent;
import com.utorrent.webapiwrapper.core.entities.TorrentFileList;
import com.utorrent.webapiwrapper.core.entities.TorrentListSnapshot;
import com.utorrent.webapiwrapper.core.entities.TorrentProperties;
import com.utorrent.webapiwrapper.utils.IOUtils;
import org.junit.Test;

import java.io.InputStream;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.utorrent.webapiwrapper.core.entities.AssertEntities.assertEquals;
import static com.utorrent.webapiwrapper.core.entities.ClientSettings.SettingType.BOOLEAN;
import static com.utorrent.webapiwrapper.core.entities.ClientSettings.SettingType.INTEGER;
import static com.utorrent.webapiwrapper.core.entities.ClientSettings.SettingType.STRING;
import static com.utorrent.webapiwrapper.core.entities.Priority.DO_NOT_DOWNLOAD;
import static com.utorrent.webapiwrapper.core.entities.Priority.NORMAL_PRIORITY;
import static com.utorrent.webapiwrapper.core.entities.TorrentProperties.State.DISABLED;
import static com.utorrent.webapiwrapper.core.entities.TorrentProperties.State.ENABLED;
import static com.utorrent.webapiwrapper.core.entities.TorrentProperties.State.NOT_ALLOWED;
import static java.util.Objects.requireNonNull;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNull;

public class MessageParserTest {

    private final String HASH_1 = "HASH_1";
    private final String HASH_2 = "HASH_2";

    private final MessageParser messageParser = new MessageParser();

    @Test
    public void whenJSONMessageIsPassedThenParseItAsTorrentFileListSnapshot() throws Exception {

        Set<TorrentFileList> torrentFileLists = messageParser.parseAsTorrentFileList(getTestMessage("com/utorrent/webapiwrapper/core/torrent.list.json"));

        List<String> hashesList = torrentFileLists.stream().map(TorrentFileList::getHash).collect(Collectors.toList());
        assertThat(hashesList).containsOnly(HASH_1, HASH_2);

        for (TorrentFileList torrentFileList : torrentFileLists) {
            assertThat(torrentFileList.getFiles()).hasSize(2);
            assertEquals(torrentFileList.getFiles().get(0), "File_1", 3899654144L, 0, NORMAL_PRIORITY, 0, 1050);
            assertEquals(torrentFileList.getFiles().get(1), "File_2", 112, 0, DO_NOT_DOWNLOAD, 1049, 1);
        }
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
        assertThat(setting.getType()).isEqualTo(INTEGER);
        assertThat(setting.getValue()).isEqualTo("6");

        setting = clientSettings.getSetting("string_setting");
        assertThat(setting.getType()).isEqualTo(STRING);
        assertThat(setting.getValue()).isEqualTo("string");


        setting = clientSettings.getSetting("boolean_setting");
        assertThat(setting.getType()).isEqualTo(BOOLEAN);
        assertThat(setting.getValue()).isEqualTo("true");
    }

    @Test
    public void whenJSONMessageIsPassedThenParseItAsTorrentProperties() throws Exception {
        String message = getTestMessage("com/utorrent/webapiwrapper/core/torrent.properties.json");
        Set<TorrentProperties> properties = messageParser.parseAsTorrentProperties(message);
        assertThat(properties).isNotNull();

        List<String> collect = properties.stream().map(TorrentProperties::getHash).collect(Collectors.toList());
        assertThat(collect).hasSameElementsAs(Arrays.asList(HASH_1, HASH_2));

        for (TorrentProperties property : properties) {
            assertThat(property.getTrackers()).containsExactly("http://tracker.com");
            assertThat(property.getUploadRate()).isEqualTo(1);
            assertThat(property.getDownloadRate()).isEqualTo(2);
            assertThat(property.getSuperSeed()).isEqualTo(ENABLED);
            assertThat(property.getUseDHT()).isEqualTo(ENABLED);
            assertThat(property.getUsePEX()).isEqualTo(NOT_ALLOWED);
            assertThat(property.getSeedOverride()).isEqualTo(DISABLED);
            assertThat(property.getSeedRatio()).isEqualTo(7);
            assertThat(property.getSeedTime()).isEqualTo(Duration.ofSeconds(8));
            assertThat(property.getUploadSlots()).isEqualTo(9);
        }
    }

    @Test
    public void whenJSONMessageIsPassedThenParseItAsTorrentSnapshot() throws Exception {
        String message = getTestMessage("com/utorrent/webapiwrapper/core/torrent.snapshot.json");
        TorrentListSnapshot snapshot = messageParser.parseAsTorrentListSnapshot(message);
        assertThat(snapshot).isNotNull();

        assertThat(snapshot.getCacheID()).isEqualTo("528600545");
        assertThat(snapshot.getTorrentToRemoveHashes()).containsExactly("HASH_TO_REMOVE");
        assertThat(snapshot.getTorrentsToAdd()).hasSize(3);
        assertThat(snapshot.getTorrentsToAdd().stream().map(Torrent::getHash).collect(Collectors.toList()))
                .containsOnly("1B1C06C35E76108149FEAE1072C71CD0E5712D71", "37C6B38465E1D9E70D3FC30E9B99832C905201FE", "45A99097064EB6158A35AF15B677643B01E2C89E");
    }

    @Test
    public void whenJSONMessageNotContainsEnoughEntries() throws Exception {
        String message = getTestMessage("com/utorrent/webapiwrapper/core/torrent.snapshot.old-build.json");
        TorrentListSnapshot snapshot = messageParser.parseAsTorrentListSnapshot(message);
        assertThat(snapshot).isNotNull();

        assertThat(snapshot.getCacheID()).isEqualTo("247996221");
        assertThat(snapshot.getTorrentToRemoveHashes()).isEmpty();
        assertThat(snapshot.getTorrentsToAdd()).hasSize(1);

        snapshot.getTorrentsToAdd().forEach(torrent -> {
            assertNull("StatusMessage should be null", torrent.getStatusMessage());
            assertThat(torrent.getStreamId()).isEqualTo(0);
            assertNull("DateAdded should be null", torrent.getDateAdded());
            assertNull("DownloadURL should be null", torrent.getDownloadURL());
            assertNull("RssFeedURL should be null", torrent.getRssFeedURL());
            assertNull("AppUpdateURL should be null", torrent.getAppUpdateURL());
            assertNull("DateCompleted should be null", torrent.getDateCompleted());
            assertNull("Path should be null", torrent.getPath());
        });
    }

    private String getTestMessage(String fileName) throws Exception {
        InputStream resource = getClass().getClassLoader().getResourceAsStream(fileName);
        requireNonNull(resource);
        return IOUtils.readFileFully(resource);
    }

}