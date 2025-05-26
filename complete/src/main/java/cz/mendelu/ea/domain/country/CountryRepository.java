package cz.mendelu.ea.domain.country;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CountryRepository extends JpaRepository<Country, Long> {
    // custom queries if needed
}