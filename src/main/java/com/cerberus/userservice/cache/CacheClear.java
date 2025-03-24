package com.cerberus.userservice.cache;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CacheClear {

    @CacheEvict(value = "getByEmail", allEntries = true)
    public void clearGetByEmail(){
        log.info("clearGetByEmail");
    }
}
