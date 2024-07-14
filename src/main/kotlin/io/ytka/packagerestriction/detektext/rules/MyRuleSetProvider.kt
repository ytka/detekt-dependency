package io.ytka.packagerestriction.detektext.rules

import io.gitlab.arturbosch.detekt.api.Config
import io.gitlab.arturbosch.detekt.api.RuleSet
import io.gitlab.arturbosch.detekt.api.RuleSetProvider

class MyRuleSetProvider : RuleSetProvider {

    override val ruleSetId = "package-restriction"

    override fun instance(config: Config): RuleSet = RuleSet(
        ruleSetId,
        listOf(
            PackageDependencyRule(config),
            PackageCyclomaticComplexRule(config),
        )
    )
}
