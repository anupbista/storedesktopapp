package Modals;

import java.io.File;

public class Products {
    private String productID;
    private String productname;
    private String productdesc;
    private String productcategory;
    private String productsize;
    private String productcolor;
    private String productbrand;
    private String productprice;
    private int productquantity;
    private File productImage;

    public Products(String productID, String productname, String productdesc, String productcategory, String productsize, String productcolor, String productbrand, String productprice, int productquantity, File productImage) {
        this.productID = productID;
        this.productname = productname;
        this.productdesc = productdesc;
        this.productcategory = productcategory;
        this.productsize = productsize;
        this.productcolor = productcolor;
        this.productbrand = productbrand;
        this.productprice = productprice;
        this.productquantity = productquantity;
        this.productImage = productImage;
    }

    public Products(String productID, String productname, String productcategory, String productsize, String productcolor, String productbrand, String productprice, int productquantity) {
        this.productID = productID;
        this.productname = productname;
        this.productcategory = productcategory;
        this.productsize = productsize;
        this.productcolor = productcolor;
        this.productbrand = productbrand;
        this.productprice = productprice;
        this.productquantity = productquantity;
    }

    public Products(String productID, String productname, String productdesc, String productcategory, String productsize, String productcolor, String productbrand, String productprice, int productquantity) {
        this.productID = productID;
        this.productname = productname;
        this.productdesc = productdesc;
        this.productcategory = productcategory;
        this.productsize = productsize;
        this.productcolor = productcolor;
        this.productbrand = productbrand;
        this.productprice = productprice;
        this.productquantity = productquantity;
    }

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public String getProductname() {
        return productname;
    }

    public void setProductname(String productname) {
        this.productname = productname;
    }

    public String getProductdesc() {
        return productdesc;
    }

    public void setProductdesc(String productdesc) {
        this.productdesc = productdesc;
    }

    public String getProductcategory() {
        return productcategory;
    }

    public void setProductcategory(String productcategory) {
        this.productcategory = productcategory;
    }

    public String getProductsize() {
        return productsize;
    }

    public void setProductsize(String productsize) {
        this.productsize = productsize;
    }

    public String getProductcolor() {
        return productcolor;
    }

    public void setProductcolor(String productcolor) {
        this.productcolor = productcolor;
    }

    public String getProductbrand() {
        return productbrand;
    }

    public void setProductbrand(String productbrand) {
        this.productbrand = productbrand;
    }

    public String getProductprice() {
        return productprice;
    }

    public void setProductprice(String productprice) {
        this.productprice = productprice;
    }

    public int getProductquantity() {
        return productquantity;
    }

    public void setProductquantity(int productquantity) {
        this.productquantity = productquantity;
    }

    public File getProductImage() {
        return productImage;
    }

    public void setProductImage(File productImage) {
        this.productImage = productImage;
    }
}
