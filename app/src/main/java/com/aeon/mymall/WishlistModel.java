package com.aeon.mymall;

public class WishlistModel {

    private String productId;
    private String productImage;
    private String productTitle;
    private long freeCoupans;
    private String rating;
    private long totalRatings;
    private String productPrice;
    private String cuttedPrice;
    private boolean cod;

    public WishlistModel(String productId, String productImage, String productTitle, long freeCoupans, String rating, long totalRatings, String productPrice, String cuttedPrice, boolean cod) {
        this.productId = productId;
        this.productImage = productImage;
        this.productTitle = productTitle;
        this.freeCoupans = freeCoupans;
        this.rating = rating;
        this.totalRatings = totalRatings;
        this.productPrice = productPrice;
        this.cuttedPrice = cuttedPrice;
        this.cod = cod;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public String getProductTitle() {
        return productTitle;
    }

    public void setProductTitle(String productTitle) {
        this.productTitle = productTitle;
    }

    public long getFreeCoupans() {
        return freeCoupans;
    }

    public void setFreeCoupans(long freeCoupans) {
        this.freeCoupans = freeCoupans;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public long getTotalRatings() {
        return totalRatings;
    }

    public void setTotalRatings(long totalRatings) {
        this.totalRatings = totalRatings;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public String getCuttedPrice() {
        return cuttedPrice;
    }

    public void setCuttedPrice(String cuttedPrice) {
        this.cuttedPrice = cuttedPrice;
    }

    public boolean isCod() {
        return cod;
    }

    public void setCod(boolean cod) {
        this.cod = cod;
    }
}
