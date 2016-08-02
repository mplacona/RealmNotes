package uk.co.placona.realmnotes;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import io.realm.RealmModel;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;

@RealmClass
public class Note implements RealmModel {
    @PrimaryKey
    private String id = UUID.randomUUID().toString();;
    private String text;
    private Date date = new Date(java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime()));

    public Note(){ }

    public Note(String text) { this.text = text; }

    public void setText(String text) { this.text = text; }
    public String getText() { return text;}
    public void setId(String id) { this.id = id; }
    public String getId() { return id; }
    public Date getDate() { return date; }
    public void setDate(Date date) { this.date = date; }
}