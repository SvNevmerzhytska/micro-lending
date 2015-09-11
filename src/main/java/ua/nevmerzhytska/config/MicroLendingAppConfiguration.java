package ua.nevmerzhytska.config;

import io.dropwizard.Configuration;

public class MicroLendingAppConfiguration extends Configuration {

    private Class springConfiguration = MicroLendingAppSpringConfiguration.class;

    public Class getSpringConfiguration() {
        return springConfiguration;
    }

    public void setSpringConfiguration(Class springConfiguration) {
        this.springConfiguration = springConfiguration;
    }

}