package cz.mendelu.ea.domain.country;

import cz.mendelu.ea.domain.happiness.Happiness;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

/**
 * Entity representing a country.
 * Has a unique name and a set of happiness records.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Country {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 100)
    @Column(unique = true)
    private String name;

    @Builder.Default
    @OneToMany(mappedBy = "country", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Happiness> happinessRecords = new HashSet<>();

    // 💡 Opravený konstruktor – inicializuje set
    public Country(String name) {
        this.name = name;
        this.happinessRecords = new HashSet<>();
    }

    public void addHappinessRecord(Happiness record) {
        happinessRecords.add(record);
        record.setCountry(this);
    }

    public void removeHappinessRecord(Happiness record) {
        happinessRecords.remove(record);
        record.setCountry(null);
    }
}
