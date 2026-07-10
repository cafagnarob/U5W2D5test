package robertoCafagna.U5W2D5test.DTO;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record PrenotazioneDTO(
        @NotNull(message = "inserire una data valida")
        @PastOrPresent(message = "la data inserita deve essere oggi o nel passato")
        LocalDate dataPrenotazione,

        @Size(max = 500, message = "Le note non possono superare i 500 caratteri")
        String note,

        @NotNull(message = "Inserire l'id del dipendente")
        Long dipendenteId,

        @NotNull(message = "Inserire l'id del viaggio")
        Long viaggioId) {
}
