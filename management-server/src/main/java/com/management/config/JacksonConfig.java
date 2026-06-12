package com.management.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Jackson 配置 —— 全局解决：
 * 1. Long 类型转 String 防止精度丢失（JavaScript 最大安全整数 2^53-1）
 * 2. LocalDateTime 格式化返回
 */
@Configuration
public class JacksonConfig {

    /** 日期时间格式化 Pattern */
    private static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    @Bean
    public MappingJackson2HttpMessageConverter jacksonHttpMessageConverter() {
        ObjectMapper mapper = new ObjectMapper();

        // 注册 Java 8 时间模块
        JavaTimeModule timeModule = new JavaTimeModule();
        // 序列化：LocalDateTime -> String
        timeModule.addSerializer(LocalDateTime.class,
            new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(DATE_TIME_PATTERN)));
        // 反序列化：String -> LocalDateTime
        timeModule.addDeserializer(LocalDateTime.class,
            new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern(DATE_TIME_PATTERN)));
        mapper.registerModule(timeModule);

        // 禁用未知属性报错（前端传多余字段不报错）
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        // 空值不序列化（不返回 null 字段）
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        // 禁用写入时间戳（不用 epoch millis）
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

        // Long 类型序列化为 String 防止精度丢失（JavaScript 最大安全整数 2^53-1）
        mapper.registerModule(new com.fasterxml.jackson.databind.module.SimpleModule("LongToString")
            .addSerializer(Long.class, ToStringSerializer.instance)
            .addSerializer(Long.TYPE, ToStringSerializer.instance));

        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setObjectMapper(mapper);
        return converter;
    }
}