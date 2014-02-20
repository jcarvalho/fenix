<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/jsf-fenix" prefix="fc"%>
<%@ taglib uri="http://fenixedu.org/taglib/jsf-portal" prefix="fp"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>

<f:view>" definition="definition.public.department" attributeName="body-inline">
	<f:loadBundle basename="resources/ScientificCouncilResources" var="scouncilBundle"/>
	<f:loadBundle basename="resources/EnumerationResources" var="enumerationBundle"/>
	<f:loadBundle basename="resources/PublicDepartmentResources" var="publicDepartmentBundle"/>
	<f:loadBundle basename="resources/GlobalResources" var="globalBundle"/>
	
	
	<h:outputText value="<div class='breadcumbs mvert0'>" escape="false"/>
		<h:outputLink value="#{CurricularCourseManagement.instalationUrl}" >
			<h:outputText value="#{CurricularCourseManagement.institutionAcronym}"/>
		</h:outputLink>
		&nbsp;&gt;&nbsp;
		<h:outputLink value="#{CurricularCourseManagement.institutionUrl}#{globalBundle['link.institution.structure']}" >
			<h:outputText value="#{publicDegreeInfoBundle['structure']}"/>
		</h:outputLink>
		&nbsp;&gt;&nbsp;
		<h:outputLink value="#{facesContext.externalContext.requestContextPath}/publico/department/showDepartments.faces">
			<h:outputText value="#{publicDepartmentBundle['academic.units']}"/>
		</h:outputLink>
		&nbsp;&gt;&nbsp;
		<h:outputLink value="departmentSite.do?method=presentation&amp;selectedDepartmentUnitID=#{CompetenceCourseManagement.selectedDepartmentUnit.externalId}">
			<h:outputText value="#{CompetenceCourseManagement.selectedDepartmentUnit.department.realName}"/>
		</h:outputLink>
		&nbsp;&gt;&nbsp;
		<h:outputText value="#{publicDepartmentBundle['department.faculty']}"/>
	<h:outputText value="</div>" escape="false"/>
	
	
	<h:outputText value="<h1>#{publicDepartmentBundle['department.faculty']} #{publicDepartmentBundle['of.masculine']} #{CompetenceCourseManagement.selectedDepartmentUnit.department.realName}</h1>" escape="false"/>
	
	<h:form>

		<fc:dataRepeater value="#{DepartmentManagement.sortedDepartmentCategories}" var="category" >
			<h:outputText value="<h2 class='greytxt mtop2'>#{category}</h2>" escape="false"/>
		
            <h:outputText value="<ul>" escape="false"/>
        		<fc:dataRepeater value="#{DepartmentManagement.teachersByCategory[category]}" var="teacher" >
                    <h:outputText value="<li>" escape="false"/>
        				<h:outputLink rendered="#{!empty teacher.person.homepage && teacher.person.homepage.activated}" value="../../homepage/#{teacher.person.user.username}">
					<h:outputText value="#{teacher.person.nickname}"/>
        				</h:outputLink>
        				<h:outputText rendered="#{empty teacher.person.homepage || !teacher.person.homepage.activated}" value="#{teacher.person.nickname}"/>
                    <h:outputText value="</li>" escape="false"/>
        		</fc:dataRepeater>
            <h:outputText value="</ul>" escape="false"/>
		</fc:dataRepeater>

	</h:form>

</f:view>
