package softuni.com.api.app.model.data.dto;

import jakarta.validation.constraints.NotBlank;

public class SaveModelDto {
	@NotBlank(message = "Името na марката не трябва да е празно!")
	private String makeName;
	@NotBlank(message = "Името на модела не трябва да е празно!")
	private String modelName;
	
	public SaveModelDto() {
	}
	
	public String getMakeName() {
		return makeName;
	}
	
	public void setMakeName(String makeName) {
		this.makeName = makeName;
	}
	
	public String getModelName() {
		return modelName;
	}
	
	public void setModelName(String modelName) {
		this.modelName = modelName;
	}
}
