package com.group3.sp25hsf302group3se1889vj.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Setter
@Getter
@ConfigurationProperties("storage")
public class StorageProperties {

    @Value("${storage.root}")
    private String location;

    @Value("${storage.temp}")
    private String tempLocation;

}
