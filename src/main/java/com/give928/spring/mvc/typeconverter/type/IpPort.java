package com.give928.spring.mvc.typeconverter.type;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@AllArgsConstructor
@Getter
@EqualsAndHashCode
public class IpPort {
    private String ip;
    private int port;
}
