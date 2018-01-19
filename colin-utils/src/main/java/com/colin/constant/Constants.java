package com.colin.constant;


import com.colin.http.RxHttpServiceConstants;

import java.io.File;

/**
 * Created by Administrator on 2017-12-29.
 */

public interface Constants extends RxHttpServiceConstants {
    /**
     * *********************** 页面中转 ***********************
     */
    int TASK_NEW = 0x101;
    int TASK_TODO = 0x102;
    int TASK_DONE = 0x103;
    /**
     * *********************** 常用符号 ***********************
     */
    String  BLANK = " ";
    String BRACKET1 = "[ ";
    String BRACKET2 = " ]";
    String TAB = "\t";
    String CRLF = "\r\n";
    String CR = "\r";
    String LF = "\n";
    String SEPARATOR = File.separator;
    /**
     * *********************** 资源定义 ***********************
     */
    String[] DAYT_TYPE ={"多天","一天","上午","下午"};



}
