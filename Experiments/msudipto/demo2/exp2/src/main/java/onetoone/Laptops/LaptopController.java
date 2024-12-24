
package onetoone.Laptops;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class LaptopController {

    @Autowired
    private LaptopRepository laptopRepository;

    @GetMapping("/laptops")
    public List<Laptop> getAllLaptops() {
        return laptopRepository.findAll();
    }

    @GetMapping("/laptops/{id}")
    public Laptop getLaptopById(@PathVariable int id) {
        return laptopRepository.findById(id).orElseThrow(() -> new RuntimeException("Laptop not found for id: " + id));
    }

    @PostMapping("/laptops")
    public Laptop createLaptop(@RequestBody Laptop laptop) {
        // Add validation for laptop details here
        if (laptop.getCpuClock() <= 0 || laptop.getRam() <= 0) {
            throw new IllegalArgumentException("Invalid laptop specifications: CPU clock and RAM must be positive.");
        }
        return laptopRepository.save(laptop);
    }

    @PutMapping("/laptops/{id}")
    public Laptop updateLaptop(@PathVariable int id, @RequestBody Laptop laptopDetails) {
        Laptop laptop = laptopRepository.findById(id).orElseThrow(() -> new RuntimeException("Laptop not found for id: " + id));

        laptop.setCpuClock(laptopDetails.getCpuClock());
        laptop.setCpuCores(laptopDetails.getCpuCores());
        laptop.setRam(laptopDetails.getRam());
        laptop.setManufacturer(laptopDetails.getManufacturer());
        laptop.setCost(laptopDetails.getCost());

        return laptopRepository.save(laptop);
    }

    @DeleteMapping("/laptops/{id}")
    public String deleteLaptop(@PathVariable int id) {
        laptopRepository.deleteById(id);
        return "Laptop deleted successfully.";
    }
}
