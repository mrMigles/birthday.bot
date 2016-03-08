package com.birthday.bot.skype.contact;

import com.birthday.bot.skype.settings.Contact;
import com.birthday.bot.skype.settings.loader.BirthdayBotSettings;
import com.samczsun.skype4j.Skype;
import com.samczsun.skype4j.exceptions.ConnectionException;
import com.birthday.bot.skype.holder.SkypeHolder;
import com.birthday.bot.skype.settings.Contacts;
import org.joda.time.DateTime;

import javax.xml.datatype.XMLGregorianCalendar;
import java.util.*;

/**
 * Factory for creating contact repository
 * Created by Vsevolod Kaimashnikov on 27.02.2016.
 */
public class ContactRepositoryFactory {

    public static ContactRepository create() {
        try {
            return new ContactRepository(loadContacts(SkypeHolder.getSkype()));
        } catch (ConnectionException e) {
            throw new RuntimeException(e);
        }
    }

    private static Map<String, ContactWithBDay> loadContacts(final Skype skype) throws ConnectionException {
        final Contacts contacts = BirthdayBotSettings.getInstance().getConfiguration().getContacts();

        final List<Contact> contactList = contacts.getContact();

        final Map<String, ContactWithBDay> result = new HashMap<String, ContactWithBDay>(contactList.size());
        for (final Contact contact : contactList) {
            final String skypeId = contact.getSkype();
            final DateTime bDay = getDate(contact.getBDay());
            final String topicName = contact.getTopicName();

            com.samczsun.skype4j.user.Contact skypeContact = skype.getOrLoadContact(skypeId);

            final ContactWithBDay contactWithBDay = new ContactWithBDay(skypeContact, bDay, topicName);

            result.put(contactWithBDay.getUsername(), contactWithBDay);
        }

        return result;
    }

    private static DateTime getDate(XMLGregorianCalendar bDay) {
        return new DateTime(bDay.getYear(), bDay.getMonth(), bDay.getDay(), 0, 0);
    }
}