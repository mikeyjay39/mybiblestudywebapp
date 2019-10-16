package com.mybiblestudywebapp.persistence;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Created by Michael Jeszenka
 * <a href="mailto:michael@jeszenka.com">michael@jeszenka.com</a>
 * 2019. 10. 14.
 */
public interface Dao<T> {
    Optional<T> get(long id);

    Optional<T> getUnique(String uniqueKey);

    List<T> getAll();
}
