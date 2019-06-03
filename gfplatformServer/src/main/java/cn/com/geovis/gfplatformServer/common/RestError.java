package cn.com.geovis.gfplatformServer.common;


import com.alibaba.fastjson.JSONObject;
import lombok.Getter;


/**
 * @author 巩志远
 * @create 2018-05-21 19:06:42.
 */
@Getter
public enum  RestError {
    BASE_ERROR("00101","系统运行时异常!"),
    OTHER_ERROR("00102","未知异常!"),
    IO_ERROR("00120","文件IO异常!"),
    CLIENT_TOKEN_ERROR("00403", "客户端token异常!"),
    USER_TOKEN_ERROR("00401", "用户token异常!"),
    DATABASE_ERROR("00157","数据库异常!"),

    BASEIMAGE_LAYER_DELETE_DADABASE_ERROR("01157","插入数据异常!");
    private String code;
    private String message;
    private String reason;
    private RestError(String code, String message) {
        this.code = String.format("007%s",code) ;
        this.message = message;
    }
    public RestError setReason(String reason){
        this.reason = reason;
        return this;
    }
    @Override
    public String toString() {
        JSONObject object = new JSONObject();
        object.put("code",code);
        object.put("message",message);
        object.put("reason",reason);
        return object.toString();
    }
}
