package com.colin.permissions.dispatcher;

/**
 * Created by zhilian on 2018/1/18.
 */

public interface GrantableRequest extends PermissionRequest {

	void grant();
}

