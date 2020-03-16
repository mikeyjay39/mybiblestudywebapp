package com.mybiblestudywebapp.bibletextservice.getbible;

/**
 * Created by Michael Jeszenka.
 * <a href="mailto:michael@jeszenka.com">michael@jeszenka.com</a>
 * 10/21/19
 */
public interface GetBible {

    /**
     * Call to getbible.net API to fetch verses for the desired chapter
     *
     * @param book    name of the book
     * @param chapter number of the chapter
     * @return GetBibleResponse
     */
    GetBibleResponse getVersesForChapter(String book, int chapter);

}
