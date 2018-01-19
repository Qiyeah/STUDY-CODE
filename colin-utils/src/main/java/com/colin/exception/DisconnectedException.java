package com.colin.exception;


import com.colin.constant.Constants;
import com.colin.utils.LogUtil;

/**
 * Created by zhilian on 2018/1/8.
 */

public class DisconnectedException extends Exception {
    @Override
    public String getMessage() {
        return Constants.RESPONSE_ERROR;
    }

    @Override
    public void printStackTrace() {
        LogUtil.e(getMessage());
    }
}
