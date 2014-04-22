<%@page import="org.fenixedu.commons.i18n.I18N"%>
<%@ page language="java" %>
<%@ page import="net.sourceforge.fenixedu.domain.organizationalStructure.Unit"%>
<%@ page import="net.sourceforge.fenixedu.domain.UnitSite"%>

<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<bean:define id="language" name="<%= org.apache.struts.Globals.LOCALE_KEY %>" property="language"/>

<%
	UnitSite site = (UnitSite) net.sourceforge.fenixedu.domain.cms.OldCmsSemanticURLHandler.getSite(request);
	Unit unit = site.getUnit();
		
    request.setAttribute("unit", unit);
    request.setAttribute("site", site);
	
    if (site != null && site.isDefaultLogoUsed()) {
		final String finalLanguage = I18N.getLocale().getLanguage();
    	//String finalLanguage = language == null ? "pt" : String.valueOf(language);
        request.setAttribute("siteDefaultLogo", 
        	String.format("%s/images/newImage2012/%s_%s.png", request.getContextPath(), unit.getAcronym(), finalLanguage));
    }
%>
<jsp:include page="../customized/symbolsRow.jsp"/>
