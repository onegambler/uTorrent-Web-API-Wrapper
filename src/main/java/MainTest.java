import com.utorrent.webapiwrapper.core.UTorrentWebAPIClientImpl;
import com.utorrent.webapiwrapper.core.entities.Torrent;
import com.utorrent.webapiwrapper.restclient.ConnectionParams;
import com.utorrent.webapiwrapper.restclient.RESTClient;

import java.io.IOException;
import java.util.Collections;

public class MainTest {

    public static void main(String[] args) throws IOException {
        ConnectionParams params = ConnectionParams.builder()
                .withTimeout(5000)
                .withHost("localhost")
                .withPort(8082)
                .enableAuthentication(true)
                .withCredentials("admin", "pass")
                .create();

        RESTClient client = new RESTClient(params);
        UTorrentWebAPIClientImpl api = new UTorrentWebAPIClientImpl(client, params);
        Torrent first = api.getTorrentList().stream().findFirst().get();
        //api.addTorrent("http://torrage.com/torrent/20C6733EAA07A98B7CE2C78E79BB7697A8A4A9D5.torrent");

        //api.getTorrentList().forEach(System.out::println);
//        System.out.println(api.getTorrent(first.getHash()));
//        System.out.println(api.getTorrentFiles(Collections.singletonList(first.getHash())));
        System.out.println(api.getTorrentProperties(Collections.singletonList(first.getHash())));
        //System.out.println(api.getClientSettings());

        //api.addTorrent(new TorrentFileList("C:\\Users\\PC\\Desktop\\DF37F2F005B047CE561B121CFAF427B873AD6BEE.torrent"));
    }
}
