package softuni.com.api.app.Model.service;

import softuni.com.api.app.Model.data.dto.AllModelsDtoByMake;
import softuni.com.api.app.Model.data.dto.ListModelDto;
import softuni.com.api.app.Model.data.dto.ModelDtoByMake;

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
