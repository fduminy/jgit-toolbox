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

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;

import java.io.PrintStream;

public class JGitToolboxCommand {
    public static void main(String[] args) {
        new JGitToolboxCommand(new JGitToolbox()).execute(args);
    }

    private final JGitToolbox toolbox;

    JGitToolboxCommand(JGitToolbox toolbox) {
        this.toolbox = toolbox;
    }

    void execute(String... args) {
        Parameters parameters = new Parameters();
        CmdLineParser parser = new CmdLineParser(parameters);

        try {
            parser.parseArgument(args);
            toolbox.track(parameters);
        } catch (CmdLineException e) {
            logError(e);
            printUsage(parser, System.err);
        } catch (GitToolboxException e) {
            logError(e);
        }
    }

    void logError(Exception e) {
        System.err.println(e.getMessage());
        System.exit(1);
    }

    void printUsage(CmdLineParser parser, PrintStream out) {
        parser.printUsage(out);
    }
}
