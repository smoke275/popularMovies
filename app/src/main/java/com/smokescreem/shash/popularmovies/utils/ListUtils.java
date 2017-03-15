package com.smokescreem.shash.popularmovies.utils;

/**
 * Created by Shash on 1/22/2017.
 */

import java.util.Collection;


public final class ListUtils {

    public static <E> boolean isEmpty(Collection<E> list) {
        return (list == null || list.size() == 0);
    }

}