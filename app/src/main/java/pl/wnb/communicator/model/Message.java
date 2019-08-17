package pl.wnb.communicator.model;


public class Message {

    private String name;
    private String content;
    private String date;
    private boolean myMsg;

    public Message(String name, String content, String date, boolean myMsg) {
        this.name = name;
        this.content = content;
        this.date = date;
        this.myMsg = myMsg;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean isMyMsg() {
        return myMsg;
    }

    public void setMyMsg(boolean myMsg) {
        this.myMsg = myMsg;
    }
}

