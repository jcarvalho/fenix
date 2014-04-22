<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://fenixedu.org/taglib/jsf-portal" prefix="fp"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>

<f:view>

<f:loadBundle basename="resources/ApplicationResources" var="bundle"/>
<f:loadBundle basename="resources/EnumerationResources" var="bundleEnumeration"/>

<%
	net.sourceforge.fenixedu.presentationTier.Action.coordinator.DegreeCoordinatorIndex.setCoordinatorContext(request);
%>
<fp:select actionClass="net.sourceforge.fenixedu.presentationTier.Action.coordinator.DegreeCoordinatorIndex" />
<jsp:include page="/coordinator/context.jsp" />


	<h:dataTable value="#{listStudentThesis.masterDegreeThesisDataVersions}" var="masterDegreeThesisDataVersion" cellpadding="0">
		<h:column>
			<h:panelGrid columns="1" styleClass="tstyle1" width="100%">
				<h:panelGrid columns="3" width="100%">
					<h:outputText value="#{bundle['label.coordinator.studentNumber']}" styleClass="bold" />
					<h:outputText value="#{bundle['label.coordinator.studentName']}" styleClass="bold" />
					<h:outputText value="#{bundle['label.coordinator.planState']}" styleClass="bold" />
					<h:outputText value="#{masterDegreeThesisDataVersion.infoMasterDegreeThesis.infoStudentCurricularPlan.infoStudent.number}" />
					<h:outputText value="#{masterDegreeThesisDataVersion.infoMasterDegreeThesis.infoStudentCurricularPlan.infoStudent.infoPerson.nome}" />
					<h:outputText value="#{bundleEnumeration[masterDegreeThesisDataVersion.infoMasterDegreeThesis.infoStudentCurricularPlan.currentState.name]}" />				
				</h:panelGrid>
				<h:outputText value=" " />
				<h:panelGrid columns="1" >
					<h:outputText value="#{bundle['label.coordinator.title']}" styleClass="bold"/>
					<h:outputText value="#{masterDegreeThesisDataVersion.dissertationTitle}" />
				</h:panelGrid>
				<h:outputText value=" " />
				<h:dataTable value="#{masterDegreeThesisDataVersion.allGuiders}" var="guider" columnClasses="solidBorderClass" headerClass="solidBorderClass" cellspacing="0" width="100%">
					<h:column>
						<h:outputFormat value="#{bundleEnumeration[guider.guiderType]}" styleClass="bold"/>
					</h:column>			
					<h:column >
						<f:facet name="header">
							<h:outputText value="#{bundle['label.coordinator.guiderNumber']}" />		
						</f:facet>
						<h:outputText value="#{(!empty guider.guiderNumber) ? guider.guiderNumber : '-'}" />
					</h:column>
					<h:column>
						<f:facet name="header">
							<h:outputText value="#{bundle['label.coordinator.name']}" />		
						</f:facet>
						<h:outputText value="#{guider.guiderName}" />				
					</h:column>
					<h:column>
						<f:facet name="header">
							<h:outputText value="#{bundle['label.coordinator.institution']}" />		
						</f:facet>
						<h:outputText value="#{guider.institutionName}" />				
					</h:column>
				</h:dataTable>					
			</h:panelGrid>
		</h:column>
	</h:dataTable>

</f:view>