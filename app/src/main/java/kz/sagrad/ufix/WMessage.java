package kz.sagrad.ufix;

/**
 * Created by Timur_hnimdvi on 2/4/2017.
 */
public class WMessage {
    private String id;
    private String text;
    private String name;
    private String photoUrl;

    public WMessage() {
    }

    public WMessage(String text, String name, String photoUrl) {
        this.text = text;
        this.name = name;
        this.photoUrl = photoUrl;
    }

    public String getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public String getName() {
        return name;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }
}
