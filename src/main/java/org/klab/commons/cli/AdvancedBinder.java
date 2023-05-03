/*
 * Copyright (c) 2007 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package org.klab.commons.cli;

import java.io.File;
import java.lang.reflect.Field;

import vavi.beans.BeanUtil;
import vavi.beans.DefaultBinder;


/**
 * フィールドへの代入を行う拡張クラスです。
 *
 * <li> TODO もっと充実させる
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 070224 nsano initial version <br>
 */
public class AdvancedBinder extends DefaultBinder {

    /**
     * {@link java.io.File} 型のフィールドも {@link Binder} 無しで設定できます。
     * boolean, Boolean クラスの場合、呼ばれただけで true, Boolean.True に設定されます。(Option が無かった場合呼ばれないため)
     */
    @SuppressWarnings("unchecked")
    public void bind(Object destBean, Field field, Class<?> fieldClass, String value, Object elseValue) {
        if (fieldClass.equals(Boolean.class)) {
            BeanUtil.setFieldValue(field, destBean, value == null || value.length() == 0 ? Boolean.TRUE : Boolean.parseBoolean(value));
        } else if (fieldClass.equals(Boolean.TYPE)) {
            BeanUtil.setFieldValue(field, destBean, value == null || value.length() == 0 ? true : Boolean.parseBoolean(value));
        } else if (fieldClass.equals(File.class)) {
            BeanUtil.setFieldValue(field, destBean, value == null || value.length() == 0 ? null : new File(value));
        } else if (fieldClass.isEnum()) {
            BeanUtil.setFieldValue(field, destBean, value == null || value.length() == 0 ? null : Enum.valueOf((Class<? extends Enum>) fieldClass, value));
        } else {
            super.bind(destBean, field, fieldClass, value, elseValue);
        }
    }
}

/* */
