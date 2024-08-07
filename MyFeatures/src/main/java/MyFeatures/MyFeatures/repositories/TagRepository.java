package MyFeatures.MyFeatures.repositories;

import MyFeatures.MyFeatures.models.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import java.util.Optional;


// Repository interface for Tag entities.
@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {

  Optional<Tag> findByName(String tagName);
 }
