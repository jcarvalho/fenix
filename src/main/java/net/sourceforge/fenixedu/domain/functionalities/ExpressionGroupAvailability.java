package net.sourceforge.fenixedu.domain.functionalities;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accessControl.Group;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.ExpressionGroup;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.exceptions.GroupDynamicExpressionException;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.exceptions.GroupExpressionException;
import net.sourceforge.fenixedu.domain.contents.Content;

import org.fenixedu.bennu.core.security.Authenticate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class represents an availability policy base on groups created from
 * expression group language.
 * 
 * @author cfgi
 */
public class ExpressionGroupAvailability extends ExpressionGroupAvailability_Base {

    private static final Logger logger = LoggerFactory.getLogger(ExpressionGroupAvailability.class);

    /** cached not persisted group */
    private ExpressionGroup group;

    protected ExpressionGroupAvailability() {
        super();
    }

    /**
     * Creates a <code>GroupAvailability</code> to the given functionality and
     * with the given expression. The expression is converted into an {@link ExpressionGroup} so if the expression is invalid an
     * exception will
     * be thrown.
     * 
     * @param item
     *            the target item
     * @param expression
     *            the group expression
     * 
     * @exception GroupExpressionException
     *                when the expression is not correct
     */

    public ExpressionGroupAvailability(Content content, String expression) {
        super();

        setContent(content);
        setExpression(expression);
    }

    /**
     * Changes the current expression to the given one. An {@link ExpressionGroup} is created with the given expression.
     * 
     * @exception GroupExpressionException
     *                when the expression is not correct
     */
    @Override
    public void setExpression(String expression) {
        super.setExpression(expression);

        // we build the group immediatly to detect problems with the expression
        // as soon as possible. Nevertheless the group has a lazy construction
        // built in getGroup(). This is used after obtaining the group for the
        // persistent storage.
        setTargetGroup(new ExpressionGroup(expression));
    }

    protected ExpressionGroup getGroup() {
        if (this.group == null) {
            this.group = new ExpressionGroup(getExpression());
        }

        return this.group;
    }

    protected void setGroup(ExpressionGroup group) {
        this.group = group;
    }

    /**
     * Obtains a group from the current expression obtained by {@link #getExpression()}.
     * 
     * @return an expression group from the current expression
     * 
     * @exception GroupExpressionException
     *                when the expression is not correct
     */
    @Override
    public ExpressionGroup getTargetGroup() {
        return getGroup();
    }

    /**
     * Delegates the availability to the group obtained with {@link #getGroup()} . The functionality is available if the group
     * allows the <tt>UserView</tt> specified in the context.
     * 
     * @return <code>getGroup().allows(context.getUserView())</code>
     * 
     * @see Group#isMember(Person)
     * 
     * @exception GroupDynamicExpressionException
     *                when the evaluation of the expression group fails
     */
    @Override
    public boolean isAvailable() {
        try {
            return getTargetGroup().allows(Authenticate.getUser());
        } catch (GroupDynamicExpressionException e) {
            logger.error(e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new GroupDynamicExpressionException(e, "accessControl.group.expression.evaluation.error");
        }
    }

    @Deprecated
    public boolean hasExpression() {
        return getExpression() != null;
    }

}
