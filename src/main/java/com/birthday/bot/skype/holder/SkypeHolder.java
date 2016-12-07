package com.birthday.bot.skype.holder;

import com.samczsun.skype4j.Skype;
import com.samczsun.skype4j.exceptions.ConnectionException;
import com.samczsun.skype4j.exceptions.InvalidCredentialsException;
import com.samczsun.skype4j.exceptions.NotParticipatingException;

/**
 * Holder for skype instance
 * Created by Vsevolod Kaimashnikov on 05.03.2016.
 */
public class SkypeHolder {

    private static Skype instance;

    public static void setInstance(final Skype skype) {
        synchronized (SkypeHolder.class) {
            instance = skype;
        }
    }

    public static Skype getSkype() {
        return instance;
    }

    public static void relogin() {
        try {
            instance.logout();
            Thread.sleep(10 * 1000/* 10 seconds*/);
            instance.login();
        } catch (
                InterruptedException
                        | ConnectionException
                        | InvalidCredentialsException
                        | NotParticipatingException e
        ) {
            System.out.println("Relogin of skype is failed");
            e.printStackTrace();
            System.exit(0);
        }
    }
}
