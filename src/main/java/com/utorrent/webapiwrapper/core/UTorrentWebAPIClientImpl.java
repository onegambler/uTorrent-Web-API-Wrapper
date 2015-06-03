package com.utorrent.webapiwrapper.core;

import com.utorrent.webapiwrapper.core.entities.*;
import com.utorrent.webapiwrapper.restclient.ConnectionParams;
import com.utorrent.webapiwrapper.restclient.RESTClient;
import com.utorrent.webapiwrapper.restclient.Request;
import com.utorrent.webapiwrapper.restclient.exceptions.BadRequestException;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

import static com.utorrent.webapiwrapper.core.Action.*;
import static com.utorrent.webapiwrapper.core.entities.RequestResult.FAIL;
import static com.utorrent.webapiwrapper.core.entities.RequestResult.SUCCESS;
import static java.util.Objects.nonNull;
import static java.util.Objects.requireNonNull;

public class UTorrentWebAPIClientImpl implements UTorrentWebAPIClient {

    private static final String ACTION_QUERY_PARAM_NAME = "action";
    private static final String TOKEN_PARAM_NAME = "token";
    private static final String URL_PARAM_NAME = "s";
    private static final String LIST_QUERY_PARAM_NAME = "list";
    private static final String CACHE_ID_QUERY_PARAM = "cid";
    private static final String HASH_QUERY_PARAM_NAME = "hash";
    private static final String FILE_INDEX_QUERY_PARAM_NAME = "f";
    private static final String PRIORITY_QUERY_PARAM_NAME = "p";
    private static final String APPLICATION_X_BIT_TORRENT_CONTENT_TYPE = "application/x-bittorrent";
    private static final String TORRENT_FILE_PART_NAME = "torrent_file";

    private final RESTClient client;
    private final TorrentsCache torrentsCache;
    private final URI serverURI;

    private final MessageParser messageParser = new MessageParser();

    private String token;


    public UTorrentWebAPIClientImpl(RESTClient client, ConnectionParams connectionParams) {
        this.client = client;
        URIBuilder uriBuilder = new URIBuilder()
                .setScheme(connectionParams.getScheme())
                .setHost(connectionParams.getHost())
                .setPort(connectionParams.getPort())
                .setPath("/gui/");
        this.serverURI = getURI(uriBuilder);
        torrentsCache = new TorrentsCache();
    }

    private String getAuthenticationToken() {
        if (token == null) {
            synchronized (this) {
                if (token == null) {
                    token = client.get(serverURI.resolve("token.html"));
                    if (nonNull(token)) {
                        token = token.replaceAll("<[^>]*>", "");
                    } else {
                        throw new NullPointerException("Token received is null");
                    }
                }
            }
        }

        return token;
    }

    private <T> T invokeWithAuthentication(Supplier<T> responseSupplier, boolean retryIfAuthFailed) {

        try {
            T response = responseSupplier.get();
            requireNonNull(response, String.format("Received null response from server, request %s", responseSupplier));
            return response;
        } catch (BadRequestException e) {
            setTokenAsExpired();
            if (retryIfAuthFailed) {
                return invokeWithAuthentication(responseSupplier, false);
            } else {
                throw new UTorrentAuthException("Impossible to connect to uTorrents, wrong username or password");
            }
        }
    }

    @Override
    public RequestResult addTorrent(String url) {
        List<Request.QueryParam> params = new ArrayList<>();
        params.add(new Request.QueryParam(URL_PARAM_NAME, url));
        String result = executeAction(ADD_URL, Collections.emptyList(), params);
        return getResult(result);
    }

    @Override
    public RequestResult addTorrent(File torrentFile) {
        URIBuilder uriBuilder = new URIBuilder(serverURI)
                .addParameter(ACTION_QUERY_PARAM_NAME, ADD_FILE.getName())
                .addParameter(TOKEN_PARAM_NAME, getAuthenticationToken());
        Request request = Request.builder()
                .addFile(TORRENT_FILE_PART_NAME, torrentFile, ContentType.create(APPLICATION_X_BIT_TORRENT_CONTENT_TYPE))
                .setDestination(getURI(uriBuilder)).create();
        String stringResult = invokeWithAuthentication(() -> client.post(request), true);
        return getResult(stringResult);
    }

    @Override
    public Set<Torrent> getTorrentList() {
        updateTorrentCache();
        return torrentsCache.getTorrentList();
    }

    private void updateTorrentCache() {
        URIBuilder requestBuilder = new URIBuilder(serverURI)
                .addParameter(LIST_QUERY_PARAM_NAME, "1")
                .addParameter(TOKEN_PARAM_NAME, getAuthenticationToken());

        if (nonNull(torrentsCache.getCachedID())) {
            requestBuilder.addParameter(CACHE_ID_QUERY_PARAM, torrentsCache.getCachedID());
        }

        String jsonTorrentSnapshotMessage = invokeWithAuthentication(() -> client.get(getURI(requestBuilder)), true);
        torrentsCache.updateCache(messageParser.parseAsTorrentListSnapshot(jsonTorrentSnapshotMessage));
    }

