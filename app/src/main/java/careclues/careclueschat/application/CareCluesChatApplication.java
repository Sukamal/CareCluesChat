package careclues.careclueschat.application;

import android.app.Application;
import android.arch.persistence.room.Room;

import com.rocketchat.common.network.ReconnectionStrategy;
import com.rocketchat.common.utils.Logger;
import com.rocketchat.common.utils.Utils;
import com.rocketchat.core.RocketChatClient;

import careclues.careclueschat.storage.database.ChatDatabase;
import careclues.careclueschat.storage.preference.AppPreference;
import careclues.careclueschat.util.AppConstant;

public class CareCluesChatApplication extends Application {

    private static String serverurl = "wss://ticklechat.careclues.com/websocket";
    private static String baseUrl = "https://ticklechat.careclues.com/api/v1/";

    private ChatDatabase chatDatabase;
    private AppPreference appPreference;
    private RocketChatClient client;

    public String token;
    private Logger logger;

    @Override
    public void onCreate() {
        super.onCreate();

        appPreference = new AppPreference(this);

        chatDatabase = Room.databaseBuilder(this,ChatDatabase.class, AppConstant.DATABASE_NAME).build();

        logger = new Logger() {
            @Override
            public void info(String format, Object... args) {
                System.out.println(format + " " + args);
            }

            @Override
            public void warning(String format, Object... args) {
                System.out.println(format + " " + args);
            }

            @Override
            public void debug(String format, Object... args) {
                System.out.println(format + " " + args);
            }
        };

        client = new RocketChatClient.Builder()
                .websocketUrl(serverurl)
                .restBaseUrl(baseUrl)
                .logger(logger)
                .build();
        Utils.DOMAIN_NAME = "https://ticklechat.careclues.com";
        client.setReconnectionStrategy(new ReconnectionStrategy(20, 3000));

    }

    public AppPreference getAppPreference() {
        return appPreference;
    }

    public RocketChatClient getRocketChatAPI() {
        return client;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public ChatDatabase getChatDatabase() {
        return chatDatabase;
    }
}
