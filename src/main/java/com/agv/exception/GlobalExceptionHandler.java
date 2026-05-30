package com.agv.exception;

import com.agv.util.Result;
import org.apache.ibatis.exceptions.PersistenceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);




    // 1. 业务异常（主动抛出，如参数校验）
    @ExceptionHandler(BusinessException.class)
    public Result handleBusinessException(BusinessException e){
        logger.info("业务异常: {}", e.getMessage());
        return new Result(e.getCode(), e.getMessage(), null);
    }

    // 2. 数据库异常
    @ExceptionHandler({DataAccessException.class, PersistenceException.class})
    public Result handleDatabaseException(Exception e){
        logger.info("数据库异常: {}", e);
        return Result.error("数据库异常，请联系管理员");
    }
    // 3. 空指针异常（开发阶段常见）
    @ExceptionHandler(NullPointerException.class)
    public Result handleNullPointerException(NullPointerException e){
        logger.info("空指针异常: {}", e);
        return Result.error("空指针异常，请联系管理员");
    }

    // 4. 兜底异常（所有未被上面的捕获的异常）
    @ExceptionHandler(Exception.class)
    public Result handleException(Exception e){
        logger.info("兜底异常: {}", e);
        return Result.error("服务器内部错误，请稍后重试");
    }

//    @ExceptionHandler(Exception.class)
//    public Result handleException(Exception e ){
//        e.printStackTrace();
////        return Result.error("系统异常，请联系管理员");
//        return Result.error("服务器内部错误，请稍后重试");
//
//    }
}
