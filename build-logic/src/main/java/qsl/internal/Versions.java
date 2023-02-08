package qsl.internal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Version constants used across the convention build scripts.
 * <p>
 * To use inside of convention build scripts, simply import this class and refer to the public static final fields.
 */
@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
public final class Versions {
	/*
	 * These must be in a Java class file due to issues with keeping this data in the groovy source set, since the
	 * convention plugins will not be able to see the groovy classes then.
	 *
	 * The gradle.properties does not work here either because you would need to load it in every single project, and it
	 * is not strictly defined in the IDE.
	 */

	/**
	 * The FrozenLib version
	 */
	public static final String FROZENLIB_VERSION = "1.1.11";

	/**
	 * The target Minecraft version.
	 */
	public static final MinecraftVersion MINECRAFT_VERSION = new MinecraftVersion("1.19.2", "1.19.2");

	/**
	 * The Minecraft versions this version of FrozenLib is compatible with.
	 */
	public static final List<MinecraftVersion> COMPATIBLE_VERSIONS = versions();

	/**
	 * The target Quilt Mappings build.
	 */
	public static final int QUILT_MAPPINGS = 21;

	/**
	 * The target Parchment Mappings build.
	 */
	public static final String PARCHMENT_MAPPINGS = "2022.11.27";

	/**
	 * The version of Fabric Loader to use.
	 */
	public static final String LOADER_VERSION = "0.14.13";

	/**
	 * The target Java version.
	 */
	public static final int JAVA_VERSION = 17; // Minecraft is Java 17

	//region 3rd-parties libraries/mods

	public static final String FABRIC_API_VERSION = "0.73.2+1.19.2";

	public static final String MOD_MENU_VERSION = "4.1.1";

	public static final String CLOTH_CONFIG_VERSION = "8.2.88";

	public static final String TERRABLENDER_VERSION = "2.0.1.130";

	// OPTIMIZATION

	public static final String SODIUM_VERISON = "mc1.19.2-0.4.4";

	public static final String IRIS_VERSION = "1.19.x-v1.4.0";

	public static final String INDIUM_VERSION = "1.0.9+mc1.19.2";

	public static final String SODIUM_EXTRA_VERSION = "0.4.10+mc1.19.2-build.64";

	public static final String REESES_SODIUM_OPTIONS_VERSION = "1.4.7+mc1.19.2-build.59";

	public static final String LITHIUM_VERSION = "mc1.19.2-0.10.1";

	public static final String FASTANIM_VERSION = "1.5.6";

	public static final String FERRITECORE_VERSION = "5.0.0-fabric";

	public static final String LAZYDFU_VERSION = "0.1.3";

	public static final String STARLIGHT_VERSION = "1.1.1+1.19";

	public static final String ENTITYCULLING_VERSION = "1.5.2-fabric-1.19";

	public static final String KSYXIS_VERSION = "1.1";

	public static final String MEMORYLEAKFIX_VERSION = "v0.7.0";

	public static final String NO_UNUSED_CHUNKS_VERSION = "v1.4+1.19.2";

	//endregion

	private Versions() {
	}

	private static List<MinecraftVersion> versions(Object... versions) {
		var list = new ArrayList<MinecraftVersion>();

		for (var version : versions) {
			if (version instanceof String name) {
				list.add(new MinecraftVersion(name, MINECRAFT_VERSION.versionEdition()));
			} else if (version instanceof MinecraftVersion mcVersion) {
				list.add(mcVersion);
			} else {
				throw new IllegalArgumentException("Unexpected version \"" + version + "\", only String and MinecraftVersion are accepted.");
			}
		}

		return Collections.unmodifiableList(list);
	}
}
