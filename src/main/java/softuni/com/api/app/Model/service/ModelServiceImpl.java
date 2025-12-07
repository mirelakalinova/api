package softuni.com.api.app.model.service;

import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import softuni.com.api.app.exception.NoSuchResourceException;
import softuni.com.api.app.make.data.entity.Make;
import softuni.com.api.app.make.service.MakeService;
import softuni.com.api.app.model.data.dto.AllModelsDtoByMake;
import softuni.com.api.app.model.data.dto.ListModelDto;
import softuni.com.api.app.model.data.dto.ModelDtoByMake;
import softuni.com.api.app.model.data.dto.SaveModelDto;
import softuni.com.api.app.model.data.entity.CarModel;
import softuni.com.api.app.model.repo.ModelRepository;
import softuni.com.api.config.RestClient;

import java.time.LocalDateTime;
import java.util.*;

@Service
@Slf4j
public class ModelServiceImpl implements ModelService {
	private static final Logger log = LoggerFactory.getLogger(ModelServiceImpl.class);
	private final ModelRepository modelRepository;
	private final RestClient restClient;
	private final MakeService makeService;
	private final ModelMapper modelMapper;
	
	public ModelServiceImpl(ModelRepository modelRepository, RestClient restClient, MakeService makeService, ModelMapper modelMapper) {
		this.modelRepository = modelRepository;
		this.restClient = restClient;
		this.makeService = makeService;
		this.modelMapper = modelMapper;
	}
	
	@Override
	public boolean isInitializeModels() {
		return modelRepository.count() > 0;
	}
	
	public ListModelDto fetchModelsData() {
		log.info("Attempt to fetch all models from vpic.nhtsa.dot.gov API ");
		return restClient.getAllModels();
	}
	
	@Override
	public void updateModelsData(ListModelDto listModelDto) {
		
		log.info("Updating {} models.", listModelDto.getResults().size());
		listModelDto.getResults().forEach((m) -> {
			String makeName = m.getMakeName().toUpperCase();
			CarModel model = modelRepository.findByModelIdAndMakeName(m.getModelId(), makeName);
			if (model == null) {
				model = new CarModel();
				Make make = makeService.getMakeByName(makeName);
				model.setMake(make);
				model.setName(m.getModelName().toUpperCase());
				model.setModelId(m.getModelId());
			}
			modelRepository.saveAndFlush(model);
		});
		
		
	}
	
	@Override
	public List<ModelDtoByMake> getAllModelsByMakeId(UUID makeId) {
		log.info("Attempt to get all models..");
		
		return modelRepository.findAllByMake_IdAndDeletedAtNullOrderByNameAsc(makeId).stream()
				.map(model -> modelMapper.map(model, ModelDtoByMake.class))
				.toList();
	}
	
	@Override
	public List<AllModelsDtoByMake> getAllModelsWithMakes() {
		log.info("Attempt to get all models with make names..");
		
		List<CarModel> rawList = modelRepository.findAllByDeletedAtNull();
		List<AllModelsDtoByMake> list = new ArrayList<>();
		rawList.forEach(m ->
		{
			AllModelsDtoByMake model = modelMapper.map(m, AllModelsDtoByMake.class);
			String make = m.getMake().getName();
			model.setMake(make);
			list.add(model);
		});
		
		
		return list
				.stream()
				.sorted(Comparator.comparing(AllModelsDtoByMake::getMake)
						.thenComparing(Comparator.comparing(AllModelsDtoByMake::getName))).toList();
		
	}
	
	@Override
	public HashMap<String, String> saveMakeWithModel(SaveModelDto saveModelDto) {
		String modelName = saveModelDto.getModelName();
		String makeName = saveModelDto.getMakeName();
		log.info("Attempt to save model with name {} and make name {}", modelName, makeName);
		HashMap<String, String> result = new HashMap<>();
		
		Make make = makeService.getMakeByName(makeName);
		
		Optional<CarModel> model = modelRepository.findByNameAndMakeId(modelName, make.getId());
		if (model.isEmpty()) {
			model = Optional.of(new CarModel());
			model.get().setName(modelName.toUpperCase());
			model.get().setMake(make);
			modelRepository.save(model.get());
			result.put("message", "Успешно запазен модел: " + modelName + " към марка: " + makeName);
			result.put("status", "success");
			log.info("Successfully saved model with name {} and make name {}", modelName, makeName);
			return result;
		} else {
			log.info("Model with name {} and make name {} already exist!", modelName, makeName);
			result.put("message", "Модел: " + modelName + " и марка: " + makeName + " вече съществуват!");
			result.put("status", "error");
			return result;
		}
		
	}
	
	@Override
	public HashMap<String, String> deleteModel(UUID id) {
		HashMap<String, String> result = new HashMap<>();
		log.info("Attempt to delete model with id {} ", id);
		Optional<CarModel> model = modelRepository.findById(id);
		if (model.isEmpty()) {
			log.error("Response Status Exception in delete model method: make with id {}", id);
			throw new NoSuchResourceException("Модел с #" + id + " не съществува!");
		}
		model.get().setDeletedAt(LocalDateTime.now());
		modelRepository.save(model.get());
		log.info("Successfully deleted model with id {} ", id);
		result.put("status", "success");
		result.put("message", "Успешно изтрит модел: " + model.get().getName());
		return result;
	}
}
