package np.com.debid.ipoinsightplusapi.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ResponseWrapper<T> {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private int statusCode;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String message;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T messages;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T data;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private int errorCode;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String apiPath;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String httpMethod;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private LocalDateTime timestamp;

    public ResponseWrapper() {
    }

    public ResponseWrapper(int statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }

    public ResponseWrapper(int statusCode, T body) {
        this.statusCode = statusCode;
        this.data = body;
        this.timestamp = LocalDateTime.now();
    }


    public ResponseWrapper(int statusCode, String message, T data) {
        this.statusCode = statusCode;
        this.message = message;
        this.data = data;
        this.timestamp = LocalDateTime.now();
    }


    public ResponseWrapper(int statusCode, String message, T data, int errorCode) {
        this.statusCode = statusCode;
        this.message = message;
        this.data = data;
        this.errorCode = errorCode;
        this.timestamp = LocalDateTime.now();
    }

    public ResponseWrapper(int statusCode, String message, T data, int errorCode, String apiPath, String httpMethod) {
        this.statusCode = statusCode;
        this.message = message;
        this.data = data;
        this.errorCode = errorCode;
        this.apiPath = apiPath;
        this.httpMethod = httpMethod;
        this.timestamp = LocalDateTime.now();
    }

    public ResponseWrapper(int statusCode, String message, T data, String apiPath, String httpMethod) {
        this.statusCode = statusCode;
        this.message = message;
        this.data = data;
        this.apiPath = apiPath;
        this.httpMethod = httpMethod;
        this.timestamp = LocalDateTime.now();
    }
}
