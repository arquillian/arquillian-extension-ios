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
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.transport.CredentialsProvider;
import org.jboss.arquillian.ios.util.ProcessExecutor;

/**
 * @author <a href="jpapouse@redhat.com">Jan Papousek</a>
 */
public class WaxSim {

    public static final String WAXSIM_GIT_REPOSITORY = "git://github.com/jonathanpenn/WaxSim.git";
    private static final Logger LOG = Logger.getLogger(WaxSim.class.getName());
    private final File binary;

    public WaxSim() throws IOException {
        this(WAXSIM_GIT_REPOSITORY);
    }

    public WaxSim(boolean verbose) throws IOException {
        this(WAXSIM_GIT_REPOSITORY, verbose);
    }

    public WaxSim(File binary) {
        this.binary = binary;
    }

    public WaxSim(String gitRemote) throws IOException {
        this(gitRemote, false);
    }

    public WaxSim(String gitRemote, boolean verbose) throws IOException {
        this.binary = prepareBinary(gitRemote, verbose);
    }

    public File getBinary() {
        return binary;
    }

    private File prepareBinary(String gitRemote, boolean verbose) throws IOException {
        File repository = null;
        try {
            repository = File.createTempFile("arq-ios", "waxsim");
            repository.delete();
            repository.mkdirs();
            repository.deleteOnExit();
            LOG.log(Level.INFO, "git clone <{0}>.", gitRemote);
            Git.cloneRepository().setDirectory(repository).setURI(gitRemote).setCredentialsProvider(CredentialsProvider.getDefault()).call();

        } catch (Exception e) {
            throw new IOException("Can't clone <" + gitRemote + ">", e);
        }
        LOG.info("building waxsim");
        List<String> output = ProcessExecutor.execute(repository, "xcodebuild", "-project", "WaxSim.xcodeproj");
        if (verbose) {
            for (String line : output) {
                System.out.println(line);
            }
        }
        return new File(repository, "build" + File.separator + "Release" + File.separator + "waxsim");
    }
}
