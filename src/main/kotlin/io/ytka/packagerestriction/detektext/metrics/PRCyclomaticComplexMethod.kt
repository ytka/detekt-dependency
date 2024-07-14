package io.ytka.packagerestriction.detektext.metrics

import io.gitlab.arturbosch.detekt.api.*
import org.jetbrains.kotlin.psi.*

/**
 * Complex methods are hard to understand and read. It might not be obvious what side-effects a complex method has.
 * Prefer splitting up complex methods into smaller methods that are in turn easier to understand.
 * Smaller methods can also be named much clearer which leads to improved readability of the code.
 *
 * This rule uses McCabe's Cyclomatic Complexity (MCC) metric to measure the number of
 * linearly independent paths through a function's source code (https://www.ndepend.com/docs/code-metrics#CC).
 * The higher the number of independent paths, the more complex a method is.
 * Complex methods use too many of the following statements.
 * Each one of them adds one to the complexity count.
 *
 * - __Conditional statements__ - `if`, `else if`, `when`
 * - __Jump statements__ - `continue`, `break`
 * - __Loops__ - `for`, `while`, `do-while`, `forEach`
 * - __Operators__ `&&`, `||`, `?:`
 * - __Exceptions__ - `catch`, `use`
 * - __Scope Functions__ - `let`, `run`, `with`, `apply`, and `also` ->
 *  [Reference](https://kotlinlang.org/docs/scope-functions.html)
 */
class PRCyclomaticComplexMethod(
    // McCabe's Cyclomatic Complexity (MCC) number for a method.
    private val threshold: Int?,
    // Ignores a complex method if it only contains a single when expression.
    private val ignoreSingleWhenExpression: Boolean = false,
    // Whether to ignore simple (braceless) when entries.
    private val ignoreSimpleWhenEntries: Boolean = false,
    // Whether to ignore functions which are often used instead of an `if` or `for` statement.
    private val ignoreNestingFunctions: Boolean = false,
    // Comma separated list of function names which add complexity.
    private val nestingFunctions: Set<String> = DEFAULT_NESTING_FUNCTIONS.toSet(),
) {

    private val issue = Issue(
        "PRCyclomaticComplexMethod",
        Severity.Maintainability,
        "Prefer splitting up complex methods into smaller, easier to test methods.",
        Debt.TWENTY_MINS
    )



    fun visitNamedFunction(function: KtNamedFunction) : CodeSmell? {
        if (threshold == null) {
            return null
        }
        if (ignoreSingleWhenExpression && hasSingleWhenExpression(function.bodyExpression)) {
            return null
        }

        val complexity = CyclomaticComplexity.calculate(function) {
            this.ignoreSimpleWhenEntries = this@PRCyclomaticComplexMethod.ignoreSimpleWhenEntries
            this.ignoreNestingFunctions = this@PRCyclomaticComplexMethod.ignoreNestingFunctions
            this.nestingFunctions = this@PRCyclomaticComplexMethod.nestingFunctions
        }

        // println("function: ${function.fqName}, complexity: $complexity")

        if (complexity >= threshold) {
            return ThresholdedCodeSmell(
                    issue,
                    Entity.atName(function),
                    Metric("MCC", complexity, threshold),
                    "The function ${function.nameAsSafeName} appears to be too complex " +
                            "based on Cyclomatic Complexity (complexity: $complexity). " +
                            "Defined complexity threshold for methods is set to '$threshold'"
            )
        }
        return null
    }

    private fun hasSingleWhenExpression(bodyExpression: KtExpression?): Boolean = when {
        bodyExpression is KtBlockExpression && bodyExpression.statements.size == 1 -> {
            val statement = bodyExpression.statements.single()
            statement is KtWhenExpression || statement.returnsWhenExpression()
        }
        // the case where function-expression syntax is used: `fun test() = when { ... }`
        bodyExpression is KtWhenExpression -> true
        else -> false
    }

    private fun KtExpression.returnsWhenExpression() =
        this is KtReturnExpression && this.returnedExpression is KtWhenExpression

    companion object {
        val DEFAULT_NESTING_FUNCTIONS = listOf(
            "also",
            "apply",
            "forEach",
            "isNotNull",
            "ifNull",
            "let",
            "run",
            "use",
            "with",
        )
    }
}
