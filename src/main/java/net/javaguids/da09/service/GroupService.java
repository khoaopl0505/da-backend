package net.javaguids.da09.service;

import net.javaguids.da09.dto.GroupDto;

public interface GroupService {
    GroupDto getListGroup();
    GroupDto getGroupById(Long id);
    GroupDto deleteGroupById(Long id);
    GroupDto updateInfoGroupById(Long id, GroupDto updateGroup);
    GroupDto createGroup(GroupDto newGroup);
}
