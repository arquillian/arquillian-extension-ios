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
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jboss.arquillian.ios.api.Application;
import org.jboss.arquillian.ios.util.ProcessExecutor;
import org.tmatesoft.svn.core.SVNDepth;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.wc.SVNClientManager;
import org.tmatesoft.svn.core.wc.SVNRevision;
import org.tmatesoft.svn.core.wc.SVNUpdateClient;

/**
 * @author <a href="jpapouse@redhat.com">Jan Papousek</a>
 */
public class IPhoneDriverApplication implements Application {

    public static final String SELENIUM_SVN_REPOSITORY = "http://selenium.googlecode.com/svn";
    public static final String SVN_TRUNK = SELENIUM_SVN_REPOSITORY + "/trunk/";
    private final File location;
    private static final Logger LOG = Logger.getLogger(IPhoneDriverApplication.class.getName());

    public IPhoneDriverApplication() throws IOException {
        this(SVN_TRUNK);
    }

    public IPhoneDriverApplication(File localRepository, String svnRepository) throws IOException {
        this.location = init(svnRepository, localRepository);
    }

    public IPhoneDriverApplication(String svnRepository) throws IOException {
        this.location = init(svnRepository, null);
    }

    public IPhoneDriverApplication(File location) {
        this.location = location;
    }

    public File getLocation() {
        return location;
    }

    private File init(String svnRepository, File target) throws IOException {
        File repository = target;
        try {
            if (repository == null) {
                repository = File.createTempFile("arq-ios", "selenium");
                repository.delete();
            }
            if (!repository.exists()) {
                LOG.log(Level.INFO, "svn checkout <{0}> to <{1}>. It can take a long time.", new Object[]{svnRepository, repository.getAbsolutePath()});
                repository.mkdirs();
                repository.deleteOnExit();
                SVNUpdateClient svn = SVNClientManager.newInstance().getUpdateClient();
                svn.doCheckout(SVNURL.parseURIEncoded(svnRepository), repository, SVNRevision.UNDEFINED, SVNRevision.HEAD, SVNDepth.INFINITY, true);
            }
        } catch (Exception ex) {
            throw new IOException("Can't checkout <" + svnRepository + ">.");
        }
        File app = new File(repository, "iphone" + File.separator + "build" + File.separator + "Release-iphonesimulator" + File.separator + "iWebDriver.app");
        if (!app.exists()) {
            LOG.log(Level.INFO, "<{0}> doesn''t exist, so building selenium.", app.getAbsolutePath());
            List<String> goOutput = ProcessExecutor.execute(repository, "./go", "iphone");
            for (String line : goOutput) {
                System.out.println(line);
            }
            List<String> xcodebuildOutput = ProcessExecutor.execute(new File(repository, "iphone"), "xcodebuild", "clean", "build", "CODE_SIGN_IDENTITY=\"\"", "CODE_SIGNING_REQUIRED=NO", "ONLY_ACTIVE_ARCHS=NO", "-arch", "i386", "-project", "iWebDriver.xcodeproj");
            // FIXME: xcodebuild fails, but iWebDriver is built successfully
//            for (String line : xcodebuildOutput) {
//                System.out.println(line);
//            }
        }
        return app;
    }
}
