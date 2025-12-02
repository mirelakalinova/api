package softuni.com.api.app.model.service;

import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import softuni.com.api.app.make.data.entity.Make;
import softuni.com.api.app.make.service.MakeService;
import softuni.com.api.app.model.data.dto.AllModelsDtoByMake;
import softuni.com.api.app.model.data.dto.ListModelDto;
import softuni.com.api.app.model.data.dto.ModelDtoByMake;
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
				model.setName(m.getModelName());
				model.setModelId(m.getModelId());
			}
			modelRepository.saveAndFlush(model);
		});
		
		
	}
	
	@Override
	public List<ModelDtoByMake> getAllModelsByMakeId(UUID makeId) {
		
		return modelRepository.findAllByMake_IdAndDeletedAtNullOrderByNameAsc(makeId).stream()
				.map(model -> modelMapper.map(model, ModelDtoByMake.class))
				.toList();
	}
	
	@Override
	public List<AllModelsDtoByMake> getAllModelsWithMakes() {
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
	public String saveMakeWithModel(String makeName, String modelName) {
		StringBuilder sb = new StringBuilder();
		Make make = makeService.getMakeByName(makeName);
		Optional<CarModel> model = modelRepository.findByName(modelName);
		if (model.isEmpty()) {
			model = Optional.of(new CarModel());
			model.get().setMake(make);
			modelRepository.save(model.get());
			sb.append("Успешно запазен модел: ").append(modelName).append(" към марка: ").append(makeName);
			return sb.toString();
		}
		if (model.get().getMake().getName().equals(makeName)) {
			return "Модел: " + modelName + " и марка " + makeName + " вече съществуват!";
		}
		
		CarModel newModel = new CarModel();
		newModel.setMake(make);
		newModel.setName(modelName);
		modelRepository.save(newModel);
		sb.append("Успешно запазен модел: ").append(modelName).append(" към марка: ").append(makeName);
		return sb.toString();
	}
	
	@Override
	public String deleteModel(UUID id) {
		Optional<CarModel> model = modelRepository.findById(id);
		if (model.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Модел с #" + id + " не съществува!");
		}
		model.get().setDeletedAt(LocalDateTime.now());
		modelRepository.save(model.get());
		return "Успешно изтрит модел: " + model.get().getName();
	}
}
