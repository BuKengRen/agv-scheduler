package com.agv.util;

public class Result {
    private Integer code;   // 200成功，其他失败
    private String message; // 提示信息
    private Object data;    // 返回的数据

    // 构造方法私有，通过静态方法创建
    public Result(Integer code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    // 成功（无数据）
    public static Result success() {
        return new Result(200, "success", null);
    }

    // 成功（有数据）
    public static Result success(Object data) {
        return new Result(200, "success", data);
    }

    // 失败
    public static Result error(String message) {
        return new Result(500, message, null);
    }
    public static Result badRequest(String message) {
        return new Result(400, message, null);
    }
    // getter 方法（让 Jackson 能序列化成 JSON）
    public Integer getCode() { return code; }
    public String getMessage() { return message; }
    public Object getData() { return data; }
}