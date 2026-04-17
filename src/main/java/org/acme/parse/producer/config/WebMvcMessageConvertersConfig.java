package org.acme.parse.producer.config;

import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.xml.Jaxb2RootElementHttpMessageConverter;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcMessageConvertersConfig implements WebMvcConfigurer {

    @Override
    public void extendMessageConverters(@NonNull List<HttpMessageConverter<?>> converters) {
        converters.add(new Jaxb2RootElementHttpMessageConverter());
    }
}
