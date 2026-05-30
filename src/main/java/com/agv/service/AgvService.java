package com.agv.service;

import com.agv.config.RabbitMQConfig;
import com.agv.config.RedisConfig;
import com.agv.dto.AgvCreateDTO;
import com.agv.dto.AgvUpdateDTO;
import com.agv.entity.Agv;
import com.agv.exception.BusinessException;
import com.agv.mapper.AgvMapper;
import com.agv.util.Result;
import com.agv.websocket.AgvWebSocketHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;


@Service
public class AgvService {

    @Autowired
    private AgvMapper agvMapper;

    //消息队列
    @Autowired
    private RabbitTemplate rabbitTemplate;

//    @Autowired
//    private RedisTemplate<String,Object> redisTemplate;
//
//    @Autowired
//    private StringRedisTemplate stringRedisTemplate;



    private static final Logger logger = LoggerFactory.getLogger(AgvService.class);

//    private void clearAgvListCache(){
//        // 获取所有 agv:list: 开头的 key
//        Set<String> keys = stringRedisTemplate.keys("agv:list:*");
//        if (keys != null && !keys.isEmpty()){
//            stringRedisTemplate.delete(keys);
//            logger.info("清空了 {} 个缓存", keys.size());
//        }
//    }

    public List<Agv> getAllAgv() {
        return agvMapper.getAllAgv();
    }

    @CacheEvict(value = "agvList", allEntries = true)
    public void updateStatus(Integer id, AgvUpdateDTO agvUpdateDTO) {
        logger.info("修改AGV状态：id={}, 更新字段={}", id, agvUpdateDTO);
        agvMapper.updateStatus(id, agvUpdateDTO);
        logger.info("修改AGV状态成功：id={},", id);

        // 删除所有 agv:list:* 开头的缓存（简单粗暴，但有效）
        // 实际项目可以用更精细的删除策略
        logger.info("删除 AGV 列表缓存");
        // 注意：这里需要注入 StringRedisTemplate 或使用 scan 删除
//        clearAgvListCache();

        String message = "AGV 状态已变更，id=" + id + "，更新内容：" + agvUpdateDTO;
        AgvWebSocketHandler.broadcast( message);
    }

    @CacheEvict(value = {"agvList","agvCount"}, allEntries = true)
    public Result addAgv(AgvCreateDTO agv) {
        logger.info("添加AGV：{}", agv);
        // 1. 参数校验
       if (agv.getName() == null || agv.getName().trim().isEmpty()) {
//            return Result.badRequest("AGV名称不能为空");
           throw new BusinessException("AGV名称不能为空");
        }
        if (agv.getBattery() == null || agv.getBattery() < 0 || agv.getBattery() > 100) {
//            return Result.badRequest("电量必须在0-100之间");
            throw new BusinessException("电量必须在0-100之间");
        }
        String status = agv.getStatus();
        if (status == null || (!status.equals("idle") && !status.equals("working")
                && !status.equals("charging") && !status.equals("offline"))) {
//            return Result.badRequest("状态必须是 idle/working/charging/offline 之一");
            throw new BusinessException("状态必须是 idle/working/charging/offline 之一");
        }

      //1.参数校验
//        if (agv.getName()==null||agv.getName().trim().isEmpty()){
//            return Result.badRequest("AGC名称不能为空");
//        }
//        if (agv.getBattery()==null||agv.getBattery()<0||agv.getBattery()>100){
//            return Result.badRequest("电量必须在0-100之间");
//        }
//
//        if (agv.getStatus()==null||!(agv.getStatus().equals("idle")
//                ||agv.getStatus().equals("working")
//                ||agv.getStatus().equals("charging")||agv.getStatus().equals("offline"))){
//            return Result.badRequest("状态必须是 idle/working/charging/offline 之一");
//        }
        // 2. 校验通过，执行插入
        Agv agv1 = new Agv();
        agv1.setName(agv.getName());
        agv1.setBattery(agv.getBattery());
        agv1.setStatus(agv.getStatus());
        agv1.setLocation(agv.getLocation());

        agvMapper.addAgv(agv1);
        logger.info("添加AGV成功：id={}", agv1.getId());

        // ✅ 发送消息到 RabbitMQ
        String message = "新增AGV：id" + agv1.getId()+" ,agv名称："+agv1.getName();
        rabbitTemplate.convertAndSend(RabbitMQConfig.AGV_QUEUE, message);
//        clearAgvListCache();
        return Result.success();
    }


    @CacheEvict(value = "agvList", allEntries = true)
    public void deleteAgv(Integer id) {
        logger.info("删除AGV：id={}", id);
        agvMapper.deleteAgv(id);
//        logger.info("删除AGV成功：id={}", id);
//        clearAgvListCache();
    }

    @Cacheable(value = "agvList", key = "#offset + ':' + #size + ':' + #name + ':'" +
            " + #status + ':' + #minBattery + ':' + #maxBattery")
    public List<Agv> getAgvList(int offset, int size, String name,String status, Integer minBattery, Integer maxBattery) {
//        // 生成缓存 key（根据查询条件不同，key 不同）
//        String cacheKey = String.format(
//                "agv:list:offset=%d,size=%d,name=%s,status=%s,minBattery=%d,maxBattery=%d",
//                offset, size, name, status, minBattery, maxBattery);
////        String cacheKey = String.format("agv:list:%d:%d:%s:%s:%d:%d",
////                offset, size, name, status, minBattery, maxBattery);
//        // 1. 先从缓存取
//        List<Agv> cachedList  = (List<Agv>) redisTemplate.opsForValue().get(cacheKey);
//        if (cachedList != null) {
//            logger.info("从缓存读取 AGV 列表，key={}", cacheKey);
//            return cachedList;
//        }
//        // 2. 缓存没有，查数据库
//        logger.info("缓存未命中，从数据库查询 AGV 列表");
////        logger.info("查询AGV列表：offset={}, size={}, name={}, status={}, minBattery={}, maxBattery={},"
////            ,offset,size,name,status,minBattery,maxBattery);
//        List<Agv> agvList = agvMapper.getAgvList(offset, size, name, status, minBattery, maxBattery);
//        // 3. 放入缓存（过期时间 60 秒）
//        redisTemplate.opsForValue().set(cacheKey, agvList, 60, TimeUnit.SECONDS);
//        return agvList;


        logger.info("缓存未命中，从数据库查询 AGV 列表");
        return agvMapper.getAgvList(offset, size, name, status, minBattery, maxBattery);
    }

    @Cacheable(value = "agvCount", key = "#name + ':' + #status + ':' + #minBattery + ':' + #maxBattery")
    public Long countAgv(String name,String status, Integer minBattery, Integer maxBattery) {
        logger.info("缓存未命中，从数据库查询 AGV 总数");
        return agvMapper.countAgv(name,status, minBattery, maxBattery);
    }
}
