package net.sourceforge.fenixedu.domain;

import java.util.List;

import net.sourceforge.fenixedu.domain.accessControl.DegreeCoordinatorsGroup;
import net.sourceforge.fenixedu.domain.accessControl.FixedSetGroup;
import net.sourceforge.fenixedu.domain.accessControl.GroupUnion;
import net.sourceforge.fenixedu.domain.accessControl.RoleTypeGroup;
import net.sourceforge.fenixedu.domain.organizationalStructure.PedagogicalCouncilUnit;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.injectionCode.IGroup;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

/**
 * Specific site instance that is associated with the unit that represents the
 * Pedagogical Council of the institution.
 * <p>
 * There should exist only one instance of this site (matching the single unit that represents the council). Nevertheless that is
 * not verified.
 * 
 * @author cfgi
 */
public class PedagogicalCouncilSite extends PedagogicalCouncilSite_Base {

    public PedagogicalCouncilSite(PedagogicalCouncilUnit pedagogicalCouncil) {
        super();

        setUnit(pedagogicalCouncil);
    }

    @Override
    public IGroup getOwner() {
        return new GroupUnion(new RoleTypeGroup(RoleType.PEDAGOGICAL_COUNCIL), new RoleTypeGroup(RoleType.TUTORSHIP),
                new FixedSetGroup(getManagers()));
    }

    @Override
    public List<IGroup> getContextualPermissionGroups() {
        List<IGroup> list = super.getContextualPermissionGroups();

        list.add(new DegreeCoordinatorsGroup());

        return list;
    }

    /**
     * This method searchs for the first instance of a PedagogicalCouncilSite.
     * 
     * @return the site associated with the Pedagogical Council or <code>null</code> if there is no such site
     */
    public static PedagogicalCouncilSite getSite() {
        final PedagogicalCouncilUnit pedagogicalCouncilUnit = PedagogicalCouncilUnit.getPedagogicalCouncilUnit();
        return pedagogicalCouncilUnit == null ? null : (PedagogicalCouncilSite) pedagogicalCouncilUnit.getSite();
    }

    @Override
    public MultiLanguageString getName() {
        return new MultiLanguageString("");
    }
}
