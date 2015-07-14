package com.utorrent.webapiwrapper.core;

import com.utorrent.webapiwrapper.core.entities.RequestResult;
import com.utorrent.webapiwrapper.core.entities.TorrentFileList;
import com.utorrent.webapiwrapper.restclient.ConnectionParams;
import com.utorrent.webapiwrapper.restclient.RESTClient;
import com.utorrent.webapiwrapper.restclient.Request;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.hamcrest.CoreMatchers;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.matchers.JUnitMatchers;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.File;
import java.net.URI;
import java.util.Optional;

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UTorrentWebAPIClientImplTest {

    public static final String TOKEN_VALUE = "token";
    @Mock
    private RESTClient restClient;
    @Mock
    private MessageParser parser;

    private UTorrentWebAPIClientImpl client;

    private static final String EXPECTED_URI_STRING = "";

    private URI serverURI;

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
    public void testAddTorrentWithFileSuccess() throws Exception {
        File torrentFile = new File("fakePath");

        ArgumentCaptor<Request> argumentCaptor = ArgumentCaptor.forClass(Request.class);
        when(restClient.post(argumentCaptor.capture())).thenReturn("build");

        RequestResult requestResult = client.addTorrent(torrentFile);
        assertEquals(RequestResult.SUCCESS, requestResult);

        Request request = argumentCaptor.getValue();
        assertThat(request.getParams(), empty());
        assertEquals(serverURI.toString() + "?action=" + Action.ADD_FILE.getName() + "&token=" + TOKEN_VALUE, request.getUri().toString());
        assertThat(request.getFiles(), hasSize(1));
        Request.FilePart filePart = request.getFiles().stream().findFirst().get();
        assertEquals(torrentFile, filePart.getFile());
        assertEquals("torrent_file", filePart.getName());
        assertThat(filePart.getContentType(), is(UTorrentWebAPIClient.APPLICATION_X_BIT_TORRENT_CONTENT_TYPE));
    }

    @Test
    public void testAddTorrentWithFileFail() throws Exception {
        File torrentFile = new File("fakePath");

        ArgumentCaptor<Request> argumentCaptor = ArgumentCaptor.forClass(Request.class);
        when(restClient.post(argumentCaptor.capture())).thenReturn("wrong");

        RequestResult requestResult = client.addTorrent(torrentFile);
        assertEquals(RequestResult.FAIL, requestResult);
    }

    @Test
    public void testAddTorrentWithStringPathSuccess() throws Exception {
        String torrentURL = "fakeURL";

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