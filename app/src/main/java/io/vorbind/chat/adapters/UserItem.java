package io.vorbind.chat.adapters;

public class UserItem {
    private String name;
    private String profilePictureUrl;
    private String status;

    public UserItem(String name, String profilePictureUrl, String status) {
        this.name = name;
        this.profilePictureUrl = profilePictureUrl;
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfilePictureUrl() {
        return profilePictureUrl;
    }

    public void setProfilePictureUrl(String profilePictureUrl) {
        this.profilePictureUrl = profilePictureUrl;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

