package fiap.hydrata.exception;

import java.util.List;

public record ValidationErrorResponse(int status, String message, List<FieldErrorDetail> errors) {
}
