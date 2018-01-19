package com.colin.db;


/**
 * Created by sl on 2017-10-17.
 */
public class RxDBManager implements IRxDBManager {

	private static RxDBManager instance = null;

	public synchronized static RxDBManager getInstance() {
		return null == instance ? new RxDBManager() : instance;
	}

	private RxDBManager() {

	}
}
