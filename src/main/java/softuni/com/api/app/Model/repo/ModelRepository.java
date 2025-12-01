package softuni.com.api.app.Model.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import softuni.com.api.app.Make.data.entity.Make;
import softuni.com.api.app.Model.data.entity.CarModel;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ModelRepository extends JpaRepository<CarModel, UUID> {
	
	CarModel findByModelIdAndMakeName(int modelId, String makeName);
	
	List<CarModel> findAllByMake_IdAndDeletedAtNullOrderByNameAsc(UUID makeId);
	
	List<CarModel> findAllByDeletedAtNull();
	
	Optional<CarModel> findByName(String modelName);
	
	List<CarModel> findAllByMake(Make make);
}
