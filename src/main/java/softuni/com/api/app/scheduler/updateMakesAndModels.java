package softuni.com.api.app.scheduler;


import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import softuni.com.api.app.make.service.MakeService;
import softuni.com.api.app.model.service.ModelService;

@Component
@Slf4j
@Profile("!test")
public class updateMakesAndModels {
	
	
	private final MakeService makeService;
	private final ModelService modelService;
	
	public updateMakesAndModels(MakeService makeService, ModelService modelService) {
		this.makeService = makeService;
		this.modelService = modelService;
	}
	
	
	@Scheduled(fixedDelayString = "${app.scheduling.update.delay}")
	public void runUpdateJob() {
		
		
		log.info("Starting update all makes job ..");
		makeService.updateMakesData(makeService.fetchMakesData());
		log.info("Starting update all models job ..");
		modelService.updateModelsData(modelService.fetchModelsData());
		
		log.info("Successfully updated makes and models!");
		
	}
	
	
}
