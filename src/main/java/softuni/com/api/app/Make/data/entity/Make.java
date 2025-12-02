package softuni.com.api.app.make.data.entity;

import jakarta.persistence.*;
import softuni.com.api.app.model.data.entity.CarModel;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "makes")
public class Make {
	
	@Id
	@GeneratedValue
	private UUID id;
	@Column(name = "make_id")
	private int makeId;
	@Column(unique = true)
	private String name;
	@OneToMany(mappedBy = "make")
	private List<CarModel> models;
	@Column(name = "deleted_at")
	private LocalDateTime deletedAt;
	
	public Make() {
		this.models = new ArrayList<>();
	}
	
	public Make(int makeId, String name) {
		this.models = new ArrayList<>();
		this.makeId = makeId;
		this.name = name;
	}
	
	public UUID getId() {
		return id;
	}
	
	public void setId(UUID id) {
		this.id = id;
	}
	
	
	public int getMakeId() {
		return makeId;
	}
	
	public void setMakeId(int makeId) {
		this.makeId = makeId;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public List<CarModel> getModels() {
		return models;
	}
	
	public void setModels(List<CarModel> models) {
		this.models = models;
	}
	
	public LocalDateTime getDeletedAt() {
		return deletedAt;
	}
	
	public void setDeletedAt(LocalDateTime deletedAt) {
		this.deletedAt = deletedAt;
	}
}
