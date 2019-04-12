package assignment.data.app.assignment.model;

import java.util.ArrayList;

public class CardData {
    String type;
    String id;
    String title;
    ArrayList<String> data;  //it will store the dataMap values and will be parsed based on the type
    boolean checked = true; // parameter will update only during user interaction
    int radioId = -1;        // parameter will update only during user interaction

    public CardData(String type, String id, String title, ArrayList<String> data) {
        this.type = type;
        this.id = id;
        this.title = title;
        this.data = data;
    }

    public CardData(String type, String id, String title) {
        this.type = type;
        this.id = id;
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ArrayList<String> getData() {
        return data;
    }

    public void setData(ArrayList<String> data) {
        this.data = data;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public int getRadioId() {
        return radioId;
    }

    public void setRadioId(int radioId) {
        this.radioId = radioId;
    }

    @Override
    public String toString() {
        return "type: " + type + ", id: " + id + ", title: " + title + ", data: " + data + ",checked: " + checked + ", radioId:" + radioId;
    }
}
