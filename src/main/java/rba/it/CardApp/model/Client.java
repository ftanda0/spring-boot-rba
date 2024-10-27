package rba.it.CardApp.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Entity
@Table(name = "clients")
@Data // Generira gettere, settere, toString, equals i hashCode metode
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @NotBlank(message = "First name is required") // Validacija: obavezno
    private String firstName;

    @Column(nullable = false)
    @NotBlank(message = "Last name is required") // Validacija: obavezno
    private String lastName;

    @Column(unique = true, nullable = false)
    @Size(min = 11, max = 11, message = "OIB must be exactly 11 digits") // Validacija: 11 znamenki
    private String oib;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CardStatus status; // Enum za status kartice

    // No-argument constructor required by JPA
    public Client() {
    }

    // Parameterized constructor for convenience
    public Client(String firstName, String lastName, String oib, CardStatus status) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.oib = oib;
        this.status = status;
    }
}
