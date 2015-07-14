package com.utorrent.webapiwrapper.core;

import com.google.common.collect.ImmutableList;
import com.utorrent.webapiwrapper.core.entities.RequestResult;
import com.utorrent.webapiwrapper.core.entities.Torrent;
import com.utorrent.webapiwrapper.core.entities.TorrentFileList;
import com.utorrent.webapiwrapper.core.entities.TorrentListSnapshot;
import com.utorrent.webapiwrapper.restclient.ConnectionParams;
import com.utorrent.webapiwrapper.restclient.RESTClient;
import com.utorrent.webapiwrapper.restclient.Request;
import com.utorrent.webapiwrapper.restclient.exceptions.BadRequestException;
import org.apache.http.ProtocolVersion;
import org.apache.http.StatusLine;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.message.BasicStatusLine;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.File;
import java.net.URI;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class UTorrentWebAPIClientImplTest {

    public static final String TOKEN_VALUE = "token";
    @Mock
    private RESTClient restClient;
    @Mock
    private MessageParser parser;

    private UTorrentWebAPIClientImpl client;

    private URI serverURI;

    private final String URI_ACTION_STRING_TEMPLATE = "%s?action=%s";

    @Before
    public void setUp() throws Exception {
        ConnectionParams connectionParams = ConnectionParams.builder()
                .withScheme("http")
                .withCredentials("username", "password")
                .withAddress("host.com", 8080)
                .withTimeout(1500)
                .create();

        serverURI = new URIBuilder()
                .setScheme(connectionParams.getScheme())
                .setHost(connectionParams.getHost())
                .setPort(connectionParams.getPort())
                .setPath("/gui/").build();

        client = new UTorrentWebAPIClientImpl(restClient, connectionParams, parser);
        when(restClient.get(Request.builder().setDestination(serverURI.resolve("token.html")).create())).thenReturn(TOKEN_VALUE);
    }

    @Test
    public void testInvokeWithAuthentication() {
        File torrentFile = new File("fakePath");
        when(restClient.post(any())).thenReturn("build");
        when(parser.parseAsTorrentListSnapshot(anyString())).thenReturn(new TorrentListSnapshot());
        StatusLine statusLine = new BasicStatusLine(new ProtocolVersion("protocol", 0, 1), 5, "reason");
        when(restClient.get(any())).thenReturn(TOKEN_VALUE);
        client.addTorrent(torrentFile);
        when(restClient.post(any())).thenThrow(new BadRequestException(statusLine));
        assertThatThrownBy(() -> client.addTorrent(torrentFile))
                .isInstanceOf(UTorrentAuthException.class)
                .hasRootCauseInstanceOf(BadRequestException.class)
                .hasMessage("Impossible to connect to uTorrents, wrong username or password");
        verify(restClient, times(2)).get(any());
    }

    @Test
    public void testAddTorrentWithFileSuccess() throws Exception {
        File torrentFile = new File("fakePath");

        ArgumentCaptor<Request> argumentCaptor = ArgumentCaptor.forClass(Request.class);
        when(restClient.post(argumentCaptor.capture())).thenReturn("build");

        RequestResult requestResult = client.addTorrent(torrentFile);
        assertThat(requestResult).isEqualTo(RequestResult.SUCCESS);

        Request request = argumentCaptor.getValue();
        assertThat(request.getParams()).isEmpty();
        assertThat(request.getUri()).hasToString(String.format(URI_ACTION_STRING_TEMPLATE, serverURI.toString(), Action.ADD_FILE.getName()) + "&token=" + TOKEN_VALUE);
        assertThat(request.getFiles()).hasSize(1);
        Request.FilePart filePart = request.getFiles().stream().findFirst().get();
        assertThat(torrentFile).isEqualTo(filePart.getFile());
        assertThat(filePart.getName()).isEqualTo("torrent_file");
        assertThat(filePart.getContentType()).isEqualTo(UTorrentWebAPIClient.APPLICATION_X_BIT_TORRENT_CONTENT_TYPE);
    }

    @Test
    public void testAddTorrentWithFileFail() throws Exception {
        File torrentFile = new File("fakePath");

        ArgumentCaptor<Request> argumentCaptor = ArgumentCaptor.forClass(Request.class);
        when(restClient.post(argumentCaptor.capture())).thenReturn("wrong");

        RequestResult requestResult = client.addTorrent(torrentFile);
        assertThat(requestResult).isEqualTo(RequestResult.FAIL);
    }

    @Test
    public void testAddTorrentWithStringPathSuccess() throws Exception {
        String torrentURL = "fakeURL";
        ArgumentCaptor<Request> argumentCaptor = ArgumentCaptor.forClass(Request.class);
        when(restClient.get(argumentCaptor.capture())).thenReturn("build");

        RequestResult requestResult = client.addTorrent(torrentURL);
        assertThat(requestResult).isEqualTo(RequestResult.SUCCESS);

        String uriString = String.format(URI_ACTION_STRING_TEMPLATE, serverURI, Action.ADD_URL.getName()) + "&s=fakeURL&token=build";
        assertThat(argumentCaptor.getValue().getUri()).hasToString(uriString);
    }

    @Test
    public void testGetTorrentList() throws Exception {
        when(restClient.get(any(Request.class))).thenReturn("build");
        TorrentListSnapshot torrentListSnapshot = new TorrentListSnapshot();
        String cacheID = "cacheID_1";
        torrentListSnapshot.setCacheID(cacheID);
        Torrent firstTorrent = Torrent.builder().hash("hash_1").build();
        Torrent secondTorrent = Torrent.builder().hash("hash_2").build();
        torrentListSnapshot.addTorrentToAdd(firstTorrent);
        torrentListSnapshot.addTorrentToAdd(secondTorrent);
        when(parser.parseAsTorrentListSnapshot(anyString())).thenReturn(torrentListSnapshot);
        assertThat(torrentListSnapshot.getCacheID()).isEqualTo(cacheID);

        Set<Torrent> torrentList = client.getTorrentList();
        assertThat(torrentList).containsOnly(firstTorrent, secondTorrent);

        torrentListSnapshot.getTorrentsToAdd().clear();
        torrentListSnapshot.addTorrentToDelete(secondTorrent.getHash());
        cacheID = "cacheID_2";
        torrentListSnapshot.setCacheID(cacheID);
        torrentList = client.getTorrentList();
        assertThat(torrentList).containsOnly(firstTorrent);
        assertThat(torrentListSnapshot.getCacheID()).isEqualTo(cacheID);

        torrentListSnapshot.getTorrentsToAdd().clear();
        torrentListSnapshot.getTorrentToRemoveHashes().clear();
        torrentListSnapshot.addTorrentToAdd(secondTorrent);
        Torrent thirdTorrent = Torrent.builder().hash("hash_3").build();
        torrentListSnapshot.addTorrentToAdd(thirdTorrent);
        torrentListSnapshot.addTorrentToDelete(firstTorrent.getHash());
        cacheID = "cacheID_3";
        torrentListSnapshot.setCacheID(cacheID);
        torrentList = client.getTorrentList();
        assertThat(torrentList).containsOnly(secondTorrent, thirdTorrent);
        assertThat(torrentListSnapshot.getCacheID()).isEqualTo(cacheID);
    }

    @Test
    public void testGetTorrent() throws Exception {
        String hashFirstTorrent = "hash_1";
        Torrent firstTorrent = Torrent.builder().hash(hashFirstTorrent).build();
        String hashSecondTorrent = "hash_2";
        Torrent secondTorrent = Torrent.builder().hash(hashSecondTorrent).build();
        when(restClient.get(any(Request.class))).thenReturn("build");
        TorrentListSnapshot torrentListSnapshot = new TorrentListSnapshot();
        torrentListSnapshot.addTorrentToAdd(firstTorrent);
        torrentListSnapshot.addTorrentToAdd(secondTorrent);
        when(parser.parseAsTorrentListSnapshot(anyString())).thenReturn(torrentListSnapshot);
        assertThat(client.getTorrent(hashFirstTorrent)).isEqualTo(firstTorrent);
        assertThat(client.getTorrent(hashSecondTorrent)).isEqualTo(secondTorrent);
    }

    @Test
    public void testGetTorrentFiles() throws Exception {
        String nameFirstTorrent = "file_1";
        TorrentFileList.File firstTorrent = TorrentFileList.File.builder().name(nameFirstTorrent).build();
        String nameSecondTorrent = "file_2";
        TorrentFileList.File secondTorrent = TorrentFileList.File.builder().name(nameSecondTorrent).build();

        String existingHash = "hash_1";
        when(restClient.get(any(Request.class))).thenReturn(existingHash);
        TorrentFileList torrentFileList = new TorrentFileList();
        torrentFileList.addFile(firstTorrent);
        torrentFileList.addFile(secondTorrent);
        torrentFileList.setHash(existingHash);
        when(parser.parseAsTorrentFileList(existingHash)).thenReturn(torrentFileList);

        TorrentFileList result = client.getTorrentFiles(ImmutableList.of(existingHash));
        assertThat(result).isEqualTo(torrentFileList);

    }

    @Test
    public void testGetTorrentProperties() throws Exception {
        //client.getTorrentProperties();
    }

    @Test
    public void testStartTorrent() throws Exception {

    }

    @Test
    public void testStopTorrent() throws Exception {

    }

    @Test
    public void testPauseTorrent() throws Exception {

    }

    @Test
    public void testForceStartTorrent() throws Exception {

    }

    @Test
    public void testUnpauseTorrent() throws Exception {

    }

    @Test
    public void testRecheckTorrent() throws Exception {

    }

    @Test
    public void testRemoveTorrent() throws Exception {

    }

    @Test
    public void testRemoveDataTorrent() throws Exception {

    }

    @Test
    public void testSetTorrentFilePriority() throws Exception {

    }

    @Test
    public void testSetClientSetting() throws Exception {

    }

    @Test
    public void testSetClientSetting1() throws Exception {

    }

    @Test
    public void testSetClientSetting2() throws Exception {

    }

    @Test
    public void testGetClientSettings() throws Exception {

    }
}