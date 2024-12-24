package coms309.repository;

import com.sun.jna.platform.win32.WinDef;
import coms309.entity.Employer;
import coms309.entity.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmployerRepository extends JpaRepository<Employer, Long> {
    Optional<Employer> findByUserProfile (UserProfile userProfile);

}
