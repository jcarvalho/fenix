package net.sourceforge.fenixedu.presentationTier.Action.publico.rss;

import java.net.URL;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.util.FenixConfigurationManager;
import pt.ist.fenixframework.DomainObject;
import pt.ist.fenixframework.FenixFramework;
import de.nava.informa.core.ChannelIF;
import de.nava.informa.core.ItemIF;
import de.nava.informa.impl.basic.ChannelBuilder;
import de.nava.informa.impl.basic.Item;

public abstract class GenerateExecutionCourseRSS extends InformaRSSAction {

    @Override
    protected ChannelIF getRSSChannel(final HttpServletRequest request) throws Exception {
        final String id = request.getParameter("id");
        final DomainObject object = FenixFramework.getDomainObject(id);
        if (object != null && object instanceof ExecutionCourse) {
            final ExecutionCourse executionCourse = (ExecutionCourse) object;
            final String executionCourseName = executionCourse.getNome();
            final ChannelBuilder builder = new ChannelBuilder();
            final ChannelIF channel = builder.createChannel(executionCourseName);
            channel.setDescription(getDescriptionPrefix() + " da disciplina " + executionCourseName);

            final String appContext = FenixConfigurationManager.getConfiguration().appContext();
            final String context = (appContext != null && appContext.length() > 0) ? "/" + appContext : "";
            final String commonLocalUrl =
                    context + "/publico/executionCourse.do?method=" + getMethodName() + "&executionCourseID="
                            + executionCourse.getExternalId().toString() + "#";

            for (final DomainObject domainObject : getObjects(executionCourse)) {
                final ItemIF item = new Item();
                fillItem(item, domainObject);
                item.setGuid(getItemGuidIF(item, domainObject));
                final String localUrl = commonLocalUrl + getIdPrefix() + domainObject.getExternalId().toString();
                item.setLink(new URL(request.getScheme(), request.getServerName(), request.getServerPort(), localUrl));
                channel.addItem(item);
            }

            return channel;
        }
        return null;
    }

    public abstract String getDescriptionPrefix();

    public abstract String getMethodName();

    public abstract Set<DomainObject> getObjects(final ExecutionCourse executionCourse);

    public abstract void fillItem(final ItemIF item, final DomainObject domainObject);

    public abstract String getIdPrefix();

}