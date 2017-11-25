package cn.summerwaves.model;

public class AccessToken {
    private long ID;


    private String accessToken;
    private long acquireTime;

    public long getID() {
        return ID;
    }

    public void setID(long ID) {
        this.ID = ID;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public long getAcquireTime() {
        return acquireTime;
    }

    public void setAcquireTime(long acquireTime) {
        this.acquireTime = acquireTime;
    }
}
