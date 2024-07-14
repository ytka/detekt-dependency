package io.ytka.detekt.dependency.extension

import io.gitlab.arturbosch.detekt.api.Config
import io.gitlab.arturbosch.detekt.api.RuleSet
import io.gitlab.arturbosch.detekt.api.RuleSetProvider
import io.ytka.detekt.dependency.extension.rules.TooManyFunctions

class DependencyRuleSetProvider : RuleSetProvider {

    override val ruleSetId = "dependency"

    override fun instance(config: Config): RuleSet = RuleSet(
        ruleSetId,
        listOf(
            TooManyFunctions(config),
        )
    )
}
