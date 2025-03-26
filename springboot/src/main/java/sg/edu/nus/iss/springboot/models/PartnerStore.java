package sg.edu.nus.iss.springboot.models;

public class PartnerStore {
    
    private int partnerStoreId;
    private String name;
    private String addressLine1;
    private String addressLine2;
    private String postalCode;
    private String phone;
    private Double latitude;
    private Double longitude;
    private String website;

    
    public int getPartnerStoreId() {
        return partnerStoreId;
    }
    public void setPartnerStoreId(int partnerStoreId) {
        this.partnerStoreId = partnerStoreId;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getAddressLine1() {
        return addressLine1;
    }
    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }
    public String getAddressLine2() {
        return addressLine2;
    }
    public void setAddressLine2(String addressLine2) {
        this.addressLine2 = addressLine2;
    }
    public String getPostalCode() {
        return postalCode;
    }
    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }
    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public Double getLatitude() {
        return latitude;
    }
    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }
    public Double getLongitude() {
        return longitude;
    }
    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
    public String getWebsite() {
        return website;
    }
    public void setWebsite(String website) {
        this.website = website;
    }


    @Override
    public String toString() {
        return "PartnerStore [partnerStoreId=" + partnerStoreId + ", name=" + name + ", addressLine1=" + addressLine1
                + ", addressLine2=" + addressLine2 + ", postalCode=" + postalCode + ", phone=" + phone + ", latitude="
                + latitude + ", longitude=" + longitude + ", website=" + website + "]";
    }

}
