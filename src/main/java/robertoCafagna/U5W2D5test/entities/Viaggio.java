package robertoCafagna.U5W2D5test.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import robertoCafagna.U5W2D5test.Enum.StatoViaggio;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor
@ToString
public class Viaggio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Setter
    @Column(nullable = false)
    private String destinazione;

    @Setter
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatoViaggio statoViaggio;

    @Setter
    @Column(nullable = false)
    private LocalDate dataViaggio;

    public Viaggio(String destinazione,
                   LocalDate dataViaggio) {
        this.destinazione = destinazione;
        this.statoViaggio = StatoViaggio.IN_PROGRAMMA;
        this.dataViaggio = dataViaggio;

    }
}
