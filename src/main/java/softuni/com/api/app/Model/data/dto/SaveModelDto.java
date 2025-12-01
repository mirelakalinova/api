package softuni.com.api.app.Model.data.dto;

public class SaveModelDto {
	private String makeName;
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
