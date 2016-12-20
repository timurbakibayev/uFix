package kz.sagrad.ufix;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Timur_hnimdvi on 20-Dec-16.
 */
public class OrderItem {
    public String id;
    public String content = "";
    public String details = "";
    public Date date;

    public String ownerId = "";
    public String ownerName = "";

    public  String city = "";
    public  String autoBrand = "";
    public  String year = "";
    public  String phone = "";
    public ArrayList<String> photos = new ArrayList<>();

    public String dateInText() {
        Calendar c = Calendar.getInstance();
        if (date != null)
            c.setTime(date);
        return "" + (c.get(Calendar.DAY_OF_MONTH)) + "." + (c.get(Calendar.MONTH) + 1) + "." + (c.get(Calendar.YEAR));
    }

    public String getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public String getDetails() {
        return details;
    }

    public Date getDate() {
        return date;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public String getCity() {
        return city;
    }

    public String getAutoBrand() {
        return autoBrand;
    }

    public String getYear() {
        return year;
    }

    public String getPhone() {
        return phone;
    }

    public ArrayList<String> getPhotos() {
        return photos;
    }

    public OrderItem() {
        this.id = UFix.generateNewId();
    }

    public OrderItem(String id, String content, String details) {
        this.id = id;
        this.content = content;
        this.details = details;
        Calendar c = Calendar.getInstance();
        this.date = c.getTime();

    }

    @Override
    public String toString() {
        return content;
    }
}