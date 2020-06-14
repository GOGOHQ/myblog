package com.hq.myblog.Config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Data
@Component
@ConfigurationProperties(prefix = "comment")
public class AvatarConfig {
    private List<String> avatar = new ArrayList<>();

}
