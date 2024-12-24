package onetomany.Laptops;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * 
 * @author Vivek Bengre
 * 
 */ 

public interface LaptopRepository extends JpaRepository<Laptop, Long> {
    Optional<Laptop> findById(int id);
    void deleteById(int id);
}
