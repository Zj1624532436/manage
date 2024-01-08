package com.sys.manage.common;

/**
 * JWT 验证时所用的常量
 */
public class JWTConstant {

    /**
     * token
     */
    public static final int JWT_ERRCODE_NULL = 4000;			//Token不存在
    public static final int JWT_ERRCODE_EXPIRE = 4001;			//Token过期
    public static final int JWT_ERRCODE_FAIL = 4002;			//验证不通过

    /**
     * JWT 秘钥
     */
    public static final String JWT_SECRET = "emo=";
    public static final long JWT_TTL = 24 * 60 * 60 * 1000;		//token有效时间
}
