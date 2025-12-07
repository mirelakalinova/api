package softuni.com.api.app.make.service;

import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.com.api.app.exception.NoSuchResourceException;
import softuni.com.api.app.make.data.dto.ListMakeDto;
import softuni.com.api.app.make.data.dto.ListMakesDto;
import softuni.com.api.app.make.data.entity.Make;
import softuni.com.api.app.make.repo.MakeRepository;
import softuni.com.api.app.model.data.entity.CarModel;
import softuni.com.api.app.model.repo.ModelRepository;
import softuni.com.api.config.RestClient;

import java.time.LocalDateTime;
import java.util.*;

@Service
@Slf4j
public class MakeServiceImpl implements MakeService {
	private final MakeRepository makeRepository;
	private final ModelRepository modelRepository;
	private final RestClient restClient;
	private final ModelMapper modelMapper;
	
	public MakeServiceImpl(MakeRepository makeRepository, ModelRepository modelRepository, RestClient restClient, ModelMapper modelMapper) {
		this.makeRepository = makeRepository;
		this.modelRepository = modelRepository;
		this.restClient = restClient;
		this.modelMapper = modelMapper;
	}
	
	@Override
	public boolean isInitializedMakes() {
		
		return makeRepository.count() > 0;
	}
	
	@Override
	public ListMakesDto fetchMakesData() {
		log.info("Attempt to fetch all makes from vpic.nhtsa.dot.gov API ");
		return restClient.getAllMakes();
	}
	
	public void updateMakesData(ListMakesDto makesListDto) {
		
		log.info("Updating {} makes.", makesListDto.getResults().size());
		makesListDto.getResults().forEach((m) -> {
			boolean makeExist = findByMakeId(m.getMakeId());
			if (!makeExist) {
				Make make = new Make();
				make.setMakeId(m.getMakeId());
				make.setName(m.getMakeName().toUpperCase());
				makeRepository.saveAndFlush(make);
			}
		});
		
	}
	
	@Override
	public boolean findByMakeId(int makeId) {
		StackTraceElement caller = Thread.currentThread().getStackTrace()[2];
		
		if (!caller.getMethodName().contains("updateMakesData")) {
			
			log.info("Attempt to find make with id {} ", makeId);
		}
		Optional<Make> make = makeRepository.findByMakeId(makeId);
		return make.isPresent();
	}
	
	@Override
	public List<ListMakeDto> getAllMakes() {
		log.info("Attempt to get all makes..");
		return makeRepository.findAllByDeletedAtNull().stream()
				.sorted(Comparator.comparing(Make::getName))
				.map(make -> modelMapper.map(make, ListMakeDto.class)).toList();
	}
	
	public Make getMakeByName(String makeName) {
		log.info("Attempt to find make with name {} ", makeName);
		Optional<Make> make = this.findByName(makeName);
		if (make.isPresent()) {
			log.info("Successfully found make with name {} in the database ", makeName);
			return make.get();
		}
		
		make = Optional.of(new Make());
		make.get().setName(makeName);
		return this.saveMake(make.get());
	}
	
	public Optional<Make> findByName(String make) {
		return makeRepository.findByName(make);
	}
	
	
	private Make saveMake(Make make) {
		log.info("Attempt to save make with name {} ", make.getName());
		make.setName(make.getName().toUpperCase());
		Make makeToReturn = makeRepository.saveAndFlush(make);
		
		log.info("Successfully saved make with name {} ", make.getName());
		
		return makeToReturn;
		
	}
	
	@Override
	public HashMap<String, String> deleteMake(UUID id) {
		HashMap<String, String> result = new HashMap<>();
		
		log.info("Attempt to delete make with id {} ", id);
		Optional<Make> make = makeRepository.findById(id);
		if (make.isEmpty()) {
			log.error("Response Status Exception in deleteMake method: make with id {}", id);
			throw new NoSuchResourceException("Макра с #" + id + " не съществува!");
		}
		log.info("Attempt to find all models of make to delete with id {} ", id);
		List<CarModel> models = modelRepository.findAllByMake(make.get());
		models.forEach(model -> {
			log.info("Attempt to set deleted at filed in models of make to delete with id {} ", id);
			model.setDeletedAt(LocalDateTime.now());
			modelRepository.save(model);
		});
		make.get().setDeletedAt(LocalDateTime.now());
		makeRepository.save(make.get());
		log.info("Successfully deleted make with id {} ", id);
		result.put("status", "success");
		result.put("message", "Успешно изтрита марка: " + make.get().getName());
		return result;
	}
}
