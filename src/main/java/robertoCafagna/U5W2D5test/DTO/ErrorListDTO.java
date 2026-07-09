package robertoCafagna.U5W2D5test.DTO;

import java.time.LocalDateTime;
import java.util.List;

public record ErrorListDTO(String message,
                           LocalDateTime timestamp, List<String> errorList) {
}
