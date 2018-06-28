package careclues.rocketchat;

import java.util.ArrayList;
import java.util.List;

import careclues.rocketchat.models.CcBaseRoom;

public class CcChatRoomFactory {

    private CcRocketChatClient client;
    private List<CcChatRoom> rooms;

    public CcChatRoomFactory(CcRocketChatClient client) {
        this.client = client;
        rooms = new ArrayList<>();
    }

    private CcChatRoom createChatRoom(CcBaseRoom room) {
        return new CcChatRoom(client, room);
    }

    public CcChatRoomFactory createChatRooms(List<? extends CcBaseRoom> roomObjects) {
        removeAllChatRooms();
        for (CcBaseRoom room : roomObjects) {
            rooms.add(createChatRoom(room));
        }
        return this;
    }

    public CcChatRoomFactory addChatRoom(CcBaseRoom room) {
        if (getChatRoomByName(room.name()) == null) {
            CcChatRoom newRoom = createChatRoom(room);
            rooms.add(newRoom);
        }
        return this;
    }

    public List<CcChatRoom> getChatRooms() {
        return rooms;
    }

    public CcChatRoom getChatRoomByName(String roomName) {
        for (CcChatRoom room : rooms) {
            if (room.getRoomData().name() != null
                    && roomName.contentEquals(room.getRoomData().name())) {
                return room;
            }
        }
        return null;
    }

    public CcChatRoom getChatRoomById(String roomId) {
        for (CcChatRoom room : rooms) {
            if (roomId.contentEquals(room.getRoomData().Id)) {
                return room;
            }
        }
        return null;
    }

    public Boolean removeChatRoomByName(String roomName) {
        for (CcChatRoom room : rooms) {
            if (room.getRoomData().name() != null
                    && roomName.contentEquals(room.getRoomData().name())) {
                return rooms.remove(room);
            }
        }
        return false;
    }

    public Boolean removeChatRoomById(String roomId) {
        for (CcChatRoom room : rooms) {
            if (room.getRoomData().Id.equals(roomId)) {
                return rooms.remove(room);
            }
        }
        return false;
    }

    public Boolean removeChatRoom(CcChatRoom room) {
        return rooms.remove(room);
    }

    public void removeAllChatRooms() {
        rooms.clear();
    }
}
