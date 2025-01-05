package net.javaguids.da09.controller;

import net.javaguids.da09.dto.GroupDto;
import net.javaguids.da09.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class GroupController {
    @Autowired
    GroupService groupService;

    @PostMapping("/admin/create-group")
    public ResponseEntity<GroupDto> createGroup(@RequestBody GroupDto createRequest){
        return ResponseEntity.ok(groupService.createGroup(createRequest));
    }

    @GetMapping("/admin/list-group")
    public ResponseEntity<GroupDto> getListGroup(){
        return ResponseEntity.ok(groupService.getListGroup());
    }

    @GetMapping("/admin/get-group-by-id")
    public  ResponseEntity<GroupDto> getGroupById(@RequestParam Long id){
        return  ResponseEntity.ok(groupService.getGroupById(id));
    }

    @DeleteMapping("/admin/delete-group")
    public  ResponseEntity<GroupDto> deleteGroup(@RequestParam Long id){
        return  ResponseEntity.ok(groupService.deleteGroupById(id));
    }

    @PutMapping("/admin/update-group")
    public ResponseEntity<GroupDto> updateGroup(@RequestParam Long id, @RequestBody GroupDto updateRequest){
        return ResponseEntity.ok(groupService.updateInfoGroupById(id, updateRequest));
    }
}
