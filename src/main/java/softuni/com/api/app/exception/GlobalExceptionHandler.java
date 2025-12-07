package softuni.com.api.app.exception; // коригирано име


import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.time.LocalDateTime;
import java.util.HashMap;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler  {
	
	private HashMap<String, Object> createBody(HttpStatus status, String message, HttpServletRequest req) {
		HashMap<String, Object> body = new HashMap<>();
		body.put("timestamp", LocalDateTime.now());
		body.put("status", status.value());
		body.put("error", status.getReasonPhrase());
		body.put("message", message);
		body.put("path", req != null ? req.getRequestURI() : null);
		return body;
	}
	
	@ExceptionHandler(NoSuchResourceException.class)
	public ResponseEntity<HashMap<String, Object>> handleNoSuchResource(NoSuchResourceException ex, HttpServletRequest req) {
		
		log.info("Resource not found: {} {}", req.getMethod(), req.getRequestURI());
		HashMap<String, Object> body = createBody(HttpStatus.NOT_FOUND, ex.getMessage(), req);
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
	}
	
	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	public ResponseEntity<HashMap<String, Object>> handleTypeMismatch(MethodArgumentTypeMismatchException ex, HttpServletRequest req) {
		log.warn("Type mismatch: param={} requiredType={} message={}",
				ex.getName(), ex.getRequiredType(), ex.getMessage());
		HashMap<String, Object> body = createBody(HttpStatus.BAD_REQUEST, "Невалиден параметър: " + ex.getName(), req);
		return ResponseEntity.badRequest().body(body);
	}
	
	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<HashMap<String, Object>> handleIllegalArg(IllegalArgumentException ex, HttpServletRequest req) {
		log.warn("Illegal argument: {}", ex.getMessage());
		HashMap<String, Object> body = createBody(HttpStatus.BAD_REQUEST, ex.getMessage(), req);
		return ResponseEntity.badRequest().body(body);
	}
	
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<HashMap<String, Object>> handleValidation(MethodArgumentNotValidException ex, HttpServletRequest req) {
		String message = ex.getBindingResult().getFieldErrors().stream()
				.map(e -> e.getField() + ": " + e.getDefaultMessage())
				.findFirst().orElse("Validation error");
		HashMap<String, Object> body = createBody(HttpStatus.BAD_REQUEST, message, req);
		return ResponseEntity.badRequest().body(body);
	}
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<HashMap<String, Object>> handleAll(Exception ex, HttpServletRequest req) throws Exception {
		if (AnnotationUtils.findAnnotation(ex.getClass(), org.springframework.web.bind.annotation.ResponseStatus.class) != null) {
			throw ex;
		}
		log.error("Unhandled error for {} {}: {}", req.getMethod(), req.getRequestURI(), ex.getMessage(), ex);
		HashMap<String, Object> body = createBody(HttpStatus.INTERNAL_SERVER_ERROR, "Възникна грешка на сървъра.", req);
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
	}
	

	
	@ExceptionHandler(NoHandlerFoundException.class)
	public ResponseEntity<HashMap<String, Object>> handleNoHandlerFound(
			NoHandlerFoundException ex, HttpServletRequest req) {
		
		HashMap<String, Object> body =
				createBody(HttpStatus.NOT_FOUND, "Няма намерен маршрут: " + req.getRequestURI(), req);
		
		body.put("detail", ex.getMessage());
		
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
	}
	
	@ExceptionHandler(SaveFailedException.class)
	public ResponseEntity<HashMap<String,Object>> handleSaveFailed(SaveFailedException ex, HttpServletRequest req) {
		HashMap<String,Object> body = createBody(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), req);
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
	}
}
