package net.javaguids.da09.service.imp;

import net.javaguids.da09.dto.GroupDto;
import net.javaguids.da09.model.UserGroup;
import net.javaguids.da09.repository.GroupRepository;
import net.javaguids.da09.repository.UserRepository;
import net.javaguids.da09.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GroupServiceImp implements GroupService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    GroupRepository groupRepository;


    GroupDto groupDto = new GroupDto();

    @Override
    public GroupDto getListGroup() {
        List<UserGroup> listGroup = groupRepository.findAll();
        groupDto.setGroupList(listGroup);
        return  groupDto;
    }

    @Override
    public GroupDto getGroupById(Long id) {
        UserGroup groupById = groupRepository.findById(id).orElseThrow(() -> new RuntimeException("not found"));
        groupDto.setGroup(groupById);
        return groupDto;
    }

    @Override
    public GroupDto deleteGroupById(Long id) {
        GroupDto groupDto = new GroupDto(); // Assuming GroupDto has a no-args constructor.

        Optional<UserGroup> groupOptional = groupRepository.findById(id);
        if (groupOptional.isPresent()) {
            try {
                // Delete all users with the foreign key id_user_group equal to the group ID
                userRepository.updateUserGroupToNull(id);

                // Delete the group itself
                groupRepository.deleteById(id);

                groupDto.setMessage("Delete success");
            } catch (Exception e) {
                groupDto.setMessage("Delete failed: " + e.getMessage());
            }
        } else {
            groupDto.setMessage("Delete failed: Group not found");
        }

        return groupDto;
    }

    @Override
    public GroupDto updateInfoGroupById(Long id, GroupDto updateGroup) {
        Optional<UserGroup> updateOptional = groupRepository.findById(id);
        if (updateOptional.isPresent()) {
            UserGroup group = updateOptional.get();
            group.setGroupName(updateGroup.getGroupName());
            group.setMembersQuantity(updateGroup.getMembersQuantity());

            UserGroup saveGroup = groupRepository.save(group);
            groupDto.setMessage("Update success");
            groupDto.setGroup(saveGroup);
        } else {
            groupDto.setMessage("Update fail");
        }
        return groupDto;
    }

    @Override
    public GroupDto createGroup(GroupDto newGroup) {
        UserGroup group = new UserGroup();
        if(groupRepository.existsGroupByGroupName(newGroup.getGroupName())){
            groupDto.setMessage("The name group already exists");
            return groupDto;
        }else {
            group.setGroupName(newGroup.getGroupName());
            UserGroup saveGroup = groupRepository.save(group);
            groupDto.setMessage("Create success");
            groupDto.setGroup(saveGroup);
        }
        return groupDto;
    }
}
