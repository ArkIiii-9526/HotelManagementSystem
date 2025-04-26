package com.system.mapper;

import com.system.pojo.Room;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface RoomMapper {
    List<Room> selectPageRoom(@Param("start") int start, @Param("end") int end);
    int insertRoom(Room room);
    int updateRoom(Room room);
    int deleteRoom(String roomId);
    long countRooms();
    int isRoomExist(String roomId);
    List<Room> selectRoomById(String roomId);
}