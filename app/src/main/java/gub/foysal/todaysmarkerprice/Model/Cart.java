package gub.foysal.todaysmarkerprice.Model;

public class Cart {
    private String prk,pname,price,quentity,discount;

    public Cart() {
    }

    public Cart(String prk, String pname, String price, String quentity, String discount) {
        this.prk = prk;
        this.pname = pname;
        this.price = price;
        this.quentity = quentity;
        this.discount = discount;
    }

    public String getPrk() {
        return prk;
    }

    public void setPrk(String prk) {
        this.prk = prk;
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

    public String getQuentity() {
        return quentity;
    }

    public void setQuentity(String quantity) {
        this.quentity = quantity;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }
}
