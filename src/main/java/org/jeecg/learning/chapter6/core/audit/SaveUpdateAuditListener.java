package org.jeecg.learning.chapter6.core.audit;

import java.util.Date;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.stereotype.Component;

/**
 * 审计记录记录创建和修改信息
 * @see AuditingEntityListener
 *
 */
@Component
public class SaveUpdateAuditListener {

    private static final Logger logger = LoggerFactory.getLogger(SaveUpdateAuditListener.class);

    private AuditorAware auditorAware;

    private boolean dateTimeForNow = true;

    private boolean modifyOnCreation = false;

    //考虑到效率影响和实际作用不大，默认关闭update更新记录处理
    //如果需要记录变更过程，可以考虑使用专门的hibernate envers机制
    private boolean skipUpdateAudit = true;

    /**
     * Setter to inject a {@code AuditorAware} component to retrieve the current
     * auditor.
     * 
     * @param auditorAware the auditorAware to set
     */
    public void setAuditorAware(final AuditorAware auditorAware) {

        this.auditorAware = auditorAware;
    }

    public void setDateTimeForNow(boolean dateTimeForNow) {
        this.dateTimeForNow = dateTimeForNow;
    }

    public void setModifyOnCreation(final boolean modifyOnCreation) {
        this.modifyOnCreation = modifyOnCreation;
    }

    /**
     * Sets modification and creation date and auditor on the target object in
     * case it implements {@link DefaultAuditable} on persist events.
     * 
     * @param target
     */
    @PrePersist
    public void touchForCreate(Object target) {
        touch(target, true);
    }

    /**
     * Sets modification and creation date and auditor on the target object in
     * case it implements {@link DefaultAuditable} on update events.
     * 
     * @param target
     */
    @PreUpdate
    public void touchForUpdate(Object target) {
        if (skipUpdateAudit) {
            return;
        }
        touch(target, false);
    }

    private void touch(Object target, boolean isNew) {

        if (!(target instanceof DefaultAuditable)) {
            return;
        }

        @SuppressWarnings("unchecked")
        DefaultAuditable<String, ?> auditable = (DefaultAuditable<String, ?>) target;

        String auditor = touchAuditor(auditable, isNew);
        Date now = dateTimeForNow ? touchDate(auditable, isNew) : null;

        Object defaultedNow = now == null ? "not set" : now;
        Object defaultedAuditor = auditor == null ? "unknown" : auditor;

        logger.trace("Touched {} - Last modification at {} by {}", new Object[] { auditable, defaultedNow, defaultedAuditor });
    }

    /**
     * Sets modifying and creating auditioner. Creating auditioner is only set
     * on new auditables.
     * 
     * @param auditable
     * @return
     */
    private String touchAuditor(final DefaultAuditable<String, ?> auditable, boolean isNew) {

        if (null == auditorAware) {
            return null;
        }

        String auditor = auditorAware.getCurrentAuditor();

        if (isNew) {

            auditable.setCreatedBy(auditor);

            if (!modifyOnCreation) {
                return auditor;
            }
        }

        auditable.setLastModifiedBy(auditor);

        return auditor;
    }

    /**
     * Touches the auditable regarding modification and creation date. Creation
     * date is only set on new auditables.
     * 
     * @param auditable
     * @return
     */
    private Date touchDate(final DefaultAuditable<String, ?> auditable, boolean isNew) {

        Date now = new Date();

        if (isNew) {
            auditable.setCreatedDate(now);

            if (!modifyOnCreation) {
                return now;
            }
        }

        auditable.setLastModifiedDate(now);

        return now;
    }
}
