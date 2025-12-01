package softuni.com.api.app.Model.data.dto;

import softuni.com.api.app.Make.data.dto.ListMakeDto;

public class ModelDtoByMake {

		
		private Long id;
		
		private int modelId;
		
		private String name;
		
		private ListMakeDto make;
	
	public ModelDtoByMake() {
	}
	
	public Long getId() {
			return id;
		}
		
		public void setId(Long id) {
			this.id = id;
		}
		
		public int getModelId() {
			return modelId;
		}
		
		public void setModelId(int modelId) {
			this.modelId = modelId;
		}
		
		public String getName() {
			return name;
		}
		
		public void setName(String name) {
			this.name = name;
		}
		
		public ListMakeDto getMake() {
			return make;
		}
		
		public void setMake(ListMakeDto make) {
			this.make = make;
		}
	
}
