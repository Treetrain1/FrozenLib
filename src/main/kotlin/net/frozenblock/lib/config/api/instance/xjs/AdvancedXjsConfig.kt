/*
 * Copyright (C) 2024 FrozenBlock
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

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