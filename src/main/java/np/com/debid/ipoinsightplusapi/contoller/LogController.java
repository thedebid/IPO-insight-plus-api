package np.com.debid.ipoinsightplusapi.contoller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@RestController
@RequestMapping("/api/v1/logs")
public class LogController {
    @Value("${logging.file.name}")
    private String logFilePath;

    @GetMapping
    public ResponseEntity<?> getLogs(@RequestParam(defaultValue = "200") int lines) {
        try {
            Path path = Path.of(logFilePath);
            if (!Files.exists(path)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Log file not found at: " + logFilePath);
            }

            List<String> allLines = Files.readAllLines(path);
            int size = allLines.size();
            int fromIndex = Math.max(size - lines, 0);
            List<String> recentLogs = allLines.subList(fromIndex, size);

            return ResponseEntity.ok(recentLogs);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error reading log file: " + e.getMessage());
        }
    }
}
