package gub.foysal.todaysmarkerprice.Model;

public class Products {
    private String catagory,date,description,image,pname,price,prk,time,location;

    public Products() {

    }

    public Products(String catagory, String date, String description, String image, String pname, String price, String prk, String time,String location) {
        this.catagory = catagory;
        this.date = date;
        this.description = description;
        this.image = image;
        this.pname = pname;
        this.price = price;
        this.prk = prk;
        this.time = time;
        this.location=location;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getCatagory() {
        return catagory;
    }

    public void setCatagory(String catagory) {
        this.catagory = catagory;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
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
