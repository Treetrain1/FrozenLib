package qsl.internal.json;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import qsl.internal.ProjectConstants;
import org.gradle.api.Project;
import qsl.internal.MinecraftVersion;
import qsl.internal.Versions;
import qsl.internal.dependency.QslLibraryDependency;
import qsl.internal.extension.QslModuleExtension;
import qsl.internal.extension.QslModuleExtensionImpl;

import org.quiltmc.json5.JsonWriter;

public final class QmjBuilder {
	public static void buildFmj(Project project, String version, String loaderVersion, String fabricApiVersion, MinecraftVersion minecraftVersion, QslModuleExtensionImpl ext, Path path) throws IOException {
		JsonWriter writer = JsonWriter.json(path);
		// write everything that is always present
		writer.beginObject()
				.name("schemaVersion").value(1)
				.name("id").value(ext.getId().get())
				.name("version").value(version)
				.name("name").value(ext.getName().get())
				.name("description").value(ext.getDescription().get())
				.name("authors").beginArray() // root object -> authors
				.endArray() // authors -> root object
				.name("contributors").beginArray() // root object -> contributors
				.endArray() // contributors -> root object
				.name("contact").beginObject() // root object -> contact
					.name("homepage").value("https://www.modrinth.com/mod/FrozenLib")
					.name("sources").value("https://github.com/FrozenBlock/FrozenLib")
					.name("issues").value("https://github.com/FrozenBlock/FrozenLib/issues")
				.endObject() // contact -> root object
				.name("license").value(ProjectConstants.LICENSE)
				.name("icon").value("assets/" + ext.getId().get() + "/icon.png");

		writer.name("depends").beginObject()
				.name("fabricloader").value(">=" + loaderVersion)
				.name("fabric-api").value(">=" + fabricApiVersion)
				.name("minecraft");
		// Write Minecraft versions.
		if (Versions.COMPATIBLE_VERSIONS.isEmpty()) {
			writer.value("=" + minecraftVersion.getSemVer());
		} else {
			writer.beginArray()
					.value(minecraftVersion.getSemVer());

			for (var v : Versions.COMPATIBLE_VERSIONS) {
				writer.value(v.getSemVer());
			}

			writer.endArray();
		}

		Map<QslModuleExtension, Project> suggests = new HashMap<>();

		for (QslLibraryDependency depend : ext.getModuleDependencyDefinitions()) {
			for (QslLibraryDependency.ModuleDependencyInfo moduleDependencyInfo : depend.getDependencyInfo().get()) {
				if (moduleDependencyInfo.type() == QslLibraryDependency.ConfigurationType.TESTMOD) {
					continue;
				}

				Project depProject = project.getRootProject().project(depend.getName()).project(moduleDependencyInfo.module());
				QslModuleExtension depExt = depProject.getExtensions().getByType(QslModuleExtension.class);
				if (moduleDependencyInfo.type() == QslLibraryDependency.ConfigurationType.COMPILE_ONLY) {
					suggests.put(depExt, depProject);
				} else {
					writer.name(depExt.getId().get()).value(">=" + depProject.getVersion());
				}
			}
		}

		writer.endObject(); // depends -> root object

		writer.name("suggests").beginObject(); // root object -> suggestion

		for (Map.Entry<QslModuleExtension, Project> suggestedMod : suggests.entrySet()) {
			var depExt = suggestedMod.getKey();
			var depProject = suggestedMod.getValue();

			writer.name(depExt.getId().get()).value(">=" + depProject.getVersion());
		}

		writer.endObject(); // suggests -> root object

		// Provides
		var provides = ext.getProvides();
		if (provides.isPresent() && !provides.get().isEmpty()) {
			writer.name("provides").beginArray(); // root object -> provides

			for (var provide : provides.get()) {
				provide.write(writer);
			}

			writer.endArray(); // provides -> root object
		}

		if (!ext.getEntrypoints().isEmpty()) {
			writer.name("entrypoints").beginObject(); // root object -> entrypoints
			for (QslModuleExtensionImpl.NamedWriteOnlyList entrypoint : ext.getEntrypoints()) {
				writer.name(entrypoint.getName());
				writer.beginArray();
				for (String clazz : entrypoint.getValues().get()) {
					writer.value(clazz);
				}

				writer.endArray();
			}

			writer.endObject(); // entrypoints -> root object
		}

		try {
			writer.name("mixins").beginArray();
		} catch (IOException ignored) {
		}

		if (ext.getHasMixins().get()) {
			//writer.name("mixin/" + ext.getId().get() + ".mixins.json");
		}

		var additionalMixins = ext.getAdditionalMixins().get();
		if (!additionalMixins.isEmpty()) {
			for (String mixin : additionalMixins) {
				System.out.println("added " + mixin);
				writer.name(mixin);
			}
		}

		writer.endArray();

		if (ext.getHasAccessWidener().get()) {
			writer.name("access_widener").value(ext.getId().get() + ".accesswidener");
		}

		// TODO: environment
		if (ext.getEnvironment().get() != Environment.ANY) {
			writer.name("environment").value(ext.getEnvironment().get().qmj);
		}

		writer.name("custom").beginObject(); // root object -> custom

		writer.name("modmenu").beginObject() // custom -> modmenu
				.name("links").beginObject()
					.name("modmenu.discord").value(ProjectConstants.DISCORD)
				.endObject()
				.name("badges").beginArray()
					.value("library")
					.value("frozenblock")
				.endArray()
				.name("parent").beginObject() // modmenu -> parent
				.name("id").value(ProjectConstants.MOD_ID)
				.name("name").value(ProjectConstants.MOD_NAME)
				.name("description").value(ProjectConstants.MOD_DESCRIPTION)
				.name("icon").value("assets/" + ext.getId().get() + "/icon.png")
				.name("badges").beginArray()
					.value("library")
					.value("frozenblock")
				.endArray()
				.endObject() // parent -> modmenu
				.endObject(); // modmenu -> custom

		if (!ext.getInjectedInterfaces().isEmpty()) {
			writer.name("loom:injected_interfaces").beginObject(); // custom -> loom:injected_interfaces

			for (QslModuleExtensionImpl.NamedWriteOnlyList injectedInterface : ext.getInjectedInterfaces()) {
				writer.name(injectedInterface.getName());
				writer.beginArray();

				for (String clazz : injectedInterface.getValues().get()) {
					writer.value(clazz);
				}

				writer.endArray();
			}

			writer.endObject(); // loom:injected_interfaces -> custom
		}

		writer.endObject(); // custom -> root object

		writer.endObject(); // end root object
		writer.flush();
		writer.close();
	}
}
