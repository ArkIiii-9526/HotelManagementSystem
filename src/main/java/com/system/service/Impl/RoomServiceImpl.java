package com.system.service.Impl;

import com.system.mapper.RoomMapper;
import com.system.pojo.Room;
import com.system.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomServiceImpl implements RoomService {
    @Autowired
    private RoomMapper roomMapper;

    @Override
    public List<Room> selectPageRoom(int start, int end) {
        return roomMapper.selectPageRoom(start, end);
    }

    @Override
    public int insertRoom(Room room) {
        return roomMapper.insertRoom(room);
    }

    @Override
    public int updateRoom(Room room) {
        return roomMapper.updateRoom(room);
    }

    @Override
    public int deleteRoom(String roomId) {
        return roomMapper.deleteRoom(roomId);
    }

    @Override
    public long countRooms() {
        return roomMapper.countRooms();
    }

    @Override
    public boolean isRoomExist(String roomId) {
        return roomMapper.isRoomExist(roomId) > 0;
    }

    @Override
    public List<Room> selectRoomById(String roomId) {
        return roomMapper.selectRoomById(roomId);
    }
}