<%@tag pageEncoding="UTF-8"%>
<%@ attribute name="value" type="java.lang.Object" required="true"%>
<%out.print(org.jeecg.learning.chapter6.core.util.JsonUtils.writeValueAsString(value));%>