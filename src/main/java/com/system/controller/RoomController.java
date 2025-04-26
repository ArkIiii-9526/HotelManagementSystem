package com.system.controller;

import com.system.pojo.Room;
import com.system.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class RoomController {
    @Autowired
    private RoomService roomService;

    @GetMapping("/api/rooms")
    public Map<String, Object> getRooms(
            @RequestParam(value = "searchRoomId", defaultValue = "") String searchRoomId,
            @RequestParam(value = "currentPage", defaultValue = "1") int currentPage,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {

        Map<String, Object> result = new HashMap<>();
        try {
            long totalRecords = roomService.countRooms();
            int totalPages = (int) Math.ceil((double) totalRecords / pageSize);
            int start = (currentPage - 1) * pageSize + 1;
            int end = currentPage * pageSize;

            List<Room> list = searchRoomId.isEmpty() ?
                    roomService.selectPageRoom(start, end) :
                    roomService.selectRoomById(searchRoomId);

            result.put("code", 200);
            result.put("data", Map.of(
                    "list", list,
                    "currentPage", currentPage,
                    "totalPages", totalPages,
                    "totalRecords", totalRecords
            ));
        } catch (Exception e) {
            result.put("code", 500);
            result.put("msg", "服务器错误: " + e.getMessage());
        }
        return result;
    }

    @PutMapping("/api/editRooms/{roomId}")
    public Map<String, Object> updateRoom(@PathVariable String roomId, @RequestBody Room room) {
        Map<String, Object> result = new HashMap<>();
        try {
            if (roomService.isRoomExist(roomId)) {
                roomService.updateRoom(room);
                result.put("code", 200);
                result.put("msg", "房间信息更新成功");
            } else {
                result.put("code", 404);
                result.put("msg", "房间不存在");
            }
        } catch (Exception e) {
            result.put("code", 500);
            result.put("msg", "服务器错误: " + e.getMessage());
        }
        return result;
    }

    @PostMapping("/api/addRooms")
    public Map<String, Object> addRoom(@RequestBody Room room) {
        Map<String, Object> result = new HashMap<>();
        try {
            if (roomService.isRoomExist(room.getRoomId())) {
                result.put("code", 400);
                result.put("msg", "房间已存在");
            } else {
                roomService.insertRoom(room);
                result.put("code", 200);
                result.put("msg", "房间添加成功");
            }
        } catch (Exception e) {
            result.put("code", 500);
            result.put("msg", "服务器错误: " + e.getMessage());
        }
        return result;
    }

    @DeleteMapping("/api/rooms/{roomId}")
    public Map<String, Object> deleteRoom(@PathVariable String roomId) {
        Map<String, Object> result = new HashMap<>();
        try {
            if (roomService.isRoomExist(roomId)) {
                roomService.deleteRoom(roomId);
                result.put("code", 200);
                result.put("msg", "房间删除成功");
            } else {
                result.put("code", 404);
                result.put("msg", "房间不存在");
            }
        } catch (Exception e) {
            result.put("code", 500);
            result.put("msg", "服务器错误: " + e.getMessage());
        }
        return result;
    }
}