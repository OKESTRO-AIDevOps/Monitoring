package com.okestro.symphony.dashboard.cmm.constant;

public class ErrorCodeConstant {

    // Error 코드 정리.
    public static final int ERRCOD_OK = 200;
    public static final int ERRCOD_CREATED = 201;
    public static final int ERRCOD_NO_CONTENT = 204;
    public static final int ERRCOD_BAD_REQUEST =  400;
    public static final int ERRCOD_UNAUTHORIZED = 401;
    public static final int ERRCOD_FORBIDDEN = 403;
    public static final int ERRCOD_NOT_FOUND = 404;
    public static final int ERRCOD_INTERNAL_SERVER_ERROR = 500;
    public static final int ERRCOD_SERVICE_UNAVAILABLE = 503;
    public static final int ERRCOD_DB_ERROR = 600;

    // Error 메시지 정리.
    public static final String ERRMSG_SELECT_SUCCESS = "조회 성공";
    public static final String ERRMSG_SELECT_FAIL = "조회 실패";
    public static final String ERRMSG_NOT_FOUND_USER = "찾을 수 없습니다.";
    public static final String ERRMSG_INTERNAL_SERVER_ERROR = "서버 내부 에러";
    public static final String ERRMSG_DB_CONNT_ERROR = "데이터베이스 접속 에러";
}
