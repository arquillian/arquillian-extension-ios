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
package org.jboss.arquillian.ios.impl;

import java.io.File;
import java.lang.annotation.Annotation;
import org.jboss.arquillian.config.descriptor.api.ArquillianDescriptor;
import org.jboss.arquillian.drone.configuration.ConfigurationMapper;
import org.jboss.arquillian.drone.spi.DroneConfiguration;

/**
 * @author <a href="jpapouse@redhat.com">Jan Papousek</a>
 */
public class IOSConfiguration implements DroneConfiguration<IOSConfiguration> {

    private String family = "iphone";
    private String sdk;
    private boolean skip = false;
    private boolean verbose = false;
    private String waxsimBinary;
    private String waxsimGitRepository = WaxSim.WAXSIM_GIT_REPOSITORY;

    private static final String NAME = "ios";

    public String getFamily() {
        return family;
    }

    public String getSdk() {
        return sdk;
    }

    public boolean isVerbose() {
        return verbose;
    }

    public boolean isSkip() {
        return skip;
    }

    public File getWaxsimBinary() {
        return waxsimBinary == null ? null : new File(waxsimBinary);
    }

    public String getWaxsimGitRepository() {
        return waxsimGitRepository;
    }

    public String getConfigurationName() {
        return NAME;
    }

    public IOSConfiguration configure(ArquillianDescriptor descriptor, Class<? extends Annotation> qualifier) {
        return ConfigurationMapper.fromArquillianDescriptor(descriptor, this, qualifier);
    }

}
