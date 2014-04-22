package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import net.sourceforge.fenixedu.domain.UnitSite;
import pt.ist.fenixWebFramework.rendererExtensions.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

/**
 * Provides as possibilities all sections contained in the Side section if
 * available.
 * 
 * @author pcma
 */
public class UnitSiteInitialSectionProvider implements DataProvider {

    @Override
    public Object provide(Object source, Object currentValue) {
        UnitSite unitSite = (UnitSite) source;

        return unitSite.getSideSection().getChildrenSections();

    }

    @Override
    public Converter getConverter() {
        return new DomainObjectKeyConverter();
    }

}
