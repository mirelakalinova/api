package softuni.com.api.app.model.service;

import softuni.com.api.app.model.data.dto.AllModelsDtoByMake;
import softuni.com.api.app.model.data.dto.ListModelDto;
import softuni.com.api.app.model.data.dto.ModelDtoByMake;

import java.util.List;
import java.util.UUID;


public interface ModelService {
	boolean isInitializeModels();
	
	ListModelDto fetchModelsData();
	void updateModelsData(ListModelDto listModelDto);
	
	List<ModelDtoByMake> getAllModelsByMakeId(UUID makeId);
	List<AllModelsDtoByMake> getAllModelsWithMakes();
	
	String saveMakeWithModel(String makeName, String modelName);
	String deleteModel(UUID id);
}
