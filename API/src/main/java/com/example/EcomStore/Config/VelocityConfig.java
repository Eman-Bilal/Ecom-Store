package com.example.EcomStore.Config;

import org.apache.velocity.app.VelocityEngine;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

@Configuration
public class VelocityConfig {

  @Bean
  public VelocityEngine velocityEngine(){
    Properties props= new Properties();
    props.setProperty("resource.loaders", "classpath");
    props.setProperty("resource.loader.classpath.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
    VelocityEngine velocityEngine= new VelocityEngine();
    velocityEngine.init(props);
    return velocityEngine;
  }


}
