package np.com.debid.ipoinsightplusapi.dto;

import lombok.Data;

@Data
public class IPODetailDTO {
    private String symbol;
    private String companyName;
    private String issueManager;
    private String openDate;
    private String closeDate;
    private double issuePrice;
    private int totalShares;
    private String moreInfoUrl;
    private String status;
}
