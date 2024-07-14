package io.ytka.packagerestriction.detektext.rules

import io.gitlab.arturbosch.detekt.api.*
import org.jetbrains.kotlin.psi.KtFile
import org.jetbrains.kotlin.psi.KtNamedFunction

class PackageRestriction(config: Config) : Rule(config) {
    override val issue = Issue(javaClass.simpleName,
        Severity.Maintainability,
        "Package restriction violation.",
        Debt.TWENTY_MINS)

    private val threshold = 10
    private var amount: Int = 0

    override fun visitKtFile(file: KtFile) {
        super.visitKtFile(file)

        if (amount > threshold) {
            report(CodeSmell(issue, Entity.from(file),
                "The amount of functions in this file is higher than the threshold of $threshold."))
        }
        amount = 0
    }

    // TODO: calculate cyclomatic complexity
    override fun visitNamedFunction(function: KtNamedFunction) {
        super.visitNamedFunction(function)
        amount++
    }
}
