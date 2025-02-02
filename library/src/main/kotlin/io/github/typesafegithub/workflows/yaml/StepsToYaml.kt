package io.github.typesafegithub.workflows.yaml

import io.github.typesafegithub.workflows.domain.CommandStep
import io.github.typesafegithub.workflows.domain.ExternalActionStep
import io.github.typesafegithub.workflows.domain.Shell
import io.github.typesafegithub.workflows.domain.Shell.Bash
import io.github.typesafegithub.workflows.domain.Shell.Cmd
import io.github.typesafegithub.workflows.domain.Shell.Custom
import io.github.typesafegithub.workflows.domain.Shell.PowerShell
import io.github.typesafegithub.workflows.domain.Shell.Pwsh
import io.github.typesafegithub.workflows.domain.Shell.Python
import io.github.typesafegithub.workflows.domain.Shell.Sh
import io.github.typesafegithub.workflows.domain.Step
import io.github.typesafegithub.workflows.domain.actions.fullName

internal fun List<Step>.stepsToYaml(): List<Map<String, Any?>> =
    this.map { it.toYaml() }

private fun Step.toYaml() =
    when (this) {
        is ExternalActionStep<*> -> toYaml()
        is CommandStep -> toYaml()
    }

@Suppress("SpreadOperator")
private fun ExternalActionStep<*>.toYaml(): Map<String, Any?> =
    mapOfNotNullValues(
        "id" to id,
        "name" to name,
        "continue-on-error" to continueOnError,
        "timeout-minutes" to timeoutMinutes,
        "uses" to action.fullName,
        "with" to action.toYamlArguments().ifEmpty { null },
        "env" to env.ifEmpty { null },
        "if" to condition,
    ) + _customArguments

@Suppress("SpreadOperator")
private fun CommandStep.toYaml(): Map<String, Any?> =
    mapOfNotNullValues(
        "id" to id,
        "name" to name,
        "env" to env.ifEmpty { null },
        "continue-on-error" to continueOnError,
        "timeout-minutes" to timeoutMinutes,
        "shell" to shell?.toYaml(),
        "working-directory" to workingDirectory,
        "run" to command,
        "if" to condition,
    ) + _customArguments

private fun Shell.toYaml() =
    when (this) {
        Bash -> "bash"
        Cmd -> "cmd"
        Pwsh -> "pwsh"
        PowerShell -> "powershell"
        Python -> "python"
        Sh -> "sh"
        is Custom -> this.value
    }
