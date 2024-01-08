package com.sys.manage.utils;

import com.alibaba.druid.util.StringUtils;
import com.sys.manage.common.JWTConstant;
import com.sys.manage.utils.constants.CheckResult;
import io.jsonwebtoken.*;
import org.bouncycastle.util.Strings;
import org.bouncycastle.util.encoders.Base64;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Date;

/**
 * jwt加密和解密的工具类
 */
public class JWTUtil {

    /**
     * 签发JWT;这里创建的jwt
     * @param id
     * @param subject   可以是JSON数据 尽可能少
     * @param ttlMillis
     * @return
     */
    public static String createJWT(String id, String subject, long ttlMillis) {
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        SecretKey secretKey = generalKey();  // 通过操作加密生成key
        JwtBuilder builder = Jwts.builder()
                .setId(id)
                .setSubject(subject)   // 主题
                .setIssuer("zj")// 签发者：zj
                .setIssuedAt(now)      // 签发时间
                .signWith(signatureAlgorithm, secretKey); // 签名算法以及密匙
        if (ttlMillis >= 0) {
            long expMillis = nowMillis + ttlMillis;
            Date expDate = new Date(expMillis);
            builder.setExpiration(expDate); // 过期时间
        }
        return builder.compact();
    }

    /**
     * 生成jwt token
     *
     * @param username
     * @return
     */
    public static String createJWT(String username) {
        return createJWT(username, username, 60 * 60 * 1000); // ttlMillis表示的是一小时
    }

    /**
     * 验证JWT
     * 根据验证时抛出的超时异常、签名异常、其他异常进行一定的操作
     *
     * @param jwtStr
     * @return
     */
    public static CheckResult validateJWT(String jwtStr) {
        CheckResult checkResult = new CheckResult();
        // 如果jwtStr为空的话，设置errcode为jwt不存在
        if(StringUtils.isEmpty(jwtStr)){
            checkResult.setSuccess(false);
            checkResult.setErrCode(JWTConstant.JWT_ERRCODE_NULL);
            return checkResult;
        }
        Claims claims = null;
        try {
            claims = parseJWT(jwtStr);
            checkResult.setSuccess(true);
            checkResult.setClaims(claims);
        } catch (ExpiredJwtException e) {
            checkResult.setErrCode(JWTConstant.JWT_ERRCODE_EXPIRE);
            checkResult.setSuccess(false);
        } catch (SignatureException e) {
            checkResult.setErrCode(JWTConstant.JWT_ERRCODE_FAIL);
            checkResult.setSuccess(false);
        } catch (Exception e) {
            checkResult.setErrCode(JWTConstant.JWT_ERRCODE_FAIL);
            checkResult.setSuccess(false);
        }
        return checkResult;
    }

    /**
     * 生成加密Key
     *
     * @return
     */
    public static SecretKey generalKey() {
        byte[] encodedKey = Base64.decode(JWTConstant.JWT_SECRET);
        SecretKey key = new SecretKeySpec(encodedKey, 0, encodedKey.length, "AES");
        return key;
    }


    /**
     * 解析JWT字符串
     *
     * @param jwt
     * @return 返回 jwt 解析后的 payload
     * @throws Exception
     */
    public static Claims parseJWT(String jwt) {
        SecretKey secretKey = generalKey();
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(jwt)
                .getBody();
    }

    public static void main(String[] args) {
        System.out.println(Strings.fromByteArray(Base64.decode(JWTConstant.JWT_SECRET)));
        String myz = Base64.toBase64String(new String("zj").getBytes());
        System.out.println(myz);
        System.out.println(new String(Base64.decode(myz)));
        String jwt = createJWT("1", "zj", 60 * 60 * 1000);
        System.out.println(jwt);
    }

}