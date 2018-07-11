package Modals;

import java.io.File;

public class Products {
    private String productID;
    private String productName;
    private String productDesc;
    private String productCat;
    private String productSize;
    private String productColor;
    private String productBrand;
    private String productPrice;
    private int productquantity;
    private File productImage;

    public String getHomedelivery() {
        return homedelivery;
    }

    public void setHomedelivery(String homedelivery) {
        this.homedelivery = homedelivery;
    }

    private String saleProductPrice;
    private String homedelivery;

    public Products(String productID, String productName, int productquantity) {
        this.productID = productID;
        this.productName = productName;
        this.productquantity = productquantity;
    }

    public Products(String saleProductID, String productID) {
        this.productID = saleProductID;
        this.saleProductPrice = productID;
    }

    public Products(String productID, String productName, String productDesc, String productCat, String productSize, String productColor, String productBrand, String productPrice, int productquantity, File productImage) {
        this.productID = productID;
        this.productName = productName;
        this.productDesc = productDesc;
        this.productCat = productCat;
        this.productSize = productSize;
        this.productColor = productColor;
        this.productBrand = productBrand;
        this.productPrice = productPrice;
        this.productquantity = productquantity;
        this.productImage = productImage;
    }

    public Products(String productID, String productName, String productDesc, String productCat, String productSize, String productColor, String productBrand, String productPrice, int productquantity) {
        this.productID = productID;
        this.productName = productName;
        this.productDesc = productDesc;
        this.productCat = productCat;
        this.productSize = productSize;
        this.productColor = productColor;
        this.productBrand = productBrand;
        this.productPrice = productPrice;
        this.productquantity = productquantity;
    }

    public Products(String productID, String productName, String productSize, String productColor, String productBrand, String productPrice, int productquantity, String homedelivery) {
        this.productID = productID;
        this.productName = productName;
        this.productSize = productSize;
        this.productColor = productColor;
        this.productBrand = productBrand;
        this.productPrice = productPrice;
        this.productquantity = productquantity;
        this.homedelivery = homedelivery;
    }

    public Products(String productID, String productName, String productDesc, String productCat, String productSize, String productColor, String productBrand, String productPrice) {
        this.productID = productID;
        this.productName = productName;
        this.productDesc = productDesc;
        this.productCat = productCat;
        this.productSize = productSize;
        this.productColor = productColor;
        this.productBrand = productBrand;
        this.productPrice = productPrice;
    }

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductDesc() {
        return productDesc;
    }

    public void setProductDesc(String productDesc) {
        this.productDesc = productDesc;
    }

    public String getProductCat() {
        return productCat;
    }

    public void setProductCat(String productCat) {
        this.productCat = productCat;
    }

    public String getProductSize() {
        return productSize;
    }

    public void setProductSize(String productSize) {
        this.productSize = productSize;
    }

    public String getProductColor() {
        return productColor;
    }

    public void setProductColor(String productColor) {
        this.productColor = productColor;
    }

    public String getProductBrand() {
        return productBrand;
    }

    public void setProductBrand(String productBrand) {
        this.productBrand = productBrand;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
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


    public String getSaleProductPrice() {
        return saleProductPrice;
    }

    public void setSaleProductPrice(String saleProductPrice) {
        this.saleProductPrice = saleProductPrice;
    }
}
