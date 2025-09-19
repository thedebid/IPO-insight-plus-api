package np.com.debid.ipoinsightplusapi.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class IPOInsightUtility {
    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static LocalDate getTodaysDate() {
        return LocalDate.now();
    }
}
