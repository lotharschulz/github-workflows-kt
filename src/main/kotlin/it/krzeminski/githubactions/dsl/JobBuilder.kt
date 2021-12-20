package it.krzeminski.githubactions.dsl

import it.krzeminski.githubactions.domain.CommandStep
import it.krzeminski.githubactions.domain.ExternalActionStep
import it.krzeminski.githubactions.domain.Job
import it.krzeminski.githubactions.domain.RunnerType

class JobBuilder(
    val name: String,
    val runsOn: RunnerType,
    val needs: List<Job>,
) {
    private var job = Job(
        name = name,
        runsOn = runsOn,
        steps = emptyList(),
    )

    fun run(
        name: String,
        run: String,
    ): CommandStep {
        val newStep = CommandStep(
            name = name,
            command = run,
        )
        job = job.copy(steps = job.steps + newStep)
        return newStep
    }

    fun uses(
        action: String,
    ): ExternalActionStep {
        val newStep = ExternalActionStep(
            name = name,
            action = action,
        )
        job = job.copy(steps = job.steps + newStep)
        return newStep
    }

    fun build() = job
}