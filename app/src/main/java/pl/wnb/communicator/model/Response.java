package pl.wnb.communicator.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Response {

    @Expose
    @SerializedName("date")
    private String date;

    @Expose
    @SerializedName("status")
    private Integer status;

    @Expose
    @SerializedName("contentType")
    private String contentType;

    @Expose
    @SerializedName("characterEncoding")
    private String characterEncoding;

    @Expose
    @SerializedName("msg")
    private String msg;

    @Expose
    @SerializedName("isError")
    private boolean isError;

    @Expose
    @SerializedName("username")
    private String username;

    @Expose
    @SerializedName("userRoles")
    private String userRoles;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getCharacterEncoding() {
        return characterEncoding;
    }

    public void setCharacterEncoding(String characterEncoding) {
        this.characterEncoding = characterEncoding;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public boolean isError() {
        return isError;
    }

    public void setError(boolean error) {
        isError = error;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserRoles() {
        return userRoles;
    }

    public void setUserRoles(String userRoles) {
        this.userRoles = userRoles;
    }

    @Override
    public String toString() {
        return "Response{" +
                "date='" + date + '\'' +
                ", status=" + status +
                ", contentType='" + contentType + '\'' +
                ", characterEncoding='" + characterEncoding + '\'' +
                ", msg='" + msg + '\'' +
                ", isError=" + isError +
                ", username='" + username + '\'' +
                ", userRoles='" + userRoles + '\'' +
                '}';
    }
}
