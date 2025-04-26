package com.system.service;

import com.system.pojo.Room;
import java.util.List;

public interface RoomService {
    List<Room> selectPageRoom(int start, int end);
    int insertRoom(Room room);
    int updateRoom(Room room);
    int deleteRoom(String roomId);
    long countRooms();
    boolean isRoomExist(String roomId);
    List<Room> selectRoomById(String roomId);
}