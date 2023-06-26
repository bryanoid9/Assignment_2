package Twitter;

import java.util.ArrayList;
import java.util.List;

public class UserGroup {
    private String id;
    private List<User> users;
    private List<UserGroup> userGroups;

    public UserGroup(String id) {
        this.id = id;
        this.users = new ArrayList<>();
        this.userGroups = new ArrayList<>();
    }
    // Add a user to the user group
    public void addUser(User user) {
        this.users.add(user);
    }
    //Add a user group to a user group
    public void addGroup(UserGroup userGroup) {
        this.userGroups.add(userGroup);
    }
    //Get ID of users
    public String getId() {
        return id;
    }
    // Get the list of users in the user group
    public List<User> getUsers() {
        return users;
    }

    public List<UserGroup> getUserGroups() {
        return userGroups;
    }

    @Override
    public String toString() {
        return id;
    }
}
