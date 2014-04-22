package net.sourceforge.fenixedu.presentationTier.servlets.startup;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletException;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpServletRequest;

import net.sourceforge.fenixedu.applicationTier.Servico.commons.ReadCurrentExecutionPeriod;
import net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.gratuity.CreateGratuitySituationsForCurrentExecutionYear;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.domain.Instalation;
import net.sourceforge.fenixedu.domain.Role;
import net.sourceforge.fenixedu.domain.contents.Container;
import net.sourceforge.fenixedu.domain.functionalities.FunctionalityContext;
import net.sourceforge.fenixedu.domain.organizationalStructure.UnitName;
import net.sourceforge.fenixedu.domain.organizationalStructure.UnitNamePart;
import net.sourceforge.fenixedu.domain.person.PersonNamePart;
import net.sourceforge.fenixedu.injectionCode.IllegalDataAccessException;
import net.sourceforge.fenixedu.presentationTier.Action.externalServices.PhoneValidationUtils;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants;
import net.sourceforge.fenixedu.domain.functionalities.FunctionalityContext;
import net.sourceforge.fenixedu.presentationTier.util.ExceptionInformation;
import net.sourceforge.fenixedu.util.FenixConfigurationManager;
import net.sourceforge.fenixedu.webServices.jersey.api.FenixJerseyAPIConfig;

import org.apache.commons.fileupload.FileUpload;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.domain.exceptions.BennuCoreDomainException;
import org.fenixedu.bennu.core.domain.groups.DynamicGroup;
import org.fenixedu.bennu.core.domain.groups.Group;
import org.fenixedu.bennu.core.presentationTier.servlets.filters.ExceptionHandlerFilter;
import org.fenixedu.bennu.core.presentationTier.servlets.filters.ExceptionHandlerFilter.CustomHandler;
import org.fenixedu.bennu.core.util.CoreConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ist.fenixWebFramework.servlets.filters.contentRewrite.RequestChecksumFilter;
import pt.ist.fenixWebFramework.servlets.filters.contentRewrite.RequestChecksumFilter.ChecksumPredicate;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ist.fenixframework.plugins.remote.domain.RemoteSystem;
import pt.utl.ist.fenix.tools.util.i18n.Language;

@WebListener
public class FenixInitializer implements ServletContextListener {

    private static final Logger logger = LoggerFactory.getLogger(FenixInitializer.class);

    @Override
    @Atomic(mode = TxMode.READ)
    public void contextInitialized(ServletContextEvent event) {

        logger.info("Initializing Fenix");

        //Hack: to set the proper language at startup - the Language from tools should be properly implemented
        CoreConfiguration.getConfiguration();
        Language.setDefaultLocale(Locale.getDefault());

        RemoteSystem.init();

        try {
            InfoExecutionPeriod infoExecutionPeriod = ReadCurrentExecutionPeriod.run();
            event.getServletContext().setAttribute(PresentationConstants.INFO_EXECUTION_PERIOD_KEY, infoExecutionPeriod);

            setScheduleForGratuitySituationCreation();

        } catch (Throwable e) {
            throw new Error("Error reading actual execution period!", e);
        }

        Instalation.ensureInstalation();
        loadLogins();
        loadPersonNames();
        loadUnitNames();
        loadRoles();
        startContactValidationServices();

        registerChecksumFilterRules();
        registerUncaughtExceptionHandler();

        initializeFenixAPI();
        initializeBennuManagersGroup();

        logger.info("Fenix initialized successfully");
    }

    private static void initializeBennuManagersGroup() {
        try {
            DynamicGroup.getInstance("managers");
        } catch (BennuCoreDomainException e) {
            logger.info("Create managers bennu group to RoleCustomGroup Managers");
            DynamicGroup.initialize("managers", Group.parse("role(MANAGER)"));
        }
    }

    private void initializeFenixAPI() {
        FenixJerseyAPIConfig.initialize();
    }

    @Override
    public void contextDestroyed(ServletContextEvent arg0) {

    }

    private void startContactValidationServices() {
        PhoneValidationUtils.getInstance();
    }

