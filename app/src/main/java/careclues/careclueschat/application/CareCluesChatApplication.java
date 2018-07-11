package careclues.careclueschat.application;

import android.app.Application;
import android.arch.persistence.room.Room;
import android.util.Log;

import com.rocketchat.common.network.ReconnectionStrategy;
import com.rocketchat.common.utils.Logger;
import com.rocketchat.common.utils.Utils;
import com.rocketchat.core.RocketChatClient;

import java.util.Date;

import careclues.careclueschat.storage.database.ChatDatabase;
import careclues.careclueschat.storage.preference.AppPreference;
import careclues.careclueschat.util.AppConstant;
import careclues.careclueschat.util.DateFormatter;
import careclues.careclueschat.util.TypefaceUtil;
import careclues.rocketchat.CcReconnectionStrategy;
import careclues.rocketchat.CcRocketChatClient;

public class CareCluesChatApplication extends Application {

    private static String serverurl = "wss://ticklechat.careclues.com/websocket";
    private static String baseUrl = "https://ticklechat.careclues.com/api/v1/";

    private ChatDatabase chatDatabase;
    private AppPreference appPreference;
    private RocketChatClient client;
    //test

    private CcRocketChatClient chatClient;


    public String token;
    public String userId;
    public String userName;
    private Logger logger;

    @Override
    public void onCreate() {
        super.onCreate();

        TypefaceUtil.overrideFont(getApplicationContext(), "DEFAULT", "fonts/opensans.ttf"); // font from assets: "assets/fonts/Roboto-Regular.ttf
        TypefaceUtil.overrideFont(getApplicationContext(), "MONOSPACE", "fonts/opensans.ttf"); // font from assets: "assets/fonts/Roboto-Regular.ttf
        TypefaceUtil.overrideFont(getApplicationContext(), "SERIF", "fonts/opensans.ttf"); // font from assets: "assets/fonts/Roboto-Regular.ttf
        TypefaceUtil.overrideFont(getApplicationContext(), "SANS_SERIF", "fonts/opensans.ttf"); // font from assets: "assets/fonts/Roboto-Regular.ttf

        appPreference = new AppPreference(this);
//        appPreference.saveStringPref(AppConstant.Preferences.LAST_ROOM_UPDATED_ON.name(), DateFormatter.format(new Date(),"yyyy-MM-dd'T'HH.mm.ss.SSS'Z'"));
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

        chatClient.setReconnectionStrategy(new CcReconnectionStrategy(20, 3000));
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
