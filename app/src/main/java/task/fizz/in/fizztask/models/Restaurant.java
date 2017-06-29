package task.fizz.in.fizztask.models;

import java.io.Serializable;
import java.util.List;



public class Restaurant implements Serializable {

    //variable names are same as API response to enable auto-serialization by gson
    private String address;
    private String area;
    private boolean awardedLoyalty;
    private float cashbackPercentage;
    private String city;
    private String contactNumber;
    private double distanceFromUser;
    private String heroImage;
    private String id;
    private boolean isActive;
    private boolean isDiscounted;
    private boolean isFavorite;
    private String logo;
    private String name;
    private String offerPercentage;
    private String specialOffer;
    private double latitude;
    private double longitude;
    private List<List<String>> timings;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public boolean isAwardedLoyalty() {
        return awardedLoyalty;
    }

    public void setAwardedLoyalty(boolean awardedLoyalty) {
        this.awardedLoyalty = awardedLoyalty;
    }

    public float getCashbackPercentage() {
        return cashbackPercentage;
    }

    public void setCashbackPercentage(float cashbackPercentage) {
        this.cashbackPercentage = cashbackPercentage;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public double getDistanceFromUser() {
        return distanceFromUser;
    }

    public void setDistanceFromUser(double distanceFromUser) {
        this.distanceFromUser = distanceFromUser;
    }

    public String getHeroImage() {
        return heroImage;
    }

    public void setHeroImage(String heroImage) {
        this.heroImage = heroImage;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOfferPercentage() {
        return offerPercentage;
    }

    public void setOfferPercentage(String offerPercentage) {
        this.offerPercentage = offerPercentage;
    }

    public String getSpecialOffer() {
        return specialOffer;
    }

    public void setSpecialOffer(String specialOffer) {
        this.specialOffer = specialOffer;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public boolean isDiscounted() {
        return isDiscounted;
    }

    public void setDiscounted(boolean discounted) {
        isDiscounted = discounted;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public List<List<String>> getTimings() {
        return timings;
    }

    public void setTimings(List<List<String>> timings) {
        this.timings = timings;
    }
}
