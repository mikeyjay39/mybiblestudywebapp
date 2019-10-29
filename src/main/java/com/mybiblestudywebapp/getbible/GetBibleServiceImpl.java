package com.mybiblestudywebapp.getbible;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * Created by Michael Jeszenka.
 * <a href="mailto:michael@jeszenka.com">michael@jeszenka.com</a>
 * 10/21/19
 */
@Service
public class GetBibleServiceImpl implements GetBibleService {

    private GetBible getBible = new GetBibleImpl();

    /**
     * {@inheritDoc}
     * @param book
     * @param chapter
     * @return
     */
    @Async
    public CompletableFuture<List<Map<String, String>>> getVersesForChapter(String book, int chapter)  {
        return CompletableFuture.completedFuture(getBible.getVersesForChapter(book, chapter).getVerses());
    }
}
