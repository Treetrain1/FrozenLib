/*
 * Copyright 2023 FrozenBlock
 * This file is part of FrozenLib.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, see <https://www.gnu.org/licenses/>.
 */

package net.frozenblock.lib.config.api.instance.json;

import blue.endless.jankson.Jankson;
import blue.endless.jankson.JsonGrammar;
import blue.endless.jankson.api.SyntaxError;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import net.frozenblock.lib.FrozenMain;
import net.frozenblock.lib.config.api.instance.Config;
import net.frozenblock.lib.config.api.instance.ConfigSerialization;

/**
 * Serializes and deserializes config data with GSON and Jankson.
 */
public class JsonConfig<T> extends Config<T> {

	public static final String GSON_EXTENSION = "json";
	public static final String JANKSON_EXTENSION = "json5";

	private final Jankson jankson;

	private final boolean json5;

	public JsonConfig(String modId, Class<T> config) {
		this(modId, config, false);
	}

	public JsonConfig(String modId, Class<T> config, boolean json5) {
		this(modId, config, makePath(modId, json5 ? JANKSON_EXTENSION : GSON_EXTENSION), json5);
	}

	public JsonConfig(String modId, Class<T> config, Path path, boolean json5) {
		super(modId, config, path);
		var janksonBuilder = Jankson.builder();

		this.jankson = ConfigSerialization.createJankson(janksonBuilder, modId);
		this.json5 = json5;

		if (this.load()) {
			this.save();
		}
	}

	@Override
	public void save() {
		FrozenMain.LOGGER.info("Saving config {}", this.configClass().getSimpleName());
		try {
			Files.createDirectories(this.path().getParent());
			BufferedWriter writer = Files.newBufferedWriter(this.path(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
			writer.write(this.jankson.toJson(this.config()).toJson(this.json5 ? JsonGrammar.JSON5 : JsonGrammar.STRICT));
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean load() {
		FrozenMain.LOGGER.info("Loading config {}", this.configClass().getSimpleName());
		if (Files.exists(this.path())) {
			try {
				this.setConfig(this.jankson.fromJson(this.jankson.load(this.path().toFile()), this.configClass()));
				return true;
			} catch (IOException | SyntaxError e) {
				e.printStackTrace();
				return false;
			}
		} else {
			return true;
		}
	}
}
