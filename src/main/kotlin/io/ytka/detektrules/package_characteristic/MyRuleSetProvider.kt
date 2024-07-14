package io.ytka.detektrules.package_characteristic

import io.gitlab.arturbosch.detekt.api.Config
import io.gitlab.arturbosch.detekt.api.RuleSet
import io.gitlab.arturbosch.detekt.api.RuleSetProvider
import io.ytka.detektrules.package_characteristic.rules.TooManyFunctions

class MyRuleSetProvider : RuleSetProvider {

    override val ruleSetId = "package-characteristic"

    override fun instance(config: Config): RuleSet = RuleSet(
        ruleSetId,
        listOf(
            TooManyFunctions(config),
        )
    )
}
