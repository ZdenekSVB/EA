package cz.mendelu.ea.domain.country;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface CountryRepository extends JpaRepository<Country, Long> {
    Optional<Country> findByName(String name);
}
