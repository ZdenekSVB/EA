// ===============================
// CountryRepository.java (Repository)
// ===============================

package cz.mendelu.ea.domain.country;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

/**
 * Repository for Country entities.
 * Provides standard CRUD and search by name.
 */
public interface CountryRepository extends JpaRepository<Country, Long> {
    /**
     * Find a country by its unique name.
     */
    Optional<Country> findByName(String name);
}
