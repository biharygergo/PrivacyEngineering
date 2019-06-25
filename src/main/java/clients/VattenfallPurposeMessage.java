package clients;

import java.lang.reflect.Field;

public class VattenfallPurposeMessage {
    VattenfallMessage originalMessage;
    String name;
    String address;
    String contact;

    public VattenfallMessage getOriginalMessage() {
        return originalMessage;
    }

    public void setOriginalMessage(VattenfallMessage originalMessage) {
        this.originalMessage = originalMessage;
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

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }
}
