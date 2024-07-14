package io.ytka.package_characteristic.detektext.rules

import io.gitlab.arturbosch.detekt.api.Config
import io.gitlab.arturbosch.detekt.api.RuleSet
import io.gitlab.arturbosch.detekt.api.RuleSetProvider

class MyRuleSetProvider : RuleSetProvider {

    override val ruleSetId = "package-characteristic"

    override fun instance(config: Config): RuleSet = RuleSet(
        ruleSetId,
        listOf(
            PackageCharacteristic(config),
        )
    )
}
