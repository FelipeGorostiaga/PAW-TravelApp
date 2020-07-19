package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.model.User;

import java.util.ArrayList;
import java.util.List;

public class UserListDTO {


    List<UserDTO> users;


    public UserListDTO() {

    }

    public UserListDTO(List<User> userList) {
        this.users = new ArrayList<>();
        userList.forEach(t -> this.users.add(new UserDTO(t)));
    }

}
