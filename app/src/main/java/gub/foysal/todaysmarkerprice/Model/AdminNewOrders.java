package gub.foysal.todaysmarkerprice.Model;

public class AdminNewOrders {
    private String name,phone,city,state,date,time,address,totalAmount;

    public AdminNewOrders() {

    }

    public AdminNewOrders(String name, String phone, String city, String state, String date, String time, String address, String totalAmount) {
        this.name = name;
        this.phone = phone;
        this.city = city;
        this.state = state;
        this.date = date;
        this.time = time;
        this.address = address;
        this.totalAmount = totalAmount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }
}
