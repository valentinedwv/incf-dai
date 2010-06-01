package org.incf.atlas.server.central.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

@Entity
public class MimeType {
	
	private Long id;
	private String mimeType;
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getMimeType() {
		return mimeType;
	}
	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
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
		MimeType that = (MimeType) obj;
		return new EqualsBuilder()
				.appendSuper(super.equals(obj))
				.append(this.mimeType, that.mimeType)
				.isEquals();
	}
	 
	public int hashCode() {
		return new HashCodeBuilder(17, 37)
				.appendSuper(17)
				.append(mimeType)
				.toHashCode();
	}

}
