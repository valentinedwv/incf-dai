package org.incf.atlas.server.central.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

@Entity
public class ProcessDescription {

	private Long id;
	private String identifier;
	private String title;
	private String _abstract;
	private List<Input> inputs;
	private List<Output> outputs;
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}

	public String getIdentifier() {
		return identifier;
	}
	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getAbstract() {
		return _abstract;
	}
	public void setAbstract(String _abstract) {
		this._abstract = _abstract;
	}
	
	@OneToMany(targetEntity = Input.class, cascade = {CascadeType.ALL})
	public List<Input> getInputs() {
		return inputs;
	}
	public void setInputs(List<Input> inputs) {
		this.inputs = inputs;
	}
	
	@OneToMany(targetEntity = Output.class, cascade = {CascadeType.ALL})
	public List<Output> getOutputs() {
		return outputs;
	}
	public void setOutputs(List<Output> outputs) {
		this.outputs = outputs;
	}

	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (obj == this) {
			return true;
		}
		if (obj.getClass() != getClass()) {
			return false;
		}
		ProcessDescription that = (ProcessDescription) obj;
		return new EqualsBuilder()
				.appendSuper(super.equals(obj))
				.append(this.identifier, that.identifier)
				.append(this.title, that.title)
				.append(this._abstract, that._abstract)
				.append(this.inputs, that.inputs)
				.append(this.outputs, that.outputs)
				.isEquals();
	}
	 
	public int hashCode() {
		return new HashCodeBuilder(17, 37)
				.appendSuper(17)
				.append(identifier)
				.append(title)
				.append(_abstract)
				.append(inputs)
				.append(outputs)
				.toHashCode();
	}

}
