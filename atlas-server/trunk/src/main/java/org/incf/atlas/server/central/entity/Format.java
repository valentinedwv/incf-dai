package org.incf.atlas.server.central.entity;

import java.util.ArrayList;
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
public class Format {
	
	private Long id;
	private List<String> mimeTypes;
	private String encoding;
	private String schema;
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}

	@OneToMany(targetEntity = MimeType.class, cascade = {CascadeType.ALL})
    public List<String> getMimeTypes() {
		if (mimeTypes == null) {
			mimeTypes = new ArrayList<String>();
		}
		return mimeTypes;
	}
	public void setMimeTypes(List<String> mimeTypes) {
		this.mimeTypes = mimeTypes;
	}
	
	public String getEncoding() {
		return encoding;
	}
	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}
	
	public String getSchema() {
		return schema;
	}
	public void setSchema(String schema) {
		this.schema = schema;
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
		Format that = (Format) obj;
		return new EqualsBuilder()
				.appendSuper(super.equals(obj))
				.append(this.mimeTypes, that.mimeTypes)
				.append(this.encoding, that.encoding)
				.append(this.schema, that.schema)
				.isEquals();
	}
	 
	public int hashCode() {
		return new HashCodeBuilder(17, 37)
				.appendSuper(17)
				.append(mimeTypes)
				.append(encoding)
				.append(schema)
				.toHashCode();
	}

}
