package softuni.com.api.app.make.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import softuni.com.api.app.make.data.entity.Make;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface MakeRepository extends JpaRepository<Make, UUID> {
	Optional<Make> findByMakeId(int makeId);
	
	List<Make> findAllByDeletedAtNull();
	
	Optional<Make> findByName(String make);
}
