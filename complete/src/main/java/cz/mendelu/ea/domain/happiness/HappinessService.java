// ===============================
// HappinessService.java (Service Layer)
// ===============================

package cz.mendelu.ea.domain.happiness;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Service providing business logic for happiness operations.
 */
@Service
public class HappinessService {

    private final HappinessRepository repository;

    public HappinessService(HappinessRepository repository) {
        this.repository = repository;
    }

    /**
     * Get all happiness records.
     */
    public List<Happiness> getAll() {
        List<Happiness> data = new ArrayList<>();
        repository.findAll().forEach(data::add);
        return data;
    }

    /**
     * Get a single happiness record by ID.
     */
    public Optional<Happiness> getById(Long id) {
        return repository.findById(id);
    }

    /**
     * Create a new happiness record.
     */
    public Happiness create(Happiness happiness) {
        return repository.save(happiness);
    }

    /**
     * Update an existing happiness record.
     */
    public Happiness update(Long id, Happiness happiness) {
        happiness.setId(id);
        return repository.save(happiness);
    }

    /**
     * Delete a happiness record by ID.
     */
    public void delete(Long id) {
        repository.deleteById(id);
    }

    /**
     * Bulk-save records.
     */
    @Transactional
    public List<Happiness> saveAll(List<Happiness> happinessRecords) {
        return (List<Happiness>) repository.saveAll(happinessRecords);
    }
}
