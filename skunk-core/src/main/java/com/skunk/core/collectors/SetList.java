package com.skunk.core.collectors;

import java.util.LinkedList;

/**
 * 有序不重复的List集合对象
 *
 * @param <E>
 * @author walker
 * @since 0.0.1
 */
public class SetList<E> extends LinkedList<E> {

    private static final long serialVersionUID = -3471979908988851747L;

    /**
     * 添加对象到集合中
     *
     * @param objct
     * @return
     */
    @Override
    public boolean add(E objct) {
        if (this.modCount == 0) {
            return super.add(objct);
        }
        return contains(objct) ? false : super.add(objct);
    }

}
