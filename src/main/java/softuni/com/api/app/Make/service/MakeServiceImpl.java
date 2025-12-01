package softuni.com.api.app.Make.service;

import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import softuni.com.api.app.Make.data.dto.ListMakeDto;
import softuni.com.api.app.Make.data.dto.ListMakesDto;
import softuni.com.api.app.Make.data.entity.Make;
import softuni.com.api.app.Make.repo.MakeRepository;
import softuni.com.api.app.Model.data.entity.CarModel;
import softuni.com.api.app.Model.repo.ModelRepository;
import softuni.com.api.config.RestClient;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class MakeServiceImpl implements MakeService {
	private static final Logger log = LoggerFactory.getLogger(MakeServiceImpl.class);
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
		return restClient.getAllMakes();
	}
	
	public void updateMakesData(ListMakesDto makesListDto) {
		
		log.info("Updating {} makes.", makesListDto.getResults().size());
		makesListDto.getResults().forEach((m) -> {
			boolean makeExist = findByMakeId(m.getMakeId());
			if (!makeExist) {
				Make make = new Make();
				make.setMakeId(m.getMakeId());
				make.setName(m.getMakeName());
				makeRepository.saveAndFlush(make);
			}
		});
		
	}
	
	@Override
	public boolean findByMakeId(int makeId) {
		Optional<Make> make = makeRepository.findByMakeId(makeId);
		return make.isPresent();
	}
	
	@Override
	public List<ListMakeDto> getAllMakes() {
		return makeRepository.findAllByDeletedAtNull().stream()
				.sorted(Comparator.comparing(Make::getName))
				.map(make -> modelMapper.map(make, ListMakeDto.class)).toList();
	}
	
	public Make getMakeByName(String makeName) {
		Optional<Make> make = this.findByName(makeName);
		if (make.isPresent()) {
			return make.get();
		}
		make = Optional.of(new Make());
		make.get().setName(makeName);
		return this.saveMake(make.get());
	}
	
	private Optional<Make> findByName(String make) {
		return makeRepository.findByName(make);
	}
	

	private Make saveMake(Make make) {
		makeRepository.save(make);
		Optional<Make> makeToReturn = makeRepository.findByName(make.getName());
		if (makeToReturn.isPresent()) {
			return makeToReturn.get();
		}
		throw new RuntimeException("Нещо се обърка!");
	}
	
	@Override
	public String deleteMake(UUID id) {
		Optional<Make> make = makeRepository.findById(id);
		if(make.isEmpty()){
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Макра с #" + id + " не съществува!");
		}
		List<CarModel> models = modelRepository.findAllByMake(make.get());
		models.forEach(model-> {
			model.setDeletedAt(LocalDateTime.now());
			modelRepository.save(model);
		});
		make.get().setDeletedAt(LocalDateTime.now());
		makeRepository.save(make.get());
		return "Успешно изтрита марка: " + make.get().getName();
	}
}
