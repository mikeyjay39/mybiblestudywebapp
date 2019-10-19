package com.mybiblestudywebapp.persistence;

import java.util.Map;

/**
 * Created by Michael Jeszenka.
 * <a href="mailto:michael@jeszenka.com">michael@jeszenka.com</a>
 * 10/15/19
 */
public interface UpdatableDao<T> extends Dao<T> {
    long save(T t);

    boolean update(T t);

    boolean delete(T t);
}
