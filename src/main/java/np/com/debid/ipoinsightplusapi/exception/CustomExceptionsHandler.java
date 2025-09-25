package np.com.debid.ipoinsightplusapi.exception;

import np.com.debid.ipoinsightplusapi.dto.ExceptionDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
public class CustomExceptionsHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(CustomExceptionsHandler.class);
    int httpStatusCode = HttpStatus.INTERNAL_SERVER_ERROR.value();
    String errorMessage = null;
    String apiPath = null;
    String httpMethod = null;
    Object data = null;
    Object errors = null;

    @ExceptionHandler(value = {CustomException.class})
    public ResponseEntity<Object> handleCustomException(CustomException exception, WebRequest request) {
        httpStatusCode = exception.getErrorCode();
        errorMessage = exception.getMessage();
        apiPath = ((ServletWebRequest) request).getRequest().getRequestURI();
        httpMethod = ((ServletWebRequest) request).getRequest().getMethod();
        ExceptionDTO exceptionResponseDTO = new ExceptionDTO(httpStatusCode, null, errorMessage, httpMethod, apiPath, data);
        return new ResponseEntity<>(exceptionResponseDTO, HttpStatusCode.valueOf(httpStatusCode));
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Object> handleBadCredentialsException(CustomException exception, WebRequest request) {
        httpStatusCode = exception.getErrorCode();
        errorMessage = exception.getMessage();
        apiPath = ((ServletWebRequest) request).getRequest().getRequestURI();
        httpMethod = ((ServletWebRequest) request).getRequest().getMethod();
        ExceptionDTO exceptionResponseDTO = new ExceptionDTO(httpStatusCode, null, errorMessage, httpMethod, apiPath, data);
        return new ResponseEntity<>(exceptionResponseDTO, HttpStatusCode.valueOf(httpStatusCode));
    }

    @ExceptionHandler(InsufficientAuthenticationException.class)
    public ResponseEntity<Object> handleInsufficientAuthenticationException(CustomException exception, WebRequest request) {
        httpStatusCode = exception.getErrorCode();
        errorMessage = exception.getMessage();
        apiPath = ((ServletWebRequest) request).getRequest().getRequestURI();
        httpMethod = ((ServletWebRequest) request).getRequest().getMethod();
        ExceptionDTO exceptionResponseDTO = new ExceptionDTO(httpStatusCode, null, errorMessage, httpMethod, apiPath, data);
        return new ResponseEntity<>(exceptionResponseDTO, HttpStatusCode.valueOf(httpStatusCode));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex, WebRequest request) {
        httpStatusCode = HttpStatus.BAD_REQUEST.value();
        errorMessage = "Validation failed";
        apiPath = ((ServletWebRequest) request).getRequest().getRequestURI();
        httpMethod = ((ServletWebRequest) request).getRequest().getMethod();
        errors = ex.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
                .toList();
        ExceptionDTO exceptionResponseDTO = new ExceptionDTO(httpStatusCode, null, errorMessage, httpMethod, apiPath, data, errors);
        return new ResponseEntity<>(exceptionResponseDTO, HttpStatus.BAD_REQUEST);
    }
}
