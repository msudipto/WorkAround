package onetomany.Phones;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PhoneRepository extends JpaRepository<Phone, Long> {
    Optional<Phone> findById(int id);
}
