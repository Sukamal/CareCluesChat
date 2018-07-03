package careclues.careclueschat.feature.chat;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;

import com.rocketchat.common.RocketChatApiException;
import com.rocketchat.common.RocketChatException;
import com.rocketchat.common.data.lightdb.collection.Collection;
import com.rocketchat.common.data.lightdb.document.UserDocument;
import com.rocketchat.common.data.model.BaseRoom;
import com.rocketchat.common.listener.ConnectListener;
import com.rocketchat.common.listener.TypingListener;
import com.rocketchat.common.network.Socket;
import com.rocketchat.core.ChatRoom;
import com.rocketchat.core.RocketChatClient;
import com.rocketchat.core.callback.AccountListener;
import com.rocketchat.core.callback.EmojiListener;
import com.rocketchat.core.callback.GetSubscriptionListener;
import com.rocketchat.core.callback.HistoryCallback;
import com.rocketchat.core.callback.LoginCallback;
import com.rocketchat.core.callback.MessageCallback;
import com.rocketchat.core.callback.UserListener;
import com.rocketchat.core.model.Emoji;
import com.rocketchat.core.model.Permission;
import com.rocketchat.core.model.PublicSetting;
import com.rocketchat.core.model.Subscription;
import com.rocketchat.core.model.Token;
import com.stfalcon.chatkit.messages.MessageInput;
import com.stfalcon.chatkit.messages.MessagesList;
import com.stfalcon.chatkit.messages.MessagesListAdapter;
import com.stfalcon.chatkit.utils.DateFormatter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import careclues.careclueschat.R;
import careclues.careclueschat.application.CareCluesChatApplication;
import careclues.careclueschat.feature.chat.chatmodel.Message;
import careclues.careclueschat.feature.chat.chatmodel.User;
import careclues.careclueschat.feature.common.BaseActivity;
import careclues.careclueschat.network.RestApiExecuter;
import careclues.careclueschat.util.AppUtil;

