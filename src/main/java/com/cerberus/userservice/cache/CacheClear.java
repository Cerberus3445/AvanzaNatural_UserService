package com.cerberus.userservice.cache;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CacheClear {

    @CacheEvict(value = "user", key = "#id")
    public void clearUserById(Long id){ //check usages
        log.info("clearUser");
    }
}
