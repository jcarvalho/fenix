package net.sourceforge.fenixedu.presentationTier.renderers.providers.executionCourse;

import net.sourceforge.fenixedu.domain.Section;
import net.sourceforge.fenixedu.domain.Site;
import net.sourceforge.fenixedu.presentationTier.Action.manager.SectionCreator;
import pt.ist.fenixWebFramework.rendererExtensions.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public abstract class SectionProvider implements DataProvider {

    @Override
    public Object provide(Object source, Object currentValue) {
        Section self;
        Section superiorSection;
        Site site;

        if (source instanceof Section) {
            self = (Section) source;

            superiorSection = self.getSuperiorSection();
            site = self.getOwnerSite();
        } else if (source instanceof SectionCreator) {
            SectionCreator creator = (SectionCreator) source;

            self = null;

            superiorSection = creator.getSuperiorSection();
            site = creator.getSite();
        } else {
            throw new RuntimeException("type not supported");
        }

        return provideForContext(site, superiorSection, self);
    }

    public abstract Object provideForContext(Site site, Section superiorSection, Section self);

    @Override
    public Converter getConverter() {
        return new DomainObjectKeyConverter();
    }

}
