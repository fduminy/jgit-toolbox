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

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import static org.mockito.Mockito.*;

public class JGitToolboxCommandTest {
    @Rule
    public final TemporaryFolder folder = new TemporaryFolder();

    @Test
    public void testExecute() throws Exception {
        // prepare
        JGitToolbox toolbox = mock(JGitToolbox.class);
        JGitToolboxCommand command = new JGitToolboxCommand(toolbox);

        // test
        command.execute("-dir", folder.newFolder().getAbsolutePath());

        // verify
        verify(toolbox, times(1)).track(any(Parameters.class));
        verifyNoMoreInteractions(toolbox);
    }
}