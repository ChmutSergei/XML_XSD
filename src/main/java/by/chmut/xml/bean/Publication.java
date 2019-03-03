package by.chmut.xml.bean;

import java.time.LocalDate;

public class Publication {

    private String type;
    private String id;
    private String monthly;
    private String title;
    private LocalDate date;
    private Chars chars;

    public String getTitle() {
        return title;
    }

    public void setTitle(String value) {
        this.title = value;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Chars getChars() {
        return chars;
    }

    public void setChars(Chars value) {
        this.chars = value;
    }

    public String getType() {
        return type;
    }

    public void setType(String value) {
        this.type = value;
    }

    public String getId() {
        return id;
    }

    public void setId(String value) {
        this.id = value;
    }

    public String getMonthly() {
        return monthly;
    }

    public void setMonthly(String value) {
        this.monthly = value;
    }
}
