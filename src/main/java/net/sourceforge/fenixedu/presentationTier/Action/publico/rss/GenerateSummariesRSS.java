package net.sourceforge.fenixedu.presentationTier.Action.publico.rss;

import java.util.Set;

import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Summary;
import pt.ist.fenixframework.DomainObject;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;
import de.nava.informa.core.ItemIF;

public class GenerateSummariesRSS extends GenerateExecutionCourseRSS {

    @Override
    public String getDescriptionPrefix() {
        return "Sumários";
    }

    @Override
    public String getMethodName() {
        return "summaries";
    }

    @Override
    public Set getObjects(final ExecutionCourse executionCourse) {
        return executionCourse.getAssociatedSummariesSet();
    }

    @Override
    public void fillItem(final ItemIF item, final DomainObject domainObject) {
        final Summary summary = (Summary) domainObject;
        item.setTitle(summary.getTitle().getContent(MultiLanguageString.pt));
        item.setDate(summary.getLastModifiedDate());
        item.setDescription(summary.getSummaryText().getContent(MultiLanguageString.pt));
    }

    @Override
    public String getIdPrefix() {
        return "s";
    }

}
