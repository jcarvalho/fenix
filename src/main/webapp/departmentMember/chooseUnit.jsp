<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>

<h3><bean:message key="label.select.unit"></bean:message></h3>
<br />

<ul>
	<c:forEach var="unit" items="${units}">
		<blockquote><a href="?unitId=${unit.externalId}">${unit.name} (${unit.acronym})</a></blockquote>
	</c:forEach>
</ul>
