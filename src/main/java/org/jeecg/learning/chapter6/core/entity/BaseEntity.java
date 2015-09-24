package org.jeecg.learning.chapter6.core.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.Version;

import org.hibernate.envers.AuditOverride;
import org.hibernate.envers.AuditOverrides;
import org.jeecg.learning.chapter6.core.audit.DefaultAuditable;
import org.jeecg.learning.chapter6.core.audit.SaveUpdateAuditListener;
import org.jeecg.learning.chapter6.core.web.json.DateTimeJsonSerializer;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonInclude(Include.NON_EMPTY)
@JsonIgnoreProperties(value = { "hibernateLazyInitializer"
		, "javassistLazyInitializer", "revisionEntity"
		, "handler" }, ignoreUnknown = true)
@MappedSuperclass
@EntityListeners({ SaveUpdateAuditListener.class })
@AuditOverrides({ @AuditOverride(forClass = BaseEntity.class) })
public abstract class BaseEntity<ID extends Serializable> extends PersistableEntity<ID>
		implements DefaultAuditable<String, ID> {

    private static final long serialVersionUID = 2476761516236455260L;

    /** 乐观锁版本,初始设置为0 */
    private Integer version = 0;

    private String createdBy;

    protected Date createdDate;

    private String lastModifiedBy;

    private Date lastModifiedDate;

    public abstract void setId(final ID id);

    @Version
    @Column(name = "optlock", nullable = false)
    @JsonProperty
    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public void resetCommonProperties() {
        setId(null);
        version = 0;
        addExtraAttribute(PersistableEntity.EXTRA_ATTRIBUTE_DIRTY_ROW, true);
    }

    private static final String[] PROPERTY_LIST = new String[] { "id", "version" };

    public String[] retriveCommonProperties() {
        return PROPERTY_LIST;
    }

    @Override
    @Transient
    @JsonProperty
    public String getDisplay() {
        return "[" + getId() + "]" + this.getClass().getSimpleName();
    }

    @Column(length = 100)
    @JsonIgnore
    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @JsonProperty
    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    @Column(length = 100)
    @JsonIgnore
    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @JsonSerialize(using = DateTimeJsonSerializer.class)
    @JsonIgnore
    public Date getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Date lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }
}
