package MyFeatures.MyFeatures.repositories;


import MyFeatures.MyFeatures.models.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@EnableJpaRepositories
@Repository
public interface ProfileRepo extends JpaRepository<Profile, Integer> {
    Optional<Profile> findById(Integer profileId);
}