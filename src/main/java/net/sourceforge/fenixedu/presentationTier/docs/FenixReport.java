package net.sourceforge.fenixedu.presentationTier.docs;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import net.sourceforge.fenixedu.util.DateI18NUtil;
import net.sourceforge.fenixedu.util.JasperPrintProcessor;

import org.apache.commons.lang.StringUtils;
import org.fenixedu.commons.i18n.I18N;
import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

abstract public class FenixReport implements Serializable {

    private static final Logger logger = LoggerFactory.getLogger(FenixReport.class);

    @SuppressWarnings("rawtypes")
    final private Collection dataSource;

    final private Map<String, Object> parameters = new HashMap<String, Object>();

    private ResourceBundle resourceBundle;

    private final ResourceBundle applicationBundle;

    private final ResourceBundle enumerationBundle;

    private final Locale locale;

    private final Locale language;

    static final protected String EMPTY_STR = StringUtils.EMPTY;

    static final protected String SINGLE_SPACE = " ";

    static final protected String DD_MMMM_YYYY = "dd MMMM yyyy";

    static final protected String DD_SLASH_MM_SLASH_YYYY = "dd/MM/yyyy";

    static final protected String YYYYMMMDD = "yyyyMMdd";

    static final protected String YYYYMMDDHHMMSS = "yyyyMMddHHmmss";

    protected FenixReport() {
        this(null, I18N.getLocale());
    }

    protected FenixReport(final Locale locale) {
        this(null, locale);
    }

    private FenixReport(final Collection<?> dataSource, final Locale locale) {
        this.dataSource = dataSource == null ? new ArrayList<Object>() : dataSource;
        this.enumerationBundle = ResourceBundle.getBundle("resources.EnumerationResources", locale);
        this.applicationBundle = ResourceBundle.getBundle("resources.ApplicationResources", locale);
        this.locale = locale;
        this.language = locale;
    }

    public final Collection<?> getDataSource() {
        return dataSource;
    }

    public final Map<String, Object> getParameters() {
        return parameters;
    }

    public final ResourceBundle getResourceBundle() {
        return resourceBundle;
    }

    public void setResourceBundle(ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
    }

    public ResourceBundle getApplicationBundle() {
        return applicationBundle;
    }

    public ResourceBundle getEnumerationBundle() {
        return enumerationBundle;
    }

    public Locale getLocale() {
        return locale;
    }

    public Locale getLanguage() {
        return language;
    }

    public String getReportTemplateKey() {
        return getClass().getName();
    }

    public JasperPrintProcessor getPreProcessor() {
        return null;
    }

    public void addParameter(final String key, final Object value) {
        this.parameters.put(key, value);
    }

    @SuppressWarnings("unchecked")
    public void addDataSourceElement(final Object object) {
        this.dataSource.add(object);
    }

    @SuppressWarnings("unchecked")
    public void addDataSourceElements(final Collection<?> objects) {
        this.dataSource.addAll(objects);
    }

    abstract public String getReportFileName();

    abstract protected void fillReport();

    protected void printParameters() {
        for (Map.Entry<String, Object> entry : parameters.entrySet()) {
            logger.info(String.format("%s - %s", entry.getKey(), entry.getValue()));
        }

    }

    protected String verboseDate(LocalDate date) {
        return "dia " + DateI18NUtil.verboseNumber(date.getDayOfMonth(), getEnumerationBundle()) + " do mês de "
                + date.toString("MMMM", new Locale("pt")) + " de "
                + DateI18NUtil.verboseNumber(date.getYear(), getEnumerationBundle());
    }
}
