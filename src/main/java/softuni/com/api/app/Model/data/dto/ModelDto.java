package softuni.com.api.app.Model.data.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ModelDto {

	@JsonProperty("Make_ID")
	private int makeId;
	@JsonProperty("Make_Name")
	private String makeName;
	@JsonProperty("Model_ID")
	private int modelId;
	@JsonProperty("Model_Name")
	private String modelName;
	
	public ModelDto() {
	}

	public int getMakeId() {
		return makeId;
	}
	
	public void setMakeId(int makeId) {
		this.makeId = makeId;
	}
	
	public String getMakeName() {
		return makeName;
	}
	
	public void setMakeName(String makeName) {
		this.makeName = makeName;
	}
	
	public int getModelId() {
		return modelId;
	}
	
	public void setModelId(int modelId) {
		this.modelId = modelId;
	}
	
	public String getModelName() {
		return modelName;
	}
	
	public void setModelName(String modelName) {
		this.modelName = modelName;
	}
}
