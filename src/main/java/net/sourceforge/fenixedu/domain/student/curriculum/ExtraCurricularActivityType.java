package net.sourceforge.fenixedu.domain.student.curriculum;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.fenixedu.bennu.core.domain.Bennu;

import pt.ist.fenixframework.Atomic;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class ExtraCurricularActivityType extends ExtraCurricularActivityType_Base {
    public ExtraCurricularActivityType() {
        setRootDomainObject(Bennu.getInstance());
    }

    public ExtraCurricularActivityType(MultiLanguageString name) {
        setName(name);
        setRootDomainObject(Bennu.getInstance());
    }

    @Atomic
    public void delete() {
        if (hasAnyExtraCurricularActivity()) {
            throw new DomainException("error.extraCurricularActivityTypes.unableToDeleteUsedType", this.getName().getContent());
        }
        setRootDomainObject(null);
        super.deleteDomainObject();
    }

    public String getNamePt() {
        return getName().getContent(MultiLanguageString.pt);
    }

    public void setNamePt(String name) {
        if (getName() == null) {
            setName(new MultiLanguageString(MultiLanguageString.pt, name));
        } else {
            setName(getName().with(MultiLanguageString.pt, name));
        }
    }

    public String getNameEn() {
        return getName().getContent(MultiLanguageString.en);
    }

    public void setNameEn(String name) {
        if (getName() == null) {
            setName(new MultiLanguageString(MultiLanguageString.en, name));
        } else {
            setName(getName().with(MultiLanguageString.en, name));
        }
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.student.curriculum.ExtraCurricularActivity> getExtraCurricularActivity() {
        return getExtraCurricularActivitySet();
    }

    @Deprecated
    public boolean hasAnyExtraCurricularActivity() {
        return !getExtraCurricularActivitySet().isEmpty();
    }

    @Deprecated
    public boolean hasName() {
        return getName() != null;
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

}
