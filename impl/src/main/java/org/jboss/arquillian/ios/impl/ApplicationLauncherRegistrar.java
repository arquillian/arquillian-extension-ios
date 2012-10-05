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

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jboss.arquillian.config.descriptor.api.ArquillianDescriptor;
import org.jboss.arquillian.core.api.Event;
import org.jboss.arquillian.core.api.InstanceProducer;
import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.core.api.annotation.Observes;
import org.jboss.arquillian.drone.api.annotation.Default;
import org.jboss.arquillian.ios.api.ApplicationLauncher;
import org.jboss.arquillian.ios.spi.event.IOSConfigured;
import org.jboss.arquillian.ios.spi.event.IOSReady;
import org.jboss.arquillian.ios.util.ProcessExecutor;
import org.jboss.arquillian.test.spi.annotation.SuiteScoped;
import org.jboss.arquillian.test.spi.event.suite.AfterSuite;
import org.jboss.arquillian.test.spi.event.suite.BeforeSuite;

/**
 * @author <a href="jpapouse@redhat.com">Jan Papousek</a>
 */
public class ApplicationLauncherRegistrar {

    @Inject
    @SuiteScoped
    private InstanceProducer<ApplicationLauncher> applicationLauncher;
    @Inject
    @SuiteScoped
    private InstanceProducer<IOSConfiguration> configuration;
    @Inject
    private Event<IOSConfigured> configured;
    @Inject
    private Event<IOSReady> ready;

    private static final Logger LOG = Logger.getLogger(ApplicationLauncherRegistrar.class.getName());

    public void configure(@Observes BeforeSuite event, ArquillianDescriptor descriptor) {
        configuration.set(new IOSConfiguration().configure(descriptor, Default.class));
        configured.fire(new IOSConfigured());
    }

    public void register(@Observes IOSConfigured event) throws IOException {
        if (configuration.get().isSkip()) {
            return;
        }
        WaxSim waxSim;
        if (configuration.get().getWaxsimBinary() != null) {
            LOG.log(Level.INFO, "path to waxsim binary has been specified <{0}>", configuration.get().getWaxsimBinary().getAbsolutePath());
            waxSim = new WaxSim(configuration.get().getWaxsimBinary());
        } else {
            LOG.info("path to waxsim binary hasn't been specified");
            waxSim = new WaxSim(configuration.get().getWaxsimGitRepository(), configuration.get().isVerbose());
            LOG.log(Level.INFO, "path to waxsim binary has bee set to <{0}>", waxSim.getBinary().getAbsolutePath());
        }
        applicationLauncher.set(new WaxSimApplicationLauncher(configuration.get(), waxSim.getBinary()));
        ready.fire(new IOSReady());
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                try {
                    ProcessExecutor.execute("killall", "iPhone Simulator");
                } catch (IOException ignored) {
                }
            }
        });
    }

}
