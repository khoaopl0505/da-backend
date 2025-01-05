package net.javaguids.da09.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import net.javaguids.da09.model.UserGroup;
import net.javaguids.da09.model.User;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GroupDto {
    Long id;
    String groupName;
    private User user;
    private Integer membersQuantity;
    UserGroup group;
    List<UserGroup> groupList;
    String message;
}
