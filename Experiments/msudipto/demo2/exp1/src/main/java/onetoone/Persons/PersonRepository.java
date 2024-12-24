
package onetoone.Persons;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for Person entity that extends JpaRepository.
 * 
 * Enhancements:
 * - Added @Repository annotation for clarity.
 * - Improved method names to follow standard naming conventions.
 * 
 * Author: Vivek Bengre
 */
@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {

    // Find person by their ID
    Optional<Person> findById(int id);

    // Delete person by their ID
    void deleteById(int id);

    // Find person by their associated laptop ID
    Person findByLaptop_Id(int id);
}
