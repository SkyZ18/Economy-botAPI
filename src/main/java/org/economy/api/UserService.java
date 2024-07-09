package org.economy.api;

import org.economy.models.User;

public class UserService {

    public String createUser(User user) {

        return "Successfully created User";
    }

    public String getUserById(Long id) {

        return "";
    }

    public String getUserByName(String name) {

        return "";
    }

    public String removeUser(Long id) {

        return "Successfully removed User";
    }

}
