package com.utorrent.webapiwrapper.core;

import com.google.common.collect.ImmutableList;
import com.utorrent.webapiwrapper.core.entities.*;
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
import java.util.List;
import java.util.Set;
import java.util.function.Function;

import static com.utorrent.webapiwrapper.restclient.Request.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class UTorrentWebAPIClientImplTest {

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
        when(restClient.get(builder().setDestination(serverURI.resolve("token.html")).create())).thenReturn(TOKEN_VALUE);
    }

    @Test
    public void testInvokeWithAuthentication() {
        File torrentFile = new File("fakePath");
        when(restClient.post(any())).thenReturn(BUILD_STRING);
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
        when(restClient.post(argumentCaptor.capture())).thenReturn(BUILD_STRING);

        RequestResult requestResult = client.addTorrent(torrentFile);
        assertThat(requestResult).isEqualTo(RequestResult.SUCCESS);

        Request request = argumentCaptor.getValue();
        FilePart filePart = new FilePart("torrent_file", torrentFile, UTorrentWebAPIClient.APPLICATION_X_BIT_TORRENT_CONTENT_TYPE);

        validateRequest(Action.ADD_FILE, request, ImmutableList.of(), ImmutableList.of(filePart));
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
        when(restClient.get(argumentCaptor.capture())).thenReturn(TOKEN_VALUE);
        client.addTorrent(torrentURL);

        when(restClient.get(argumentCaptor.capture())).thenReturn(BUILD_STRING);
        RequestResult requestResult = client.addTorrent(torrentURL);
        assertThat(requestResult).isEqualTo(RequestResult.SUCCESS);

        QueryParam expectedURL = new QueryParam(UTorrentWebAPIClientImpl.URL_PARAM_NAME, torrentURL);

        Request actualRequest = argumentCaptor.getValue();
        validateRequest(Action.ADD_URL, actualRequest, ImmutableList.of(expectedURL));
    }

    @Test
    public void testGetTorrentList() throws Exception {
        when(restClient.get(any(Request.class))).thenReturn(BUILD_STRING);
        TorrentListSnapshot torrentListSnapshot = new TorrentListSnapshot();
        Torrent firstTorrent = Torrent.builder().hash("hash_1").build();
        Torrent secondTorrent = Torrent.builder().hash("hash_2").build();
        torrentListSnapshot.addTorrentToAdd(firstTorrent);
        torrentListSnapshot.addTorrentToAdd(secondTorrent);
        when(parser.parseAsTorrentListSnapshot(anyString())).thenReturn(torrentListSnapshot);

        Set<Torrent> torrentList = client.getTorrentList();
        assertThat(torrentList).containsOnly(firstTorrent, secondTorrent);

        torrentListSnapshot.getTorrentsToAdd().clear();
        torrentListSnapshot.addTorrentToDelete(secondTorrent.getHash());
        torrentList = client.getTorrentList();
        assertThat(torrentList).containsOnly(firstTorrent);

        torrentListSnapshot.getTorrentsToAdd().clear();
        torrentListSnapshot.getTorrentToRemoveHashes().clear();
        torrentListSnapshot.addTorrentToAdd(secondTorrent);
        Torrent thirdTorrent = Torrent.builder().hash(HASH_3).build();
        torrentListSnapshot.addTorrentToAdd(thirdTorrent);
        torrentListSnapshot.addTorrentToDelete(firstTorrent.getHash());

        torrentList = client.getTorrentList();
        assertThat(torrentList).containsOnly(secondTorrent, thirdTorrent);

    }

    @Test
    public void testGetTorrent() throws Exception {
        String hashFirstTorrent = HASH_1;
        Torrent firstTorrent = Torrent.builder().hash(hashFirstTorrent).build();
        String hashSecondTorrent = HASH_2;
        Torrent secondTorrent = Torrent.builder().hash(hashSecondTorrent).build();
        when(restClient.get(any(Request.class))).thenReturn(BUILD_STRING);
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
        when(restClient.get(any(Request.class))).thenReturn(TOKEN_VALUE);
        client.getTorrentFiles(ImmutableList.of());

        TorrentFileList torrentFileList = new TorrentFileList();
        torrentFileList.addFile(firstTorrent);
        torrentFileList.addFile(secondTorrent);
        torrentFileList.setHash(HASH_1);
        ArgumentCaptor<Request> requestArgumentCaptor = ArgumentCaptor.forClass(Request.class);
        when(restClient.get(requestArgumentCaptor.capture())).thenReturn(HASH_1);
        when(parser.parseAsTorrentFileList(HASH_1)).thenReturn(torrentFileList);

        TorrentFileList result = client.getTorrentFiles(ImmutableList.of(HASH_1));

        QueryParam expectedHash = new QueryParam(UTorrentWebAPIClientImpl.HASH_QUERY_PARAM_NAME, HASH_1);

        Request actualRequest = requestArgumentCaptor.getValue();
        validateRequest(Action.GET_FILES, actualRequest, ImmutableList.of(expectedHash));

        assertThat(result).isEqualTo(torrentFileList);
        verify(restClient, times(3)).get(any());
    }

    @Test
    public void testGetTorrentProperties() throws Exception {
        TorrentProperties torrentPropertiesExpected = TorrentProperties.builder().build();
        when(restClient.get(any(Request.class))).thenReturn(TOKEN_VALUE);
        when(parser.parseAsTorrentProperties(BUILD_STRING)).thenReturn(torrentPropertiesExpected);
        client.getTorrentProperties(ImmutableList.of(HASH_1));

        ArgumentCaptor<Request> requestArgumentCaptor = ArgumentCaptor.forClass(Request.class);
        when(restClient.get(requestArgumentCaptor.capture())).thenReturn(BUILD_STRING);
        TorrentProperties actualTorrentProperties = client.getTorrentProperties(ImmutableList.of(HASH_1));

        Request actualRequest = requestArgumentCaptor.getValue();
        validateRequest(Action.GET_PROP, actualRequest, ImmutableList.of(new QueryParam(UTorrentWebAPIClientImpl.HASH_QUERY_PARAM_NAME, HASH_1)));

        assertThat(actualTorrentProperties).isEqualTo(torrentPropertiesExpected);
        verify(restClient, times(3)).get(any());

    }

    @Test
    public void testStartTorrent() throws Exception {
        testSimpleTorrentAction(Action.START, client::startTorrent);
    }

    @Test
    public void testStopTorrent() throws Exception {
        testSimpleTorrentAction(Action.STOP, client::stopTorrent);
    }

    @Test
    public void testPauseTorrent() throws Exception {
        testSimpleTorrentAction(Action.PAUSE, client::pauseTorrent);
    }

    @Test
    public void testForceStartTorrent() throws Exception {
        testSimpleTorrentAction(Action.FORCE_START, client::forceStartTorrent);
    }

    @Test
    public void testUnpauseTorrent() throws Exception {
        testSimpleTorrentAction(Action.UN_PAUSE, client::unpauseTorrent);
    }

    @Test
    public void testRecheckTorrent() throws Exception {
        testSimpleTorrentAction(Action.RECHECK, client::recheckTorrent);
    }

    @Test
    public void testRemoveTorrent() throws Exception {
        testSimpleTorrentAction(Action.REMOVE, client::removeTorrent);
    }

    @Test
    public void testRemoveDataTorrent() throws Exception {
        testSimpleTorrentAction(Action.REMOVE_DATA, client::removeDataTorrent);
    }

    @Test
    public void testSetTorrentFilePriority() throws Exception {
        final Priority priority = Priority.HIGH_PRIORITY;

        when(restClient.get(any(Request.class))).thenReturn(TOKEN_VALUE);
        client.setTorrentFilePriority(HASH_1, priority, ImmutableList.of(1, 2, 3));

        ArgumentCaptor<Request> requestArgumentCaptor = ArgumentCaptor.forClass(Request.class);
        when(restClient.get(requestArgumentCaptor.capture())).thenReturn(BUILD_STRING);
        RequestResult requestResult = client.setTorrentFilePriority(HASH_1, priority, ImmutableList.of(1, 2, 3));
        assertThat(requestResult).isEqualTo(RequestResult.SUCCESS);

        ImmutableList<QueryParam> queryParams = ImmutableList.<QueryParam>builder()
                .add(new QueryParam(UTorrentWebAPIClientImpl.PRIORITY_QUERY_PARAM_NAME, String.valueOf(priority.getValue())))
                .add(new QueryParam(UTorrentWebAPIClientImpl.FILE_INDEX_QUERY_PARAM_NAME, String.valueOf(1)))
                .add(new QueryParam(UTorrentWebAPIClientImpl.FILE_INDEX_QUERY_PARAM_NAME, String.valueOf(2)))
                .add(new QueryParam(UTorrentWebAPIClientImpl.FILE_INDEX_QUERY_PARAM_NAME, String.valueOf(3)))
                .add(new QueryParam(UTorrentWebAPIClientImpl.HASH_QUERY_PARAM_NAME, HASH_1))
                .build();

        Request actualRequest = requestArgumentCaptor.getValue();

        validateRequest(Action.SET_PRIORITY, actualRequest, queryParams);

        verify(restClient, times(3)).get(any());
    }

    @Test
    public void testSetClientSetting() throws Exception {
        when(restClient.get(any(Request.class))).thenReturn(TOKEN_VALUE);
        client.setClientSetting(SettingsKey.BOSS_KEY, "value");

        ArgumentCaptor<Request> requestArgumentCaptor = ArgumentCaptor.forClass(Request.class);
        when(restClient.get(requestArgumentCaptor.capture())).thenReturn(BUILD_STRING);
        ImmutableList<QueryParam> setting = ImmutableList.of(new QueryParam(SettingsKey.BOSS_KEY.getKeyValue(), "value"));
        RequestResult requestResult = client.setClientSetting(setting);

        assertThat(requestResult).isEqualTo(RequestResult.SUCCESS);

        validateRequest(Action.SET_SETTING, requestArgumentCaptor.getValue(), setting);

        verify(restClient, times(3)).get(any());
    }

    private void testSimpleTorrentAction(Action action, Function<List<String>, RequestResult> actionMethod) {
        when(restClient.get(any(Request.class))).thenReturn(TOKEN_VALUE);
        actionMethod.apply(ImmutableList.of());

        ArgumentCaptor<Request> requestArgumentCaptor = ArgumentCaptor.forClass(Request.class);
        when(restClient.get(requestArgumentCaptor.capture())).thenReturn(BUILD_STRING);
        RequestResult requestResult = actionMethod.apply(ImmutableList.of(HASH_1));
        assertThat(requestResult).isEqualTo(RequestResult.SUCCESS);

        QueryParam expectedHash = new QueryParam(UTorrentWebAPIClientImpl.HASH_QUERY_PARAM_NAME, HASH_1);

        validateRequest(action, requestArgumentCaptor.getValue(), ImmutableList.of(expectedHash));

        verify(restClient, times(3)).get(any()); //Once to retrieve the token, twice for the actual call
    }

    private void validateRequest(Action action, Request requestToValidate, List<QueryParam> queryParams) {
        validateRequest(action, requestToValidate, queryParams, ImmutableList.of());
    }

    private void validateRequest(Action action, Request requestToValidate, List<QueryParam> queryParams, List<FilePart> fileList) {
        assertThat(requestToValidate).isNotNull();

        ImmutableList<QueryParam> queryParamsToCompare = ImmutableList.<QueryParam>builder()
                .add(new QueryParam(UTorrentWebAPIClientImpl.TOKEN_PARAM_NAME, TOKEN_VALUE))
                .add(new QueryParam(UTorrentWebAPIClientImpl.ACTION_QUERY_PARAM_NAME, action.getName()))
                .addAll(queryParams)
                .build();

        assertThat(requestToValidate.getUri()).isEqualTo(serverURI);
        assertThat(requestToValidate.getParams()).hasSameElementsAs(queryParamsToCompare);
        assertThat(requestToValidate.getFiles()).hasSameElementsAs(fileList);
    }

    public static final String TOKEN_VALUE = "token";
    public static final String HASH_1 = "hash_1";
    public static final String HASH_2 = "hash_2";
    public static final String BUILD_STRING = "build";
    public static final String HASH_3 = "hash_3";
}