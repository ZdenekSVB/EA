package cz.mendelu.ea.domain.happiness;

import org.springframework.data.jpa.repository.JpaRepository;

public interface HappinessRepository extends JpaRepository<Happiness, Long> {
    // custom queries if needed
}