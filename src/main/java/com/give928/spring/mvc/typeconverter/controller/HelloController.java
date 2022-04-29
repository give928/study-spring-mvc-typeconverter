package com.give928.spring.mvc.typeconverter.controller;

import com.give928.spring.mvc.typeconverter.type.IpPort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@Slf4j
public class HelloController {
    // http://localhost:8080/hello-v1idata=10
    @GetMapping("/hello-v1")
    public String helloV1(HttpServletRequest request) {
        String data = request.getParameter("data"); // 문자 타입 조회
        Integer intValue = Integer.valueOf(data);
        log.info("intValue = {}", intValue);
        return "ok";
    }

    // http://localhost:8080/hello-v2idata=10
    @GetMapping("/hello-v2")
    public String helloV2(@RequestParam Integer data) {
        log.info("data = {}", data);
        return "ok";
    }

    // http://localhost:8080/ip-port?ipPort=127.0.0.1:8080
    @GetMapping("/ip-port")
    public String ipPort(@RequestParam IpPort ipPort) {
        log.info("ipPort.getIp() = {}", ipPort.getIp());
        log.info("ipPort.getPort() = {}", ipPort.getPort());
        return "ok";
    }
}
