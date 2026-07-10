package robertoCafagna.U5W2D5test.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;


@Entity
@Getter
@NoArgsConstructor
@ToString
public class Prenotazione {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private LocalDate dataPrenotazione;

    @Setter
    @Column
    private String note;

    @ManyToOne
    @JoinColumn(name = "dipendente_id")
    private Dipendente dipendente;

    @ManyToOne
    @JoinColumn(name = "viaggio_id")
    private Viaggio viaggio;

    public Prenotazione(String note, Dipendente dipendente, Viaggio viaggio) {
        this.dataPrenotazione = LocalDate.now();
        this.note = note;
        this.dipendente = dipendente;
        this.viaggio = viaggio;

    }
}
