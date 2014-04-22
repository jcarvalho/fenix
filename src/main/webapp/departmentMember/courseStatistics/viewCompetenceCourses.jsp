<%@ page isELIgnored="true"%>
<%@ page language="java"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml />
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers"	prefix="fr"%>

<bean:define id="executionSemesterId" name="courseStatisticsBean" property="executionSemester.externalId" />

<em><bean:message key="label.departmentMember" /></em>

<h2>
	<bean:message key="label.courseStatistics.competenceStatistics" />
</h2>

<h3>
	<bean:write name="courseStatisticsBean" property="department.nameI18n.content" />
</h3>

<fr:form id="chooseSemesterForm" action="/departmentCourses.do?method=prepareListCourses">
	<fr:edit id="courseStatisticsBean" name="courseStatisticsBean">
		<fr:schema bundle="DEPARTMENT_MEMBER_RESOURCES"
			type="net.sourceforge.fenixedu.presentationTier.Action.departmentMember.CourseStatisticsBean">
			<fr:slot name="executionSemester" layout="menu-select-postback"
				key="label.common.executionYear">
				<fr:property name="providerClass" value="net.sourceforge.fenixedu.presentationTier.renderers.providers.NotClosedExecutionPeriodsProvider" />
				<fr:property name="format" value="${qualifiedName}" />
				<fr:property name="saveOptions" value="true" />
			</fr:slot>
			<fr:layout>
				<fr:property name="classes" value="tstyle5 mtop05 mbottom15" />
			</fr:layout>
		</fr:schema>

	</fr:edit>
</fr:form>

<table class="tstyle1">
	<tr>

		<th>&nbsp;</th>

		<th colspan="3"><bean:message key="label.courseStatistics.firstCount" /></th>
		<th colspan="3"><bean:message key="label.courseStatistics.restCount" />
		<th colspan="3"><bean:message key="label.courseStatistics.totalCount" /></th>
		<th>&nbsp;</th>
	</tr>
	<tr>
		<th><bean:message key="label.common.courseName" /></th>
		<th><bean:message key="label.courseStatistics.enrolled" /></th>
		<th><bean:message key="label.courseStatistics.approved" /></th>
		<th><bean:message key="label.courseStatistics.average" /></th>
		<th><bean:message key="label.courseStatistics.enrolled" /></th>
		<th><bean:message key="label.courseStatistics.approved" /></th>
		<th><bean:message key="label.courseStatistics.average" /></th>
		<th><bean:message key="label.courseStatistics.enrolled" /></th>
		<th><bean:message key="label.courseStatistics.approved" /></th>
		<th><bean:message key="label.courseStatistics.average" /></th>
		<th><bean:message key="label.courseStatistics.approvedPercentage" /></th>

	</tr>
	<tbody>

		<logic:iterate id="competenceCourse" name="courseStatisticsBean" property="competenceCourses">
			<tr>
				<td>
					<bean:define id="competenceCourseId" name="competenceCourse" property="externalId" />
					<html:link action="<%="/departmentCourses.do?method=prepareDegreeCourses&competenceCourseId="+ competenceCourseId +"&executionSemesterId="+ executionSemesterId %>">
						<bean:write name="competenceCourse" property="name"/>
					</html:link>
				</td>
				<td class="aright"><bean:write name="competenceCourse" property="firstEnrolledCount" /></td>
				<td class="aright"><bean:write name="competenceCourse" property="firstApprovedCount" /></td>
				<td class="aright"><bean:write name="competenceCourse" property="firstApprovedAveragex" /></td>
				<td class="aright"><bean:write name="competenceCourse" property="restEnrolledCount" /></td>
				<td class="aright"><bean:write name="competenceCourse" property="restApprovedCount" /></td>
				<td class="aright"><bean:write name="competenceCourse" property="restApprovedAveragex" /></td>
				<td class="aright"><bean:write name="competenceCourse" property="totalEnrolledCount" /></td>
				<td class="aright"><bean:write name="competenceCourse" property="totalApprovedCount" /></td>
				<td class="aright"><bean:write name="competenceCourse" property="totalApprovedAveragex" /></td>
				<td class="aright"><bean:write name="competenceCourse" property="approvedPercentage" /></td>
			</tr>
		
		</logic:iterate>
	</tbody>
</table>