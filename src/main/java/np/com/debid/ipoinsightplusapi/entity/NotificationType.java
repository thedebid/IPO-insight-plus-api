package np.com.debid.ipoinsightplusapi.entity;

public enum NotificationType {
    EMAIL("email"),
    SMS("sms");

    private final String value;

    NotificationType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}