    private void loadLogins() {
        long start = System.currentTimeMillis();
        User.findByUsername("...PlaceANonExistingLoginHere...");
        long end = System.currentTimeMillis();
        logger.info("Load of all users took: " + (end - start) + "ms.");
    }

    private void loadPersonNames() {
        long start = System.currentTimeMillis();
        PersonNamePart.find("...PlaceANonExistingPersonNameHere...");
        long end = System.currentTimeMillis();
        logger.info("Load of all person names took: " + (end - start) + "ms.");
    }

    private void loadUnitNames() {
        long start = System.currentTimeMillis();
        UnitNamePart.find("...PlaceANonExistingUnitNameHere...");
        long end = System.currentTimeMillis();
        logger.info("Load of all unit names took: " + (end - start) + "ms.");

        start = System.currentTimeMillis();
        for (final UnitName unitName : Bennu.getInstance().getUnitNameSet()) {
            unitName.getName();
        }
        end = System.currentTimeMillis();
        logger.info("Load of all units took: " + (end - start) + "ms.");
    }

    private void loadRoles() {
        long start = System.currentTimeMillis();
        Role.getRoleByRoleType(null);
        long end = System.currentTimeMillis();
        logger.info("Load of all roles took: " + (end - start) + "ms.");
    }

    private void setScheduleForGratuitySituationCreation() {

        TimerTask gratuitySituationCreatorTask = new TimerTask() {

            @Override
            public void run() {
                try {
                    CreateGratuitySituationsForCurrentExecutionYear.runCreateGratuitySituationsForCurrentExecutionYear("");
                } catch (Exception e) {
                }

                // temporary
                try {
                    CreateGratuitySituationsForCurrentExecutionYear
                            .runCreateGratuitySituationsForCurrentExecutionYear("2003/2004");
                } catch (Exception e) {
                }
            }
        };

        try {
            Calendar calendar = Calendar.getInstance();
            String hourString = FenixConfigurationManager.getConfiguration().getGratuitySituationCreatorTaskHour();
            int scheduledHour = Integer.parseInt(hourString);
            if (scheduledHour == -1) {
                return;
            }
            int currentHour = calendar.get(Calendar.HOUR_OF_DAY);

            calendar.set(Calendar.HOUR_OF_DAY, scheduledHour);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);

            if (currentHour >= scheduledHour) {
                calendar.add(Calendar.DAY_OF_MONTH, 1);
            }
            Date firstTimeDate = calendar.getTime();

            Timer timer = new Timer();

            timer.schedule(gratuitySituationCreatorTask, firstTimeDate, 3600 * 24 * 1000);

        } catch (Exception e) {
        }

    }

    static final int APP_CONTEXT_LENGTH = FenixConfigurationManager.getConfiguration().appContext().length() + 1;

    private void registerChecksumFilterRules() {
        RequestChecksumFilter.registerFilterRule(new ChecksumPredicate() {
            @Override
            public boolean shouldFilter(HttpServletRequest request) {
                final String uri = request.getRequestURI().substring(APP_CONTEXT_LENGTH);

                if (uri.indexOf("domainbrowser/") >= 0) {
                    return false;
                }
                if (uri.indexOf("images/") >= 0) {
                    return false;
                }
                if (uri.indexOf("gwt/") >= 0) {
                    return false;
                }
                if (uri.indexOf("remote/") >= 0) {
                    return false;
                }
                if (uri.indexOf("javaScript/") >= 0) {
                    return false;
                }
                if (uri.indexOf("script/") >= 0) {
                    return false;
                }
                if (uri.indexOf("ajax/") >= 0) {
                    return false;
                }
                if (uri.indexOf("redirect.do") >= 0) {
                    return false;
                }
                if (uri.indexOf("home.do") >= 0) {
                    return false;
                }
                if (uri.indexOf("/student/fillInquiries.do") >= 0) {
                    return false;
                }
                if (uri.indexOf("/google") >= 0 && uri.endsWith(".html")) {
                    return false;
                }
                if ((uri.indexOf("/teacher/executionCourseForumManagement.do") >= 0 || uri
                        .indexOf("/student/viewExecutionCourseForuns.do") >= 0)
                        && request.getQueryString().indexOf("method=viewThread") >= 0) {
                    return false;
                }
                if (FileUpload.isMultipartContent(request)) {
                    return false;
                }
                final FunctionalityContext FunctionalityContext = getContextAttibute(request);
                if (FunctionalityContext != null) {
                    final Container container = FunctionalityContext.getSelectedTopLevelContainer();
                    if (container != null && container.isPublic() && (uri.indexOf(".do") < 0 || uri.indexOf("publico/") >= 0)) {
                        return false;
                    }
                }
                if (uri.indexOf("notAuthorized.do") >= 0) {
                    return false;
                }

                return uri.length() > 1 && (uri.indexOf("CSS/") == -1) && (uri.indexOf("ajax/") == -1)
                        && (uri.indexOf("images/") == -1) && (uri.indexOf("img/") == -1) && (uri.indexOf("download/") == -1)
                        && (uri.indexOf("external/") == -1) && (uri.indexOf("services/") == -1)
                        && (uri.indexOf("index.jsp") == -1) && (uri.indexOf("index.html") == -1)
                        && (uri.indexOf("login.do") == -1) && (uri.indexOf("loginCAS.do") == -1)
                        && (uri.indexOf("privado") == -1) && (uri.indexOf("loginPage.jsp") == -1)
                        && (uri.indexOf("loginExpired.jsp") == -1) && (uri.indexOf("loginExpired.do") == -1)
                        && (uri.indexOf("logoff.do") == -1) && (uri.indexOf("publico/") == -1)
                        && (uri.indexOf("showErrorPage.do") == -1) && (uri.indexOf("showErrorPageRegistered.do") == -1)
                        && (uri.indexOf("exceptionHandlingAction.do") == -1) && (uri.indexOf("manager/manageCache.do") == -1)
                        && (uri.indexOf("checkPasswordKerberos.do") == -1) && (uri.indexOf("siteMap.do") == -1)
                        && (uri.indexOf("fenixEduIndex.do") == -1) && (uri.indexOf("cms/forwardEmailAction.do") == -1)
                        && (uri.indexOf("isAlive.do") == -1) && (uri.indexOf("gwt/") == -1) && (uri.indexOf("remote/") == -1)
                        && (uri.indexOf("downloadFile/") == -1) && !(uri.indexOf("google") >= 0 && uri.endsWith(".html"))
                        && (uri.indexOf("api/fenix") == -1);
            }

            private FunctionalityContext getContextAttibute(final HttpServletRequest httpServletRequest) {
                return (FunctionalityContext) httpServletRequest.getAttribute(FunctionalityContext.CONTEXT_KEY);
            }
        });
    }

    public static class FenixCustomExceptionHandler implements CustomHandler {
        @Override
        public boolean isCustomizedFor(Throwable t) {
            return true;
        }

        @Override
        public void handle(HttpServletRequest request, ServletResponse response, final Throwable t) throws ServletException,
                IOException {
            ExceptionInformation exceptionInfo = new ExceptionInformation(request, t);
            if (FunctionalityContext.getCurrentContext(request) != null) {
                exceptionInfo.getRequestBean().setRequestContext(
                        FunctionalityContext.getCurrentContext(request).getSelectedTopLevelContainer());
            }

            if (CoreConfiguration.getConfiguration().developmentMode()) {
                request.setAttribute("debugExceptionInfo", exceptionInfo);
            } else {
                request.setAttribute("requestBean", exceptionInfo.getRequestBean());
                request.setAttribute("exceptionInfo", exceptionInfo.getExceptionInfo());
            }

            String urlToForward =
                    t instanceof IllegalDataAccessException ? "/exception/notAuthorizedForward.jsp" : "/showErrorPage.do";
            request.getRequestDispatcher(urlToForward).forward(request, response);
        }
    }

    private void registerUncaughtExceptionHandler() {
        ExceptionHandlerFilter.register(new FenixCustomExceptionHandler());
    }

}