    @Override
    public Torrent getTorrent(String torrentHash) {
        updateTorrentCache();
        return torrentsCache.getTorrent(torrentHash);
    }

    @Override
    public TorrentFileList getTorrentFiles(List<String> torrentHashes) {
        String torrentFilesJsonMessage = executeAction(GET_FILES, torrentHashes, Collections.emptyList());
        return messageParser.parseAsTorrentFileList(torrentFilesJsonMessage);
    }

    @Override
    public TorrentProperties getTorrentProperties(List<String> torrentHash) {
        String jsonTorrentPropertiesMessage = executeAction(GET_PROP, Collections.unmodifiableList(torrentHash), Collections.emptyList());
        return messageParser.parseAsTorrentProperties(jsonTorrentPropertiesMessage);
    }

    @Override
    public RequestResult startTorrent(List<String> hash) {
        return executeBaseTorrentAction(START, hash);
    }

    @Override
    public RequestResult stopTorrent(List<String> hash) {
        return executeBaseTorrentAction(STOP, hash);
    }

    @Override
    public RequestResult pauseTorrent(List<String> hash) {
        return executeBaseTorrentAction(PAUSE, hash);
    }

    @Override
    public RequestResult forceStartTorrent(List<String> hash) {
        return executeBaseTorrentAction(FORCE_START, hash);
    }

    @Override
    public RequestResult unpauseTorrent(List<String> hash) {
        return executeBaseTorrentAction(UN_PAUSE, hash);
    }

    @Override
    public RequestResult recheckTorrent(List<String> hash) {
        return executeBaseTorrentAction(RECHECK, hash);
    }

    @Override
    public RequestResult removeTorrent(List<String> hash) {
        return executeBaseTorrentAction(REMOVE, hash);
    }

    @Override
    public RequestResult removeDataTorrent(List<String> hash) {
        return executeBaseTorrentAction(REMOVE_DATA, hash);
    }

    @Override
    public RequestResult setTorrentFilePriority(String hash, Priority priority,
                                                List<Integer> fileIndices) {
        List<Request.QueryParam> params = new ArrayList<>();
        params.add(new Request.QueryParam(PRIORITY_QUERY_PARAM_NAME, String.valueOf(priority.getValue())));
        fileIndices.forEach(index -> params.add(new Request.QueryParam(FILE_INDEX_QUERY_PARAM_NAME, String.valueOf(index))));
        return getResult(executeAction(SET_PRIORITY, Collections.singletonList(hash), params));
    }

    @Override
    public RequestResult setClientSetting(SettingsKey settingKey, String settingValue) {
        return setClientSetting(settingKey.getKeyValue(), settingValue);
    }

    @Override
    public RequestResult setClientSetting(String settingName, String settingValue) {
        return setClientSetting(Collections.singletonList(new Request.QueryParam(settingName, settingValue)));
    }

    @Override
    public RequestResult setClientSetting(List<Request.QueryParam> settings) {
        return getResult(executeAction(SET_SETTING, Collections.emptyList(), settings));
    }

    @Override
    public ClientSettings getClientSettings() {
        String returnedValue = executeAction(GET_SETTINGS);
        return messageParser.parseAsClientSettings(returnedValue);
    }

    private RequestResult executeBaseTorrentAction(Action action, List<String> hashes) {
        return getResult(executeAction(action, hashes, Collections.emptyList()));
    }

    private String executeAction(Action action) {
        return executeAction(action, Collections.emptyList(), Collections.emptyList());
    }

    private String executeAction(Action action, List<String> torrentHashes, List<Request.QueryParam> queryParams) {
        URIBuilder requestBuilder = new URIBuilder(serverURI)
                .addParameter(ACTION_QUERY_PARAM_NAME, action.getName());
        queryParams.forEach(param -> requestBuilder.addParameter(param.getName(), param.getValue()));
        torrentHashes.forEach(hash -> requestBuilder.addParameter(HASH_QUERY_PARAM_NAME, hash));
        requestBuilder.addParameter(TOKEN_PARAM_NAME, getAuthenticationToken());
        return invokeWithAuthentication(() -> client.get(getURI(requestBuilder)), true);
    }

    private void setTokenAsExpired() {
        token = null;
    }

    @Override
    public void close() throws IOException {
        this.client.close();
    }

    private URI getURI(URIBuilder requestBuilder) {
        try {
            return requestBuilder.build();
        } catch (URISyntaxException e) {
            throw new RuntimeException("URISyntaxException occurred!", e);
        }
    }

    private RequestResult getResult(String result) {
        return nonNull(result) && result.contains("build") ? SUCCESS : FAIL;
    }
}
