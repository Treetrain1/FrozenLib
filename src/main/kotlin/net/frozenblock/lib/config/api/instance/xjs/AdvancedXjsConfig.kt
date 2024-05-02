package net.frozenblock.lib.config.api.instance.xjs

import java.nio.file.Files;
import java.nio.file.Path;
import kotlin.reflect.KClass

open class AdvancedXjsConfig<T>(
    modId: String,
    config: KClass<T>,
    format: XjsFormat = XjsFormat.DJS_FORMATTED,
    path: Path = makePath(modId, format.serializedName),
    supportsModification: Boolean = true,
    private val typedEntryFunc: CustomTypedEntryFunction
) : XjsConfig<T>(
    modId,
    config.java,
    path,
    type,
    supportsModification
) {

    @Throws(Exception::class)
    override fun onLoad(): Boolean {
        if (Files.exists(this.path())) {
            this.setConfig(XjsObjectMapper.deserializeObject(this.modId(), typedEntryFunc, this.path(), this.configClass()))
        }
        return true
    }
}