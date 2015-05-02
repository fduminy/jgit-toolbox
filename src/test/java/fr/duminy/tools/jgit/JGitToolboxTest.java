/**
 * JGitToolbox is a set of tools on top of JGit.
 * <p/>
 * Copyright (C) 2015-2015 Fabien DUMINY (fabien [dot] duminy [at] webmails [dot] com)
 * <p/>
 * JGitToolbox is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.
 * <p/>
 * JGitToolbox is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p/>
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301,
 * USA.
 */
package fr.duminy.tools.jgit;

import org.apache.commons.io.IOUtils;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.revwalk.RevCommit;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

public class JGitToolboxTest {
    private static int COUNTER = 1;
    private static final Logger LOGGER = LoggerFactory.getLogger(JGitToolboxTest.class);

    @Rule
    public final TemporaryFolder folder = new TemporaryFolder();

    @Test
    public void testTrack() throws Exception {
        // prepare
        Git sourceGit = createGitRepository("source");
        Git targetGit = cloneRepository(sourceGit);
        addAndCommitFile(sourceGit);
        JGitToolbox toolbox = new JGitToolbox();
        Parameters parameters = new Parameters();
        parameters.setGitDirectory(targetGit.getRepository().getDirectory());

        // test
        String targetHead = toolbox.track(parameters);

        // verify
        log(sourceGit);
        log(targetGit);
        assertThat(targetHead).isEqualTo(getHead(sourceGit).getName());
    }

    @Test(expected = GitToolboxException.class)
    public void testTrack_mergeConflict() throws Exception {
        // prepare
        Git sourceGit = createGitRepository("source");
        Git targetGit = cloneRepository(sourceGit);
        addAndCommitFile(sourceGit);
        addAndCommitFile(targetGit);
        JGitToolbox toolbox = new JGitToolbox();
        Parameters parameters = new Parameters();
        parameters.setGitDirectory(targetGit.getRepository().getDirectory());

        // test
        toolbox.track(parameters);

        // verify
        log(sourceGit);
        log(targetGit);
    }

    private static Ref getHead(Git targetGit) throws IOException {
        return targetGit.getRepository().getRef(Constants.HEAD);
    }

    private Git cloneRepository(Git sourceGit) throws GitAPIException, IOException {
        final String sourceURL = sourceGit.getRepository().getDirectory().getParentFile().toURI().toString();
        return Git.cloneRepository().setURI(sourceURL).setDirectory(folder.newFolder("target")).call();
    }

    private Git createGitRepository(String name) throws IOException, GitAPIException {
        File dir = folder.newFolder(name);

        Git git = Git.init().setDirectory(dir).call();
        addAndCommitFile(git);
        return git;
    }

    private static String addAndCommitFile(Git git) throws IOException, GitAPIException {
        File gitDirectory = git.getRepository().getDirectory().getParentFile();
        String filename = "file" + COUNTER++;
        IOUtils.write(filename + " content", new FileOutputStream(new File(gitDirectory, filename)));
        git.add().addFilepattern(filename).call();
        RevCommit call = git.commit().setMessage("add " + filename).call();
        String sha1 = call.getName();
        LOGGER.info("{}: Added file {} (sha1: {})", gitDirectory.getName(), filename, sha1);
        return sha1;
    }

    private static void log(Git git) throws GitAPIException {
        File gitDirectory = git.getRepository().getDirectory().getParentFile();
        LOGGER.info("Log for {}", gitDirectory.getName());
        for (RevCommit commit : git.log().call()) {
            LOGGER.info("{} {}", commit.getName(), commit.getFullMessage());
        }
    }
}