package com.sys.manage.controller;


import cn.hutool.core.codec.Base64Encoder;
import com.google.code.kaptcha.Producer;
import com.sys.manage.common.R;
import com.sys.manage.utils.RedisUtil;
import com.sys.manage.common.Constant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 验证码Controller控制器
 */
@RestController
@Slf4j
public class CaptchaController {

    @Autowired
    private Producer producer;

    @Autowired
    private RedisUtil redisUtil;

    @GetMapping("/captcha")
    public R captcha() throws IOException {
        String key= UUID.randomUUID().toString(); // 生成随机唯一key
        String code = producer.createText();

        log.info("code--->{}",code);

        BufferedImage image = producer.createImage(code);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(image, "jpg", outputStream);

        Base64Encoder encoder = new Base64Encoder();
        String str = "data:image/jpeg;base64,";

        String base64Img = str + encoder.encode(outputStream.toByteArray());

        redisUtil.hset(Constant.CAPTCHA_KEY,key,code,60*5);

        Map<String,Object> resultMap=new HashMap<>();
        resultMap.put("captchaImg",base64Img);
        resultMap.put("uuid",key);
        return R.ok(resultMap);

    }

}
