/*
 * External Code Formatter
 * Copyright (c) 2007-2008 Esko Luontola, www.orfjackal.net
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.orfjackal.extformatter.plugin.util;

import com.intellij.openapi.command.CommandProcessor;
import com.intellij.openapi.command.UndoConfirmationPolicy;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author Esko Luontola
 * @since 12.12.2007
 */
public class CommandRunner implements Runnable {

    @NotNull private final Project project;
    @NotNull private final Runnable runnable;
    @NotNull private final String commandName;
    @Nullable private final Object groupId;
    @NotNull private final UndoConfirmationPolicy undoConfirmationPolicy;

    public CommandRunner(@NotNull Project project, @NotNull Runnable runnable, @NotNull String commandName,
                         @Nullable Object groupId, @NotNull UndoConfirmationPolicy undoConfirmationPolicy) {
        this.project = project;
        this.runnable = runnable;
        this.commandName = commandName;
        this.groupId = groupId;
        this.undoConfirmationPolicy = undoConfirmationPolicy;
    }

    public void run() {
        CommandProcessor.getInstance().executeCommand(project, runnable, commandName, groupId, undoConfirmationPolicy);
    }
}