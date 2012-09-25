/**
 * JBoss, Home of Professional Open Source
 * Copyright 2012, Red Hat Middleware LLC, and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.arquillian.ios.drone.impl;

import java.io.File;
import java.lang.annotation.Annotation;
import org.jboss.arquillian.config.descriptor.api.ArquillianDescriptor;
import org.jboss.arquillian.drone.configuration.ConfigurationMapper;
import org.jboss.arquillian.drone.spi.DroneConfiguration;

/**
 * @author <a href="jpapouse@redhat.com">Jan Papousek</a>
 */
public class IOSDroneConfiguration implements DroneConfiguration<IOSDroneConfiguration> {

    private String localSeleniumCopy;
    private String seleniumSvnRepository = IPhoneDriverApplication.SELENIUM_SVN_REPOSITORY;
    private boolean skip = false;
    private int timeoutInSeconds = 10;
    private boolean verbose;

    public String getConfigurationName() {
        return "ios-drone";
    }

    public File getLocalSeleniumCopy() {
        return localSeleniumCopy == null ? null : new File(localSeleniumCopy);
    }

    public String getSeleniumSvnRepository() {
        return seleniumSvnRepository;
    }

    public int getTimeoutInSeconds() {
        return timeoutInSeconds;
    }

    public boolean isSkip() {
        return skip;
    }

    public boolean isVerbose() {
        return verbose;
    }

    public IOSDroneConfiguration configure(ArquillianDescriptor descriptor, Class<? extends Annotation> qualifier) {
        return ConfigurationMapper.fromArquillianDescriptor(descriptor, this, qualifier);
    }

}
