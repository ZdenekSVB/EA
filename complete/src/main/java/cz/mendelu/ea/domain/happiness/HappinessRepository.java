// ===============================
// HappinessRepository.java (Repository)
// ===============================

package cz.mendelu.ea.domain.happiness;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository for Happiness entities.
 * Standard CRUD methods available.
 */
public interface HappinessRepository extends JpaRepository<Happiness, Long> {
    // Add custom queries if needed (e.g. findByCountryIdAndYear)
}
