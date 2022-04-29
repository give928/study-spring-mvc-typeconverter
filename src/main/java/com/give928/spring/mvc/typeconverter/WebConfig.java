package com.give928.spring.mvc.typeconverter;

import com.give928.spring.mvc.typeconverter.converter.IpPortToStringConverter;
import com.give928.spring.mvc.typeconverter.converter.StringToIpPortConverter;
import com.give928.spring.mvc.typeconverter.formatter.MyNumberFormatter;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addFormatters(FormatterRegistry registry) {
//        registry.addConverter(new StringToIntegerConverter());
//        registry.addConverter(new IntegerToStringConverter());
        registry.addConverter(new StringToIpPortConverter());
        registry.addConverter(new IpPortToStringConverter());

        registry.addFormatter(new MyNumberFormatter());
    }
}
