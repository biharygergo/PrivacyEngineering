package broker.parsers;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Utilizer {

    @JsonProperty("id")
    private String id = "";

    @JsonProperty("name")
    private String name = "";

    @JsonProperty("address")
    private String address = "";

    @JsonProperty("phone_number")
    private String phoneNumber = "";

    String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
