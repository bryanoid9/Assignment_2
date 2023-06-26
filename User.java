package Twitter;

import java.util.ArrayList;
import java.util.List;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class User {
    private String id;
    private List<User> followers;
    private List<User> following;
    private List<String> newsFeed;
    private PropertyChangeSupport pcs;

    public User(String id) {
        this.id = id;
        this.followers = new ArrayList<>();
        this.following = new ArrayList<>();
        this.newsFeed = new ArrayList<>();
        this.pcs = new PropertyChangeSupport(this);
    }

    public String getID() {
        return this.id;
    }

    public void follow(User user) {
        if (!following.contains(user)) {
            following.add(user);
            user.addFollower(this);
        }
    }

    public void addFollower(User user) {
        if (!followers.contains(user)) {
            followers.add(user);
        }
    }

    public void postTweet(String tweet) {
        newsFeed.add(tweet);
        pcs.firePropertyChange("postTweet", null, tweet);
        updateFollowers(tweet);
    }

    public void updateNewsFeed(String tweet) {
        newsFeed.add(tweet);
        pcs.firePropertyChange("updateNewsFeed", null, tweet);
    }
    // Update the news feed of the followers
    private void updateFollowers(String tweet) {
        for (User user : followers) {
            user.updateNewsFeed(tweet);
        }
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(listener);
    }

    public List<String> getNewsFeed() {
        return new ArrayList<>(newsFeed);
    }

    @Override
    public String toString() {
        return id;
    }
}
