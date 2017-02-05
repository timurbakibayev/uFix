package kz.sagrad.ufix;

import java.util.ArrayList;

/**
 * Created by Timur_hnimdvi on 2/4/2017.
 */
public class WChat {
    private String id = "";
    private String groupId = ""; //STO, PARIKM, ...
    private String subject = "";
    private ArrayList<String> chatters = new ArrayList<>();
    private String ownerId = "";
    ArrayList<WMessage> messages = new ArrayList<>();
    public WChat() {

    }
    public WChat(String subject) {
        this.subject = subject;
        this.id = UFix.generateNewId();
    }

    public String getId() {
        return id;
    }

    public String getGroupId() {
        return groupId;
    }

    public String getSubject() {
        return subject;
    }

    public ArrayList<String> getChatters() {
        return chatters;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public ArrayList<WMessage> getMessages() {
        return messages;
    }
}
