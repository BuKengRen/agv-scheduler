package com.agv.mapper;

import com.agv.dto.AgvUpdateDTO;
import com.agv.entity.Agv;
import org.apache.ibatis.annotations.*;

import java.util.List;
@Mapper
public interface AgvMapper {

    @Select("SELECT * FROM agv")
    List<Agv> getAllAgv();
//    @Update("UPDATE agv SET status = #{status} WHERE id = #{id}")
//    void updateStatus(@Param("id") Integer id, @Param("status") String status);
//    @Update("UPDATE agv <set> " +
//            "" +
//            "" +
//            "status = #{status} WHERE id = #{id}")
//    @Update(
//            "update age" +
//            "<set>" +
//            "<if test='status!=null'>status=#{agvUpdateDTO.status},</if>" +
//            "<if test='battery!=null'>battery=#{agvUpdateDTO.battery},</if>" +
//            "<if test='location!=null'>location=#{agvUpdateDTO.location},</if>" +
//            "</set>" +
//            "where id=#{id}"
//            )

    @Update("<script>"
            + "UPDATE agv "
            + "<set>"
            + "<if test='agvUpdateDTO.status != null'>status = #{agvUpdateDTO.status},</if>"
            + "<if test='agvUpdateDTO.battery != null'>battery = #{agvUpdateDTO.battery},</if>"
            + "<if test='agvUpdateDTO.location != null'>location = #{agvUpdateDTO.location},</if>"
            + "</set>"
            + "WHERE id = #{id}"
            + "</script>")

    void updateStatus(@Param("id") Integer id, @Param("agvUpdateDTO") AgvUpdateDTO agvUpdateDTO);

    @Insert("INSERT INTO agv(name, status, battery, location) VALUES(#{name}, #{status}, #{battery}, #{location})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
//    @Options(useGeneratedKeys = true,keyProperty = "id")
    void addAgv(Agv agv);

    @Delete("DELETE FROM agv WHERE id = #{id}")
    void deleteAgv(Integer id);

    /**@Select("""
    <script>
        SELECT * FROM agv
        <where>
            <if test="status != null">AND status = #{status}</if>
            <if test="minBattery != null">AND battery &gt;= #{minBattery}</if>
            <if test="maxBattery != null">AND battery &lt;= #{maxBattery}</if>
        </where>
        LIMIT #{offset}, #{size}
    </script>
    """)
     */
   /**
    * 错误的
    * @Select("<script>" +
            "select * from agv" +
            "<where>" +
//            "<if test=\"name != null and name != ''\">AND name LIKE CONCAT('%', #{name}, '%')</if>" +
            "<if test=\"name != null and name !=''\" > and name like concat('%' ,#{name}, '%')</if>" +
            "<if test='status !=null'> and status=#{status}</if>" +
            "<if test='minBattery!=null'> and battery &gt;=#{minBattery} </if>" +
            "<if test='maxBattery!=null'> and battery &lt;=#{maxBattery} </if>" +
            "</where>" +
            "LIMIT #{offset}, #{size}" +
            "</script>")*/
   @Select("<script>" +
           "select * from agv" +
           "<where>" +
           "<if test='name != null and name != \"\"'> and name like concat('%', #{name}, '%')</if>" +
           "<if test='status != null and status != \"\"'> and status = #{status}</if>" +
           "<if test='minBattery != null'> and battery &gt;= #{minBattery}</if>" +
           "<if test='maxBattery != null'> and battery &lt;= #{maxBattery}</if>" +
           "</where>" +
           " order by id desc" +
           " limit #{offset}, #{size}" +
           "</script>")
   List<Agv> getAgvList(@Param("offset") int offset, @Param("size") int size,
                        @Param("name") String name,
                        @Param("status") String status, @Param("minBattery") Integer minBattery,
                        @Param("maxBattery") Integer maxBattery);

    /**@Select("""
    <script>
        SELECT COUNT(*) FROM agv
        <where>
            <if test="status != null"> AND status = #{status}</if>
            <if test="minBattery != null"> AND battery &gt;= #{minBattery} </if>
            <if test="maxBattery != null"> AND battery &lt;= #{maxBattery} </if>
        </where>
    </script>
    """)
    */


    /**
     * 错误的跟上一个一样
     * @Select("<script>" +
            "select count(*) from agv" +
            "<where>" +
//            "<if test=\"name != null and name != ''\">AND name LIKE CONCAT('%', #{name}, '%')</if>" +
            "<if test=\"name != null and name !='' \"> and name like concat('%',#{name},'%')</if>" +
            "<if test='status!=null'> and status = #{status}</if>" +
            "<if test='minBattery != null' > and battery &gt;= #{minBattery}</if>" +
            "<if test ='maxBattery != null'> and battery &lt;= #{maxBattery}</if>" +
            "</where>" +
            "</script>"
             )*/

    @Select("<script>" +
            "select count(*) from agv" +
            "<where>" +
            "<if test='name != null and name != \"\"'> and name like concat('%', #{name}, '%')</if>" +
            "<if test='status != null and status != \"\"'> and status = #{status}</if>" +
            "<if test='minBattery != null'> and battery &gt;= #{minBattery}</if>" +
            "<if test='maxBattery != null'> and battery &lt;= #{maxBattery}</if>" +
            "</where>" +
            "</script>")
    Long countAgv(@Param("name") String name,
                  @Param("status") String status, @Param("minBattery") Integer minBattery,
                  @Param("maxBattery") Integer maxBattery);

}