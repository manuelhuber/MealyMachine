package manuel_huber.model;

/**
 * This will be the content of the JSON files we use as input
 */
public class Message {
    private String type;
    private String payload;

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
