package np.com.debid.ipoinsightplusapi.entity;

public enum ShareIssueType {
    LOCAL_IPO("local"),
    GENERAL_IPO("general"),
    RIGHT_SHARE("right");

    private final String value;

    ShareIssueType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
