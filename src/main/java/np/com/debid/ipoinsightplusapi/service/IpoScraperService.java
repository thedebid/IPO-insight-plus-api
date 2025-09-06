package np.com.debid.ipoinsightplusapi.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import np.com.debid.ipoinsightplusapi.dto.IPODetailDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;


@Service
public class IpoScraperService {

    @Value("${ipo-insight-plus.api.url}")
    private String API_URL;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public List<IPODetailDTO> getCurrentIpos() throws IOException, InterruptedException {
        List<IPODetailDTO> ipos = new ArrayList<>();
        String apiUrl = API_URL + "?draw=1"
                + "&columns[0][data]=DT_Row_Index"
                + "&columns[0][name]="
                // Include ALL parameters from the browser request
                + "&type=1"
                + "&start=0"
                + "&length=20"
                + "&search[value]="
                + "&search[regex]=false"
                + "&_=" + System.currentTimeMillis(); // Timestamp

        // Create request with proper headers
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl))
                .header("Accept", "application/json")
                .header("User-Agent", "Mozilla/5.0")
                .header("X-Requested-With", "XMLHttpRequest") // Important!
                .header("Referer", "https://www.sharesansar.com/upcoming-ipo")
                .build();

        // Send request
        HttpResponse<String> response = HttpClient.newHttpClient()
                .send(request, HttpResponse.BodyHandlers.ofString());

        // Parse JSON
        JsonNode root = new ObjectMapper().readTree(response.body());
        JsonNode data = root.path("data");

        // Parse JSON response
        JsonNode rootNode = objectMapper.readTree(response.body());
        JsonNode dataNode = rootNode.path("data");

        if (dataNode.isArray()) {
            for (JsonNode ipoNode : dataNode) {
                IPODetailDTO ipo = new IPODetailDTO();

                // Extract company info
                JsonNode companyNode = ipoNode.path("company");
                ipo.setSymbol(cleanHtml(companyNode.path("symbol").asText()));
                ipo.setCompanyName(cleanHtml(companyNode.path("companyname").asText()));

                // Extract IPO details
                ipo.setTotalShares((int) parseDouble(ipoNode.path("total_units").asText()));
                ipo.setIssuePrice(parseDouble(ipoNode.path("issue_price").asText()));
                ipo.setOpenDate(ipoNode.path("opening_date").asText());
                ipo.setCloseDate(ipoNode.path("closing_date").asText());
//                ipo.setLastClosingDate(ipoNode.path("final_date").asText());
//                ipo.setListingDate(ipoNode.path("listing_date").asText());
                ipo.setIssueManager(ipoNode.path("issue_manager").asText());

                // Determine status based on status code
                int statusCode = ipoNode.path("status").asInt();
                ipo.setStatus(getStatusText(statusCode));

                // Extract links
//                ipo.setAnnouncementLink(ipoNode.path("announcement_link").asText());
//                ipo.setCompanyUrl(extractHref(companyNode.path("symbol").asText()));

                ipos.add(ipo);
            }


        }
        return ipos;
    }

    // Helper methods
    private double parseDouble(String value) {
        try {
            return Double.parseDouble(value.replace(",", ""));
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }

    private String cleanHtml(String html) {
        // Remove HTML tags from strings like "<a href='...'>SWSLBSL</a>"
        return html.replaceAll("<[^>]*>", "");
    }

    private String extractHref(String html) {
        // Extract URL from href attribute
        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("href='(.*?)'");
        java.util.regex.Matcher matcher = pattern.matcher(html);
        return matcher.find() ? matcher.group(1) : "";
    }

    private String getStatusText(int statusCode) {
        // Map status codes to text
        return switch (statusCode) {
            case -2 -> "Coming Soon";
            case -1 -> "Open Soon";
            case 0 -> "Open";
            case 1 -> "Closed";
            default -> "Unknown";
        };
    }
}