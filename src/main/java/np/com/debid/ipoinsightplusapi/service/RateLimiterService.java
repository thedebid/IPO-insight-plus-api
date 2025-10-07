package np.com.debid.ipoinsightplusapi.service;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class RateLimiterService {

    @Value("${ipo-insight-plus.rate-limiting.max-requests}")
    private int maxRequests;

    @Value("${ipo-insight-plus.rate-limiting.token}")
    private int token;

    @Value("${ipo-insight-plus.rate-limiting.duration-minutes}")
    private int durationMinutes;

    private final Map<String, Bucket> cache = new ConcurrentHashMap<>();

    public Bucket resolveBucket(String key) {
        return cache.computeIfAbsent(key, this::newBucket);
    }

    private Bucket newBucket(String key) {
        Bandwidth limit = Bandwidth.classic(maxRequests, Refill.greedy(token, Duration.ofMinutes(durationMinutes)));
        return Bucket.builder()
                .addLimit(limit)
                .build();
    }
}
