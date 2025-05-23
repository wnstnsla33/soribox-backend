package pProject.pPro.global;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
	// ✅ @Valid 유효성 검증 실패 처리
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<CommonResponse<Void>> handleValidationException(MethodArgumentNotValidException ex) {
		String message = ex.getBindingResult().getFieldErrors().stream().map(error -> error.getDefaultMessage())
				.findFirst().orElse("요청 값이 유효하지 않습니다.");

		log.warn("유효성 검증 실패: {}", message);

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(CommonResponse.fail(message));
	}

	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	public ResponseEntity<CommonResponse<Void>> handleMethodArgumentTypeMismatchException(
			MethodArgumentTypeMismatchException ex) {
		String message = "요청 값이 잘못되었습니다.";

		log.warn("유효성 검증 실패: {}", message);

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(CommonResponse.fail(message));
	}

	@ExceptionHandler(CustomException.class)
	public ResponseEntity<CommonResponse<Void>> handleCustomException(CustomException e) {
		String message = (e.getCustomMessage() != null) ? e.getCustomMessage() : e.getErrorCode().getMessage();
		log.info("예외 발생 :{}",e);
		return ResponseEntity.status(e.getErrorCode().getStatus()).body(CommonResponse.fail(message));
	}

}
