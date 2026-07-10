package robertoCafagna.U5W2D5test.DTO;

import jakarta.validation.constraints.Size;

public record PrenotazioneUpdateDTO(@Size(max = 500, message = "Le note non possono superare i 500 caratteri")
                                    String note) {
}
