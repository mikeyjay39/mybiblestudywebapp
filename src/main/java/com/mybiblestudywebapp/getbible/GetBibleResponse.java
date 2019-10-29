package com.mybiblestudywebapp.getbible;

import java.util.List;
import java.util.Map;

/**
 * Created by Michael Jeszenka.
 * <a href="mailto:michael@jeszenka.com">michael@jeszenka.com</a>
 * 10/21/19
 */
public interface GetBibleResponse {

    /**
     * Return a list of all the verses
     * @return Ordered list of the verses. Keys are verseNr and verse
     */
    List<Map<String, String>> getVerses();
}
