package com.mybiblestudywebapp.bibletextservice.getbible;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * Created by Michael Jeszenka.
 * <a href="mailto:michael@jeszenka.com">michael@jeszenka.com</a>
 * 10/21/19
 */
public interface GetBibleService {

    /**
     * Get verses for a chapter
     *
     * @param book
     * @param chapter
     * @return
     */
    List<Map<String, String>> getVersesForChapter(String book, int chapter);

}
