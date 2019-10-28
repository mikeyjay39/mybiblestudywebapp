package com.mybiblestudywebapp.getbible;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger logger = LoggerFactory.getLogger(GetBibleServiceImpl.class);

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
