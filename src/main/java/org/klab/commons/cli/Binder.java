/*
 * Copyright (c) 2007 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package org.klab.commons.cli;


/**
 * フィールドへの複雑な代入を実装します。 
 *
 * @author <a href="mailto:vavivavi@yahoo.co.jp">Naohide Sano</a> (nsano)
 * @version 0.00 070224 nsano initial version <br>
 */
public interface Binder {

    /** コマンドライン引数解析の状態を表すクラスです。 */
    public abstract class Context {
        /** 指定した {@link Option#option} が存在しているかどうか。 */
        public abstract boolean hasOption(String option);
    }

    /** ここで代入を実装します。 */
    void bind(Object bean, String arg, Context context);
}

/* */
