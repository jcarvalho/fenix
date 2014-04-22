<%@ page language="java" %>

<%@ page import="java.lang.String" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>

	<h2><bean:message key="title.SentedProjectProposalsWaiting"/></h2>
	
	<table width="98%" cellpadding="0" cellspacing="0">
	<tr>
		<td class="infoop">
			<bean:message key="label.teacher.SentedProjectProposalsWaiting.description" />
		</td>
	</tr>
	</table>
	<br/>

	<span class="error"><!-- Error messages go here --><html:errors /></span> 	
	
<br/>
	<html:link page="<%="/studentGroupManagement.do?method=prepareViewExecutionCourseProjects&amp;executionCourseID=" + pageContext.findAttribute("executionCourseID")%>">
    	<bean:message key="link.backToProjectsAndLink"/></html:link>
<br/>
<br/>
<table width="98%" border="0" style="text-align: left;">
        <tbody>
    	 <tr>
			<th class="listClasses-header" width="30%" ><bean:message key="label.projectName" />
			</th>
			<th class="listClasses-header" width="40%" ><bean:message key="label.SentedProjectProposalsWaitingExecutionCourses" />
			</th>
			<th class="listClasses-header" width="30%" ><bean:message key="label.SentedProjectProposalsWaitingExecutionCoursesOption" />
			</th>
		</tr>
	<logic:iterate id="infoGroupProperties" name="groupings">
		<bean:define id="groupPropertiesCode" name="infoGroupProperties" property="externalId"/>
		<logic:iterate id="infoGroupPropertiesExecutionCourseElement" name="infoGroupProperties" property="exportGroupings">
	       <bean:define id="infoExecutionCourse" name="infoGroupPropertiesExecutionCourseElement" property="executionCourse" />
	       <bean:define id="executionCourseCode" name="infoExecutionCourse" property="externalId"/>
			<tr>
				
				<td class="listClasses" align="left">
					<b><bean:write name="infoGroupProperties" property="name"/></b>
                </td>
                    
				<td class="listClasses" align="left">
					<bean:define id="executionCourseCode" name="infoExecutionCourse" property="externalId"/>
    			  	<bean:write name="infoExecutionCourse" property="nome"/>
    			</td>
		
				<td class="listClasses" align="left">
					<html:link page="<%="/studentGroupManagement.do?method=deleteProjectProposal&amp;executionCourseID=" + pageContext.findAttribute("executionCourseID") + "&amp;groupPropertiesCode=" + groupPropertiesCode.toString() + "&amp;executionCourseCode=" + executionCourseCode.toString()%>">
					<bean:message key="link.deleteProjectProposal"/></html:link>
				</td>
			</tr>
			</logic:iterate>	
	        </logic:iterate>						
            </tbody>
</table>
