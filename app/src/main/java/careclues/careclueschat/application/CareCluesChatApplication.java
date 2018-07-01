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
import careclues.rocketchat.CcRocketChatClient;

public class CareCluesChatApplication extends Application {

    private static String serverurl = "wss://ticklechat.careclues.com/websocket";
    private static String baseUrl = "https://ticklechat.careclues.com/api/v1/";

    private ChatDatabase chatDatabase;
    private AppPreference appPreference;
    private RocketChatClient client;

    private CcRocketChatClient chatClient;


    public String token;
    public String userId;
    public String userName;
    private Logger logger;

    @Override
    public void onCreate() {
        super.onCreate();

        appPreference = new AppPreference(this);
//        appPreference.saveStringPref(AppConstant.Preferences.LAST_ROOM_UPDATED_ON.name(),"2018-01-01T09:12:58.633Z");

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

//        client = new RocketChatClient.Builder()
//                .websocketUrl(serverurl)
//                .restBaseUrl(baseUrl)
//                .logger(logger)
//                .build();
//        CcUtils.DOMAIN_NAME = "https://ticklechat.careclues.com";
//        client.setReconnectionStrategy(new ReconnectionStrategy(20, 3000));

        chatClient = new CcRocketChatClient.Builder()
                .websocketUrl(serverurl)
                .build();

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

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public ChatDatabase getChatDatabase() {
        return chatDatabase;
    }

    public CcRocketChatClient getRocketChatClient() {
        return chatClient;
    }
}
