package com.utorrent.webapiwrapper.core;

import com.utorrent.webapiwrapper.core.entities.RequestResult;
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
        when(restClient.get(serverURI.resolve("token.html"))).thenReturn(TOKEN_VALUE);
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
        ArgumentCaptor<URI> argumentCaptor = ArgumentCaptor.forClass(URI.class);
        when(restClient.get(argumentCaptor.capture())).thenReturn("build");

        RequestResult requestResult = client.addTorrent(torrentURL);
        assertThat(requestResult).isEqualTo(RequestResult.SUCCESS);

        String uriString = String.format(URI_ACTION_STRING_TEMPLATE, serverURI, Action.ADD_URL.getName()) + "&s=fakeURL&token=build";
        assertThat(argumentCaptor.getValue()).hasToString(uriString);
    }

    @Test
    public void testGetTorrentList() throws Exception {

    }

    @Test
    public void testGetTorrent() throws Exception {

    }

    @Test
    public void testGetTorrentFiles() throws Exception {

    }

    @Test
    public void testGetTorrentProperties() throws Exception {

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