public class TestChatACtivity extends AppCompatActivity implements

        ConnectListener,
        AccountListener.getPermissionsListener,
        AccountListener.getPublicSettingsListener,
        EmojiListener,
        GetSubscriptionListener,
        UserListener.getUserRoleListener,
        MessageCallback.SubscriptionCallback,
        TypingListener,
        MessagesListAdapter.SelectionListener,
        MessagesListAdapter.OnLoadMoreListener,
        MessageInput.InputListener,
        DateFormatter.Formatter,
        MessageInput.AttachmentsListener {


    @BindView(R.id.messagesList)
    MessagesList messagesList;

    @BindView(R.id.input)
    MessageInput input;

    RocketChatClient api;
    ChatRoom chatRoom;
    String userId;
    private int selectionCount;

    private static final int TOTAL_MESSAGES_COUNT = 1000;

    public MessagesListAdapter<Message> messagesAdapter;

    Handler Typinghandler = new Handler();
    Boolean typing = false;
    String roomId;

    private Date lastTimestamp;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        roomId = getIntent().getStringExtra("roomId");
        userId = RestApiExecuter.getInstance().getAuthToken().getUserId();
        api = ((CareCluesChatApplication) getApplicationContext()).getRocketChatAPI();

        //------- Connect with soccket if not done previously
        // ------if connect to socket here then call init socket inside onLoginSuccess otherwise call it in onCreate
//        api.setReconnectionStrategy(null);
//        api.connect(this);

        initSocketApis();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testchat);
        ButterKnife.bind(this);

        afterViewsSet();

    }

    private void initSocketApis(){
        api.getWebsocketImpl().getConnectivityManager().register(TestChatACtivity.this);
        List<BaseRoom> rooms = new ArrayList<>();
        BaseRoom  baseRoom;
        baseRoom  = new BaseRoom() {
            @Override
            public String roomId() {
                return roomId;
            }

            @Nullable
            @Override
            public RoomType type() {
                return RoomType.PRIVATE;
            }

            @Nullable
            @Override
            public com.rocketchat.common.data.model.User user() {
                return new com.rocketchat.common.data.model.User() {
                    @Nullable
                    @Override
                    public String id() {
                        return "2eRbrjSZnsACYkRx4";
                    }

                    @Nullable
                    @Override
                    public String username() {
                        return "sachu-985";
                    }

                    @Nullable
                    @Override
                    public List<String> roles() {
                        return null;
                    }
                };
            }

            @Nullable
            @Override
            public String name() {
                return null;
            }
        };
        rooms.add(baseRoom);
        api.getChatRoomFactory().createChatRooms(rooms);
        if (getCurrentUser() !=null) {
            updateUserStatus(getCurrentUser().status().toString());
        }
        api.getDbManager().getUserCollection().register(userId, new Collection.Observer<UserDocument>() {
            @Override
            public void onUpdate(Collection.Type type, UserDocument document) {
                switch (type) {
                    case ADDED:
                        updateUserStatus(document.status().toString());
                        break;
                    case CHANGED:
                        updateUserStatus(document.status().toString());
                        break;
                    case REMOVED:
                        updateUserStatus("UNAVAILABLE");
                        break;
                }
            }
        });

        chatRoom = api.getChatRoomFactory().getChatRoomById(roomId);
        chatRoom.subscribeRoomMessageEvent(null, TestChatACtivity.this);
        chatRoom.subscribeRoomTypingEvent(null, TestChatACtivity.this);
        chatRoom.getChatHistory(50, lastTimestamp, null, new HistoryCallback() {
            @Override
            public void onLoadHistory(List<com.rocketchat.core.model.Message> list, int unreadNotLoaded) {
                TestChatACtivity.this.onLoadHistory(list, unreadNotLoaded);
            }

            @Override
            public void onError(RocketChatException error) {

            }
        });
    }

    @Override
    public void onConnect(String sessionID) {
//        String token = ((CareCluesChatApplication)getApplicationContext()).getToken();
        String token = RestApiExecuter.getInstance().getAuthToken().getToken();

        if (api.getWebsocketImpl().getSocket().getState() == Socket.State.CONNECTED) {
            api.loginUsingToken(token, new LoginCallback() {
                @Override
                public void onLoginSuccess(Token token) {


                    chatRoom = api.getChatRoomFactory().getChatRoomById(roomId);
                    chatRoom.subscribeRoomMessageEvent(null, TestChatACtivity.this);
                    chatRoom.subscribeRoomTypingEvent(null, TestChatACtivity.this);
                    chatRoom.getChatHistory(50, lastTimestamp, null, new HistoryCallback() {
                        @Override
                        public void onLoadHistory(List<com.rocketchat.core.model.Message> list, int unreadNotLoaded) {
                            TestChatACtivity.this.onLoadHistory(list, unreadNotLoaded);
                        }

                        @Override
                        public void onError(RocketChatException error) {

                        }
                    });

                    afterViewsSet();
                }

                @Override
                public void onError(RocketChatException error) {
                    System.out.println("Connection Error : " + error.toString());
                }
            });
            showConnectedSnackbar();
        }else{
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(chatRoom != null)
                        chatRoom.unSubscribeAllEvents();
                    AppUtil.getSnackbarWithAction(findViewById(R.id.chat_activity), R.string.connection_error)
                            .setAction("RETRY", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    api.getWebsocketImpl().getSocket().reconnect();

                                }
                            })
                            .show();
                }
            });
        }


    }

    @Override
    public void onDisconnect(boolean closedByServer) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(chatRoom != null)
                    chatRoom.unSubscribeAllEvents();
                AppUtil.getSnackbarWithAction(findViewById(R.id.chat_activity), R.string.disconnected_from_server)
                        .setAction("RETRY", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                api.getWebsocketImpl().getSocket().reconnect();
                            }
                        })
                        .show();
            }
        });

    }

    @Override
    public void onConnectError(Throwable websocketException) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(chatRoom != null)
                    chatRoom.unSubscribeAllEvents();
                AppUtil.getSnackbarWithAction(findViewById(R.id.chat_activity), R.string.connection_error)
                        .setAction("RETRY", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                api.getWebsocketImpl().getSocket().reconnect();

                            }
                        })
                        .show();
            }
        });

    }

    @Override
    public void onTyping(final String roomId, final String user, final Boolean istyping) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (istyping) {
                    getSupportActionBar().setSubtitle(user + " is typing...");
                } else {
                    if (getCurrentUser() != null) {
                        updateUserStatus(getCurrentUser().status().toString());
                    } else {
                        getSupportActionBar().setSubtitle("");
                    }
                }
            }
        });

    }

    void updateUserStatus(final String status) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                getSupportActionBar().setSubtitle(status.substring(0,1)+status.substring(1).toLowerCase());

            }
        });
    }

    @Override
    protected void onDestroy() {
        api.getWebsocketImpl().getConnectivityManager().unRegister(this);
        super.onDestroy();
    }

    @Override
    public void onGetPermissions(List<Permission> permissions, RocketChatApiException error) {

    }

    @Override
    public void onGetPublicSettings(List<PublicSetting> settings, RocketChatApiException error) {

    }

    @Override
    public void onListCustomEmoji(List<Emoji> emojis, RocketChatApiException error) {

    }

    @Override
    public void onGetSubscriptions(List<Subscription> subscriptions, RocketChatApiException error) {

    }


    @Override
    public void onAddAttachments() {

    }

    @Override
    public boolean onSubmit(CharSequence input) {
        chatRoom.sendMessage(input.toString(), new MessageCallback.MessageAckCallback() {
            @Override
            public void onMessageAck(com.rocketchat.core.model.Message message) {

            }

            @Override
            public void onError(RocketChatException error) {

            }
        });
        return true;
    }

    @Override
    public void onLoadMore(int page, int totalItemsCount) {
        if (totalItemsCount < TOTAL_MESSAGES_COUNT) {
            chatRoom.getChatHistory(50, lastTimestamp, null, new HistoryCallback() {
                @Override
                public void onLoadHistory(List<com.rocketchat.core.model.Message> list, int unreadNotLoaded) {
                    TestChatACtivity.this.onLoadHistory(list, unreadNotLoaded);
                }

                @Override
                public void onError(RocketChatException error) {

                }
            });
        }
    }

    @Override
    public void onSelectionChanged(int count) {
        this.selectionCount = count;
    }

    @Override
    public String format(Date date) {
        if (DateFormatter.isToday(date)) {
            return "Today";
        } else if (DateFormatter.isYesterday(date)) {
            return "Yesterday";
        } else {
            return DateFormatter.format(date, DateFormatter.Template.STRING_DAY_MONTH_YEAR);
        }
    }

    UserDocument getCurrentUser() {
        return api.getDbManager().getUserCollection().get(userId);
    }

    @Override
    public void onBackPressed() {
        if (selectionCount == 0) {
            if(chatRoom != null)
                chatRoom.unSubscribeAllEvents();
            super.onBackPressed();
        } else {
            messagesAdapter.unselectAllItems();
        }
    }



    void afterViewsSet() {
        input.setInputListener(this);
        input.setAttachmentsListener(this);
        input.getInputEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!typing) {
                    typing = true;
                    chatRoom.sendIsTyping(true);
                }
                Typinghandler.removeCallbacks(onTypingTimeout);
                Typinghandler.postDelayed(onTypingTimeout, 600);
            }

            Runnable onTypingTimeout = new Runnable() {
                @Override
                public void run() {
                    typing = false;
                    chatRoom.sendIsTyping(false);
                }
            };

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        initAdapter();
    }

    private void initAdapter() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //        messagesAdapter = new MessagesListAdapter<>(RestApiExecuter.getInstance().getAuthToken().getUserId(), null);
                messagesAdapter = new MessagesListAdapter<>(api.getWebsocketImpl().getMyUserId(), null);
                messagesAdapter.enableSelectionMode(TestChatACtivity.this);
                messagesAdapter.setLoadMoreListener(TestChatACtivity.this);
                messagesAdapter.setDateHeadersFormatter(TestChatACtivity.this);
                messagesList.setAdapter(messagesAdapter);

            }
        });

    }


    private MessagesListAdapter.Formatter<Message> getMessageStringFormatter() {
        return new MessagesListAdapter.Formatter<Message>() {
            @Override
            public String format(Message message) {
                String createdAt = new SimpleDateFormat("MMM d, EEE 'at' h:mm a", Locale.getDefault())
                        .format(message.getCreatedAt());

                String text = message.getText();
                if (text == null) text = "[attachment]";

                return String.format(Locale.getDefault(), "%s: %s (%s)",
                        message.getUser().getName(), text, createdAt);
            }
        };
    }

    void updateMessage(final ArrayList<Message> messages) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                messagesAdapter.addToEnd(messages, false);

            }
        });
    }

    public void onLoadHistory(final List<com.rocketchat.core.model.Message> list, final int unreadNotLoaded) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (list.size() > 0) {
                    lastTimestamp = new Date(list.get(list.size() - 1).timestamp());
                    final ArrayList<Message> messages = new ArrayList<>();
                    for (com.rocketchat.core.model.Message message : list) {
                        switch (message.getMsgType()) {
                            case TEXT:
                                messages.add(new Message(message.id(), new User(message.sender().id(), message.sender().username(), null, true), message.message(), new Date(message.timestamp())));
                                break;
                        }
                    }
                    updateMessage(messages);
                }
            }
        });

    }


    @Override
    public void onMessage(final String roomId, final com.rocketchat.core.model.Message message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                messagesAdapter.addToStart(new Message(message.id(), new User(message.sender().id(), message.sender().username(), null, true), message.message(), new Date(message.timestamp())), true);

            }
        });

    }


    void showConnectedSnackbar() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Snackbar
                        .make(findViewById(R.id.chat_activity), R.string.connected, Snackbar.LENGTH_LONG)
                        .show();
            }
        });

    }

    @Override
    public void onUserRoles(List<com.rocketchat.common.data.model.User> users, RocketChatApiException error) {

    }
}
