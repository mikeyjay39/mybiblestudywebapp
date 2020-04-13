package com.mybiblestudywebapp.security.xss;

import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

/**
 * Created by Michael Jeszenka.
 * <a href="mailto:michael@jeszenka.com">michael@jeszenka.com</a>
 * 11/11/19
 */
public class RequestWrapper extends HttpServletRequestWrapper {

    public RequestWrapper(HttpServletRequest servletRequest) {
        super(servletRequest);
    }

    public String[] getParameterValues(String parameter) {
        String[] values = super.getParameterValues(parameter);

        if (values == null) {
            return null;
        }

        int count = values.length;
        String[] encodedValues = new String[count];

        for (int i = 0; i < count; i++) {
            encodedValues[i] = cleanXSS(values[i]);
        }

        return encodedValues;
    }

    public String getParameter(String parameter) {
        String value = super.getParameter(parameter);

        if (value == null) {
            return null;
        }

        return cleanXSS(value);
    }

    public String getHeader(String name) {
        String value = super.getHeader(name);

        if (value == null) {
            return null;
        }

        return cleanXSS(value);
    }

    /**
     * Clean up untrusted user input
     *
     * @param value
     * @return
     */
    private String cleanXSS(String value) {
        String filteredValue = Jsoup.clean(value, Whitelist.basic());
        return filteredValue;
    }
}