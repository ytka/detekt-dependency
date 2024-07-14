package io.ytka.detekt.dependency.extension

import io.gitlab.arturbosch.detekt.api.Config
import io.gitlab.arturbosch.detekt.api.ConfigValidator
import io.gitlab.arturbosch.detekt.api.Notification

class DependencyConfigValidator : ConfigValidator {

    override val id: String = "DependencyConfigValidator"

    override fun validate(config: Config): Collection<Notification> {
        val result = mutableListOf<Notification>()
        runCatching {
            config.subConfig("dependency")
                .subConfig("PackageImport")
                .valueOrNull<Boolean>("active")
        }.onFailure {
            result.add(Message("'active' property must be of type boolean."))
        }
        return result
    }
}

class Message(
    override val message: String,
    override val level: Notification.Level = Notification.Level.Error
) : Notification
