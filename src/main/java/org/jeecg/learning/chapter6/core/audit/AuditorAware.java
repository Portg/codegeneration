package org.jeecg.learning.chapter6.core.audit;

public interface AuditorAware {

    /**
     * Returns the current auditor of the application.
     * 
     * @return the current auditor
     */
    String getCurrentAuditor();
}
