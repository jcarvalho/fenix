package net.sourceforge.fenixedu.domain.messaging;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accessControl.Group;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.fenixedu.commons.StringNormalizer;
import org.joda.time.DateTime;

import pt.utl.ist.fenix.tools.util.i18n.Language;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public abstract class Forum extends Forum_Base {

    protected Forum() {

    }

    public Forum(MultiLanguageString name, MultiLanguageString description) {
        init(name, description);
    }

    public void init(MultiLanguageString name, MultiLanguageString description) {
        setCreationDate(new DateTime());
        setName(name);
        setDescription(description);
    }

    public boolean hasConversationThreadWithSubject(MultiLanguageString subject) {
        ConversationThread conversationThread = getConversationThreadBySubject(subject);

        return (conversationThread != null) ? true : false;
    }

    public ConversationThread getConversationThreadBySubject(MultiLanguageString subject) {
        for (ConversationThread conversationThread : getConversationThreadSet()) {
            final MultiLanguageString title = conversationThread.getTitle();
            if (title != null && title.equalInAnyLanguage(subject)) {
                return conversationThread;
            }
        }

        return null;
    }

    public int getConversationMessagesCount() {
        int total = 0;

        for (ConversationThread conversationThread : getConversationThreadSet()) {
            total += conversationThread.getMessageSet().size();
        }

        return total;
    }

    public void checkIfPersonCanWrite(Person person) {
        if (!getWritersGroup().isMember(person)) {
            throw new DomainException("forum.person.cannot.write");
        }
    }

    public void checkIfCanAddConversationThreadWithSubject(MultiLanguageString subject) {
        if (hasConversationThreadWithSubject(subject)) {
            throw new DomainException("forum.already.existing.conversation.thread");
        }
    }

    public void addEmailSubscriber(Person person) {
        if (!getReadersGroup().isMember(person)) {
            throw new DomainException("forum.cannot.subscribe.person.because.does.not.belong.to.readers");
        }

        ForumSubscription subscription = getPersonSubscription(person);
        if (subscription == null) {
            subscription = new ForumSubscription(person, this);
        }

        subscription.setReceivePostsByEmail(true);

    }

    public void removeEmailSubscriber(Person person) {
        ForumSubscription subscription = getPersonSubscription(person);

        if (subscription != null) {
            if (subscription.getFavorite() == false) {
                removeForumSubscriptions(subscription);
                subscription.delete();
            } else {
                subscription.setReceivePostsByEmail(false);
            }
        }

    }

    public ForumSubscription getPersonSubscription(Person person) {
        for (ForumSubscription subscription : getForumSubscriptionsSet()) {
            if (subscription.getPerson() == person) {
                return subscription;
            }
        }

        return null;
    }

    public boolean isPersonReceivingMessagesByEmail(Person person) {
        ForumSubscription subscription = getPersonSubscription(person);

        return (subscription != null) ? subscription.getReceivePostsByEmail() : false;
    }

    public ConversationThread createConversationThread(Person creator, MultiLanguageString subject) {
        checkIfPersonCanWrite(creator);
        checkIfCanAddConversationThreadWithSubject(subject);

        return new ConversationThread(this, creator, subject);
    }

    public abstract Group getReadersGroup();

    public abstract Group getWritersGroup();

    public abstract Group getAdminGroup();

    public MultiLanguageString getNormalizedName() {
        return normalize(getName());
    }

    public static MultiLanguageString normalize(final MultiLanguageString multiLanguageString) {
        if (multiLanguageString == null) {
            return null;
        }
        MultiLanguageString result = new MultiLanguageString();
        for (final Language language : multiLanguageString.getAllLanguages()) {
            result = result.with(language, normalize(multiLanguageString.getContent(language)));
        }
        return result;
    }

    public static String normalize(final String string) {
        return string == null ? null : StringNormalizer.normalize(string).replace(' ', '-');
    }

    public void delete() {
        for (final ForumSubscription forumSubscription : getForumSubscriptionsSet()) {
            forumSubscription.delete();
        }
        for (final ConversationThread thread : getConversationThreadSet()) {
            thread.delete();
        }
        setCreator(null);
        deleteDomainObject();
    }

}
