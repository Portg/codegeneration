package org.jeecg.learning.chapter6.core.entity;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.envers.AuditOverride;
import org.hibernate.envers.AuditOverrides;
import org.jeecg.learning.chapter6.core.annotation.MetaData;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Access(AccessType.FIELD)
@JsonInclude(Include.NON_EMPTY)
@MappedSuperclass
@AuditOverrides({ @AuditOverride(forClass = IdEntity.class) })
public abstract class IdEntity extends BaseEntity<String> {

	private static final long serialVersionUID = -2193817581425923048L;

	@MetaData("主键")
	@Id
	@GeneratedValue(generator = "idGenerator")
	@GenericGenerator(name = "idGenerator", strategy = "uuid")
	@Column(name ="ID",nullable=false,length=32)
	@JsonProperty
	private String id;

}
