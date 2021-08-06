package gub.foysal.todaysmarkerprice.Model;

public class Comments {
    public String comment, location, date, prk, time;

    public Comments() {

    }

    public Comments(String comment, String location, String date, String prk, String time) {
        this.comment = comment;
        this.location = location;
        this.date = date;
        this.prk = prk;
        this.time = time;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPrk() {
        return prk;
    }

    public void setPrk(String prk) {
        this.prk = prk;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}