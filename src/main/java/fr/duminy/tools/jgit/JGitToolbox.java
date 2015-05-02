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

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.MergeResult;
import org.eclipse.jgit.api.PullCommand;
import org.eclipse.jgit.api.PullResult;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.ProgressMonitor;
import org.eclipse.jgit.lib.TextProgressMonitor;

import static org.eclipse.jgit.api.MergeResult.MergeStatus.ALREADY_UP_TO_DATE;
import static org.eclipse.jgit.api.MergeResult.MergeStatus.FAST_FORWARD;

public class JGitToolbox {
    public String track(Parameters parameters) throws GitToolboxException {
        try {
            Git targetGit = Git.open(parameters.getGitDirectory());
            ProgressMonitor progressMonitor = new TextProgressMonitor();
            PullCommand pullCommand = targetGit.pull().setProgressMonitor(progressMonitor);
            PullResult result = pullCommand.call();
            System.out.println(result);
            if (!result.isSuccessful()) {
                throw new GitToolboxException("Failed to update tracking branch : " + result.toString());
            }

            MergeResult.MergeStatus mergeStatus = result.getMergeResult().getMergeStatus();
            if (!ALREADY_UP_TO_DATE.equals(mergeStatus) && !FAST_FORWARD.equals(mergeStatus)) {
                throw new GitToolboxException("Failed to update tracking branch : " + result.toString());
            }

            return targetGit.getRepository().getRef(Constants.HEAD).getName();
        } catch (Exception e) {
            throw new GitToolboxException("Error while updating tracking branch", e);
        }
    }
}
