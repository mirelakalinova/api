package softuni.com.api.app.init;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import softuni.com.api.app.Make.service.MakeService;
import softuni.com.api.app.Model.service.ModelService;

@Component
public class MakesAndModelsInit implements CommandLineRunner {
	public final MakeService makeService;
	public final ModelService modelService;
	
	public MakesAndModelsInit(MakeService makeService, ModelService modelService) {
		this.makeService = makeService;
		this.modelService = modelService;
	}
	
	@Override
	public void run(String... args) throws Exception {
		if (!makeService.isInitializedMakes()) {
		makeService.updateMakesData(makeService.fetchMakesData());
		}
		if (!modelService.isInitializeModels()) {
			modelService.updateModelsData(modelService.fetchModelsData());
		}
	}
}
