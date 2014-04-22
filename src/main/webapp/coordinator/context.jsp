<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<c:set var="dcp" scope="request" value="${master_degree.degreeCurricularPlan.externalId}" />
<c:set var="degree" scope="request" value="${master_degree.degreeCurricularPlan.degree}" />
<c:set var="base" scope="request" value="${pageContext.request.contextPath}/coordinator" />

<style>
.navbar {
  margin-bottom: 0;
}
</style>

<div class="row">
  <nav class="navbar navbar-default" role="navigation">
    <div class="container-fluid">
      <!-- Brand and toggle get grouped for better mobile display -->
      <div class="navbar-header">
        <button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1">
        <span class="sr-only">Toggle navigation</span>
        <span class="icon-bar"></span>
        <span class="icon-bar"></span>
        <span class="icon-bar"></span>
        </button>
        <a class="navbar-brand" href="${base}/coordinatorIndex.do?degreeCurricularPlanID=${dcp}">${master_degree.degreeCurricularPlan.name} - <span class="small">${master_degree.executionYear.year}</span></a>
      </div>

      <!-- Collect the nav links, forms, and other content for toggling -->
      <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
        <ul class="nav navbar-nav">
          <li>
            <a href="${base}/xYear.do?method=showDisclaimer&degreeCurricularPlanID=${dcp}">
              <bean:message key="label.coordinator.analyticTools"/>
            </a>
          </li>
          <c:if test="${isCoordinator}">
            <li class="dropdown">
              <a href="#" class="dropdown-toggle" data-toggle="dropdown"><bean:message key="label.coordinator.management"/> <b class="caret"></b></a>
              <ul class="dropdown-menu">
                <li>
                  <a href="${base}/viewCoordinationTeam.do?method=chooseExecutionYear&degreeCurricularPlanID=${dcp}">
                    <bean:message key="link.coordinator.degreeCurricularPlan.coordinationTeam"/>
                  </a>
                </li>
                <li>
                  <a href="${base}/scientificCommissionTeamDA.do?method=manage&degreeCurricularPlanID=${dcp}">
                    <bean:message key="link.coordinator.degreeCurricularPlan.scientificCommissionTeam"/>
                  </a>
                </li>
                <li>
                  <a href="${base}/degreeSiteManagement.do?method=subMenu&degreeCurricularPlanID=${dcp}">
                    <bean:message key="link.coordinator.degreeSite.management"/>
                  </a>
                </li>
                <li>
                  <a href="${base}/announcementsManagement.do?method=viewBoards&degreeCurricularPlanID=${dcp}">
                    <bean:message key="label.coordinator.degreeSite.announcements"/>
                  </a>
                </li>
                <c:if test="${degree.bolonhaDegree}">
                  <li>
                    <a href="${base}/degreeCurricularPlan/showDegreeCurricularPlanBolonha.faces?degreeCurricularPlanID=${dcp}&organizeBy=groups&showRules=false&hideCourses=false">
                      <bean:message key="link.coordinator.degreeCurricularPlan.management"/>
                    </a>
                  </li>
                  <li>
                    <a href="${base}/degreeCurricularPlan/showAllCompetenceCourses.faces?degreeCurricularPlanID=${dcp}">
                      <bean:message key="list.competence.courses"/>
                    </a>
                  </li>
                  <li>
                    <a href="${base}/degreeCurricularPlan/equivalencyPlan.do?method=showPlan&degreeCurricularPlanID=${dcp}">
                      <bean:message key="link.equivalency.plan"/>
                    </a>
                  </li>
                  <li>
                    <a href="${base}/viewInquiriesResults.do?method=prepare&degreeCurricularPlanID=${dcp}">
                      <bean:message key="title.inquiries.results" bundle="INQUIRIES_RESOURCES"/>
                    </a>
                  </li>
                </c:if>
                <li>
                  <a href="${base}/sendEmail.do?method=sendEmail&degreeCurricularPlanID=${dcp}">
                    <bean:message key="link.coordinator.sendMail"/>
                  </a>
                </li>
                <c:if test="${!degree.bolonhaDegree}">
                  <li>
                    <a href="${base}/degreeCurricularPlanManagement.do?method=showActiveCurricularCourses&degreeCurricularPlanID=${dcp}">
                      <bean:message key="link.coordinator.degreeCurricularPlan.management"/>
                    </a>
                  </li>
                  <li>
                    <a href="${base}/executionCoursesInformation.do?method=prepareChoiceForCoordinator&degreeCurricularPlanID=${dcp}">
                      <bean:message key="link.coordinator.executionCoursesInformation"/>
                    </a>
                  </li>
                  <li>
                    <a href="${base}/teachersInformation.do?executionDegreeId=${master_degree.externalId}&degreeCurricularPlanID=${dcp}">
                      <bean:message key="link.coordinator.teachersInformation"/>
                    </a>
                  </li>
                </c:if>
              </ul>
            </li>
            <c:if test="${!degree.bolonhaDegree}">
              <li>
                  <a href="${base}/evaluation/evaluationsCalendar.faces?degreeCurricularPlanID=${dcp}">
                    <bean:message key="label.coordinator.manageEvaluations"/>
                  </a>
              </li>
            </c:if>
          </c:if>
          <c:if test="${degree.degreeType.name() == 'MASTER_DEGREE'}">
            <li class="dropdown">
              <a href="#" class="dropdown-toggle" data-toggle="dropdown"><bean:message key="link.coordinator.candidate"/> <b class="caret"></b></a>
              <ul class="dropdown-menu">
                <li>
                  <a href="${base}/candidateOperation.do?method=getCandidates&action=visualize&degreeCurricularPlanID=${dcp}">
                    <bean:message key="link.coordinator.visualizeCandidate"/>
                  </a>
                </li>
                <li>
                  <a href="${base}/prepareCandidateApproval.do?method=chooseExecutionDegree&degreeCurricularPlanID=${dcp}">
                    <bean:message key="link.coordinator.approveCandidates"/>
                  </a>
                </li>
                <li>
                  <a href="${base}/displayCandidateListToMakeStudyPlan.do?method=prepareSelectCandidates&degreeCurricularPlanID=${dcp}">
                    <bean:message key="link.masterDegree.administrativeOffice.makeStudyPlan"/>
                  </a>
                </li>
                <li>
                  <a href="${base}/printAllCandidatesList.do?method=prepare&degreeCurricularPlanID=${dcp}">
                    <bean:message key="link.masterDegree.candidateListFilter.printListAllCandidatesFilterMenu"/>
                  </a>
                </li>
              </ul>
            </li>
            <li class="dropdown">
              <a href="#" class="dropdown-toggle" data-toggle="dropdown"><bean:message key="link.masterDegree.administrativeOffice.lists"/> <b class="caret"></b></a>
              <ul class="dropdown-menu">
                <li>
                  <a href="${base}/listStudentsForCoordinator.do?method=getStudentsFromDCP&degreeCurricularPlanID=${dcp}">
                    <bean:message key="link.coordinator.studentListByDegree"/>
                  </a>
                </li>
                <li>
                  <a href="${base}/listStudentsForCoordinator.do?method=getCurricularCourses&jspTitle=title.studentListByCourse&degreeCurricularPlanID=${dcp}">
                    <bean:message key="link.coordinator.studentListByCourse"/>
                  </a>
                </li>
                <li>
                  <a href="${base}/student/displayStudentThesisList.faces?degreeCurricularPlanID=${dcp}">
                    <bean:message key="link.coordinator.studentByThesis"/>
                  </a>
                </li>
              </ul>
            </li>
          </c:if>
          <c:if test="${degree.degreeType.name() != 'MASTER_DEGREE'}">
            <li class="dropdown">
              <a href="#" class="dropdown-toggle" data-toggle="dropdown"><bean:message key="label.coordinator.degreeSite.tutorship"/> <b class="caret"></b></a>
              <ul class="dropdown-menu">
                <li>
                  <a href="${pageContext.request.contextPath}/tutorado" target="_blank">
                    <bean:message key="link.coordinator.gepTutorshipPage"/>
                  </a>
                </li>
                <li>
                  <a href="${base}/tutorTeachers.do?method=prepareTutorSelection&executionDegreeId=${master_degree.externalId}&degreeCurricularPlanID=${dcp}">
                    <bean:message key="link.coordinator.tutorTeachers" bundle="COORDINATOR_RESOURCES"/>
                  </a>
                </li>
              </ul>
            </li>
            <li class="dropdown">
              <a href="#" class="dropdown-toggle" data-toggle="dropdown"><bean:message key="label.coordinator.degreeSite.students"/> <b class="caret"></b></a>
              <ul class="dropdown-menu">
                <li>
                  <a href="${base}/viewStudentCurriculumSearch.do?method=prepare&executionDegreeId=${master_degree.externalId}&degreeCurricularPlanID=${dcp}">
                    <bean:message key="label.coordinator.studentInformation" />
                  </a>
                </li>
                <li>
                  <a href="${base}/students.faces?executionDegreeId=${master_degree.externalId}&degreeCurricularPlanID=${dcp}">
                    <bean:message key="list.students" />
                  </a>
                </li>
                <li>
                  <a href="${base}/weeklyWorkLoad.do?method=prepare&degreeCurricularPlanID=${dcp}">
                    <bean:message key="link.weekly.work.load" />
                  </a>
                </li>
                <li>
                  <a href="${base}/delegatesManagement.do?method=prepare&executionDegreeId=${master_degree.externalId}&degreeCurricularPlanID=${dcp}">
                    <bean:message key="link.delegatesManagement" bundle="COORDINATOR_RESOURCES" />
                  </a>
                </li>
              </ul>
            </li>
            <c:if test="${degree.degreeType.name() == 'BOLONHA_DEGREE' || degree.degreeType.name() == 'BOLONHA_INTEGRATED_MASTER_DEGREE' || degree.degreeType.name() == 'BOLONHA_MASTER_DEGREE' }">
              <li class="dropdown">
                <a href="#" class="dropdown-toggle" data-toggle="dropdown"><bean:message key="label.candidacies"/> <b class="caret"></b></a>
                <ul class="dropdown-menu">
                  <li>
                    <a href="${base}/caseHandlingSecondCycleCandidacyProcess.do?method=intro&executionDegreeId=${master_degree.externalId}&degreeCurricularPlanID=${dcp}">
                      <bean:message key="link.coordinator.second.cycle.applications"/>
                    </a>
                  </li>
                  <li>
                    <a href="${base}/caseHandlingDegreeCandidacyForGraduatedPersonProcess.do?method=intro&executionDegreeId=${master_degree.externalId}&degreeCurricularPlanID=${dcp}">
                      <bean:message key="link.coordinator.degree.applications.for.graduated"/>
                    </a>
                  </li>
                  <li>
                    <a href="${base}/caseHandlingDegreeChangeCandidacyProcess.do?method=intro&executionDegreeId=${master_degree.externalId}&degreeCurricularPlanID=${dcp}">
                      <bean:message key="link.coordinator.degree.change.application"/>
                    </a>
                  </li>
                  <li>
                    <a href="${base}/caseHandlingDegreeTransferCandidacyProcess.do?method=intro&executionDegreeId=${master_degree.externalId}&degreeCurricularPlanID=${dcp}">
                      <bean:message key="link.coordinator.degree.transfer.appication"/>
                    </a>
                  </li>
                  <li>
                    <a href="${base}/caseHandlingStandaloneCandidacyProcess.do?method=intro&executionDegreeId=${master_degree.externalId}&degreeCurricularPlanID=${dcp}">
                      <bean:message key="label.candidacy.standalone"/>
                    </a>
                  </li>
                </ul>
              </li>
            </c:if>
          </c:if>
          <c:if test="${isScientificCommissionMember}">
            <li class="dropdown">
              <a href="#" class="dropdown-toggle" data-toggle="dropdown"><bean:message key="label.coordinator.thesis"/> <b class="caret"></b></a>
              <ul class="dropdown-menu">
                <li>
                  <a href="${base}/manageThesis.do?method=searchStudent&degreeCurricularPlanID=${dcp}">
                    <bean:message key="link.coordinator.thesis.viewStudent"/>
                  </a>
                </li>
                <li>
                  <a href="${base}/manageFinalDegreeWork.do?method=showChooseExecutionDegreeForm&degreeCurricularPlanID=${dcp}">
                    <bean:message key="label.coordinator.thesis"/>
                  </a>
                </li>
                <li>
                  <a href="${base}/manageThesis.do?method=listThesis&degreeCurricularPlanID=${dcp}">
                    <bean:message key="link.coordinator.thesis.list"/>
                  </a>
                </li>
                <li>
                  <a href="${pageContext.request.contextPath}/publico/viewFinalDegreeWorkProposals.do">
                    <bean:message key="link.finalDegreeWork.proposal.listings" bundle="STUDENT_RESOURCES"/>
                  </a>
                </li>
              </ul>
            </li>
          </c:if>
        </ul>
        <ul class="nav navbar-nav navbar-right">
          <li>
            <a href="${base}/searchDLog.do?method=prepareInit&degreeCurricularPlanID=${dcp}">
              <bean:message key="label.coordinator.logs"/>
            </a>
          </li>
        </ul>
      </div><!-- /.navbar-collapse -->
    </div><!-- /.container-fluid -->
  </nav>
</div>
