package careclues.rocketchat.listner;

import careclues.rocketchat.Socket;
import okhttp3.OkHttpClient;

public interface SocketFactory {

    Socket create(OkHttpClient client, String url, SocketListener socketListener);
}
