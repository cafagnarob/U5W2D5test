package robertoCafagna.U5W2D5test.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record StatoViaggioDTO(@NotBlank(message = "inserire uno stato di viaggio valido")
                              @Pattern(regexp = "IN_PROGRAMMA|COMPLETATO",
                                      message = "stato non valido, valori ammessi: IN_PROGRAMMA, COMPLETATO")
                              String statoViaggio) {
}
