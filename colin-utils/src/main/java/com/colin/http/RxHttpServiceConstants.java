package com.colin.http;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.zhilian.api.ContextUtil;
import com.zhilian.api.InQueryMsg;
import com.zhilian.api.ParaMap;
import com.zhilian.api.RequestUtil;
import com.zhilian.api.Sign;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by sl on 2017-10-17.
 */

public interface RxHttpServiceConstants {
    /**
     * ************************************* 请求参数 ***************************************
     */
//    String BASE_URL = "http://192.168.9.31:8083/";
//    String BASE_URL = "http://192.168.9.124:8080/hzrf-oa";
    String BASE_URL = "http://hzrfoa.vicp.io:25246/hzrf-oa/";// 云服务器
    String URL = null;
    String DOWNLOADURL = BASE_URL + "/File/File/";
    String TYPE_QUERY = "query";
    String TYPE_SAVE = "save";
    int REQUEST_NUM = 5;//请求失败时，重新请求的次数
    int REQUEST_INTERVAL = 1000*3;//请求失败时，重新请求的时间间隔

    /**
     * ************************************* 方法名 ***************************************
     */
    String QUERY_LEAVE_TODO = "getLeaveTodoList";//查询待办理请休假
    String QUERY_LEAVE_DONE = "getLeaveDoneList";//查询已办理请休假
    String QUERY_MY_LEAVE = "getMyLeaveList";// 获取我的请休假列表
    String QUERY_LEAVE_NEW = "";//查询新申请请休假
    String QUERY_LEAVE_DETAIL= "LeaveDetail";//查询新申请请休假
    String QUERY_LEAVE_DAYT = "countWorkingDay";//查询可请假天数
    String QUERY_EGRESS_TODO = "getEgressTodoList";//查询待办外出公干
    String QUERY_EGRESS_DONE = "getEgressDoneList";//查询已办外出公干
    String QUERY_MY_EGRESS = "getMyEgressList";// 获取我的外出公干列表
    String QUERY_EGRESS_DETAIL= "EgressDetail";//查询新申请请休假
    String SAVE_EGRESS= "egresssave";//保存外出报备
    String QUERY_FSONG = "fasong";//查询可请假天数
    String SAVE_OPINION = "editopinion";//意见保存
    String SAVE_LEAVE= "leavesave";//

    /**
     * ************************************* 错误反馈 ***************************************
     */
    String RESPONSE_ERROR = "用户登录超时！";
    String HTTP_SERVER_ERROR = "HTTP 500 Server Error";
    String HTTP_NOT_FOUND = "HTTP 404 Not Found";
    /**
     * ************************************* 视图错误提示 ***************************************
     */
    String LEAVE_DETAIL_ERROR = "获取请休假详情失败";

}
