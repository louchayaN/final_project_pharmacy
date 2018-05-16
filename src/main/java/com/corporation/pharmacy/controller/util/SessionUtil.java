package com.corporation.pharmacy.controller.util;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import com.corporation.pharmacy.controller.constant.SessionAttribute;
import com.corporation.pharmacy.entity.LocaleType;

/**
 * Defines static methods for working with the session attributes.
 */
public class SessionUtil {

    /** The default locale is it is not defined by the user */
    private static final LocaleType DEFAULT_LOCALE = LocaleType.RU_BY;

    /**
     * Not instantiate utility class.
     */
    private SessionUtil() {
        throw new AssertionError("Class contains static methods only. You should not instantiate it!");
    }

    /**
     * Checks if user is authenticated. User is considered authenticated if id of
     * user is saved in attributes of the session.
     *
     * @param session
     *            the user session
     * @return {@code true}, if user is authenticated
     */
    public static boolean isAuthenticatedUser(HttpSession session) {
        if (session.getAttribute(SessionAttribute.ID_USER) == null) {
            return false;
        }
        return true;
    }

    /**
     * Returns the locale chosen by the user. If locale is not defined returns
     * default value.
     *
     * @param session
     *            the user session
     * @return the locale chosen by the user. If locale is not defined returns
     *         default value.
     */
    public static LocaleType getLocale(HttpSession session) {
        String locale = (String) session.getAttribute(SessionAttribute.LOCALE);
        LocaleType localeType;
        if (locale != null) {
            localeType = LocaleType.valueOf(locale.toUpperCase());
        } else {
            localeType = DEFAULT_LOCALE;
        }
        return localeType;
    }

    /**
     * Gets the user basket from the session attributes. If the basket is
     * {@code null} returns empty modifiable Map.
     *
     * @param session
     *            the user session
     * @return the user basket. If the basket is {@code null} returns empty
     *         modifiable Map.
     */
    public static Map<Integer, Integer> getBasket(HttpSession session) {
        Map<Integer, Integer> basket = (HashMap<Integer, Integer>) session.getAttribute(SessionAttribute.BASKET);
        if (basket != null) {
            return basket;
        } else {
            return new HashMap<>();
        }
    }

}
