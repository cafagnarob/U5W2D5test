package robertoCafagna.U5W2D5test.DTO;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record ViaggioDTO(
        @NotBlank(message = "inserire una destinazione valida")
        @Size(min = 2, max = 40, message = "la destinazione deve avere un numero di caratteri compreso tra 2 e 40")
        String destinazione,
        @NotBlank(message = "inserire uno stato valido")
        String statoViaggio,
        @NotNull(message = "inserire una data valida")
        @FutureOrPresent(message = "inserire la data di oggi o una futura")
        LocalDate dataViaggio) {
}
