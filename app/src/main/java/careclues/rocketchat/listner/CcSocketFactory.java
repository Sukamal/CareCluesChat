package careclues.rocketchat.listner;

import careclues.rocketchat.CcSocket;
import okhttp3.OkHttpClient;

public interface CcSocketFactory {

    CcSocket create(OkHttpClient client, String url, CcSocketListener socketListener);
}
