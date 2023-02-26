package com.numble.banking.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AccountAlarmService {

    public void callAlarmService(String loginId, String message) throws InterruptedException {
        Thread.sleep(500);
        log.info("{} 님에게 송금 완료되었습니다. 메시지 : {}", loginId, message);
    }

}
