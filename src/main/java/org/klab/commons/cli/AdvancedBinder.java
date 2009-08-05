/*
 * Copyright (c) 2007 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package org.klab.commons.cli;

import java.io.File;
import java.lang.reflect.Field;

import vavi.beans.BeanUtil;


/**
 * フィールドへの代入を行う拡張クラスです。
 *  
 * <li> TODO もっと充実させる
 * 
 * @author <a href="mailto:vavivavi@yahoo.co.jp">Naohide Sano</a> (nsano)
 * @version 0.00 070224 nsano initial version <br>
 */
public class AdvancedBinder extends DefaultBinder {

    /**
     * {@link java.io.File} 型のフィールドも {@link Binder} 無しで設定できます。
     */
    public void bind(Object destBean, Field field, Class<?> fieldClass, String value, Object elseValue) {
        if (fieldClass.equals(File.class)) {
            BeanUtil.setFieldValue(field, destBean, value == null || value.length() == 0 ? null : new File(value));
        } else {
            super.bind(destBean, field, fieldClass, value, elseValue);
        }
    }
}

/* */
