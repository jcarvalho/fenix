<%@ page language="java" %>

<%@ page import="java.lang.String" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>


	<h2><bean:message key="title.NewProjectProposals"/></h2>
	
	<table width="98%" cellpadding="0" cellspacing="0">
	<tr>
		<td class="infoop">
			<bean:message key="label.teacher.newProjectProposals.description" />
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
			<th class="listClasses-header" width="25%" ><bean:message key="label.projectName" />
			</th>
			<th class="listClasses-header" width="45%" ><bean:message key="label.projectDescription" />
			</th>
			<th class="listClasses-header" width="30%" ><bean:message key="label.newProjectProposalExecutionCourses" />
			</th>
		</tr>
            <logic:iterate id="infoGroupProperties" name="newProposals" >
                <tr>
                    <td class="listClasses" align="left">
                        <b><html:link page="<%= "/studentGroupManagement.do?method=prepareImportGroupProperties&amp;executionCourseID=" + pageContext.findAttribute("executionCourseID")%>" paramId="groupPropertiesCode" paramName="infoGroupProperties" paramProperty="externalId">
							<bean:write name="infoGroupProperties" property="name"/></html:link></b>
                    </td>
                                        
                    <td class="listClasses">
             		<logic:notEmpty name="infoGroupProperties" property="projectDescription">
                     	<bean:write name="infoGroupProperties" property="projectDescription" filter="false"/>
                	</logic:notEmpty>
                	
             		<logic:empty name="infoGroupProperties" property="projectDescription">
                     	<bean:message key="message.project.wihtout.description"/>
                	</logic:empty>
                	</td>
                	
                	 
                	<td class="listClasses" align="left">
            		    <logic:iterate id="infoExportGrouping" name="infoGroupProperties" property="exportGroupings" >
	                		<bean:define id="infoExecutionCourse" name="infoExportGrouping" property="executionCourse" />
							<bean:write name="infoExecutionCourse" property="nome"/><br/>
                    	 </logic:iterate>
                    </td>
                	
                </tr>
            </logic:iterate>
            
            </tbody>
</table>
