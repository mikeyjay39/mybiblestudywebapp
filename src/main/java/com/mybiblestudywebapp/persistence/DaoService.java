package com.mybiblestudywebapp.persistence;

import java.util.concurrent.CompletableFuture;

/**
 * Created by Michael Jeszenka.
 * <a href="mailto:michael@jeszenka.com">michael@jeszenka.com</a>
 * 10/19/19
 */
public interface DaoService {

    /**
     * Add all notes from a user to a view
     * @param userId
     * @param viewId
     * @return
     */
    CompletableFuture<Long> addUserNotesToView(long userId, long viewId);
}
