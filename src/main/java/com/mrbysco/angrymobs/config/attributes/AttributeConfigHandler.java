package com.mrbysco.angrymobs.config.attributes;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mrbysco.angrymobs.AngryMobs;
import net.minecraftforge.fml.loading.FMLPaths;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AttributeConfigHandler {
	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
	public static final File ANGRYMOBS_FOLDER = new File(FMLPaths.CONFIGDIR.get().toFile() + "/angrymobs");
	public static final File ANGRY_FILE = new File(ANGRYMOBS_FOLDER, "angrymobs_attributes.json");

	public static final Map<String, AdditionValues> additionMap = new HashMap<>();
	private static boolean isLoaded = false;

	public static void initializeConfig() {
		if (!ANGRYMOBS_FOLDER.exists() || !ANGRY_FILE.exists()) {
			ANGRYMOBS_FOLDER.mkdirs();

			AttributeConfig attributeConfig = new AttributeConfig(List.of(new AttributeAddition("insert:mob_here", "generic.attack_damage", 1.0D)));
			try (FileWriter writer = new FileWriter(ANGRY_FILE)) {
				GSON.toJson(attributeConfig, writer);
				writer.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void loadConfig() {
		initializeConfig();

		//Only load the config once (Just to be safe)
		if (!isLoaded) {
			isLoaded = true;

			additionMap.clear();
			String fileName = ANGRY_FILE.getName();
			try (FileReader json = new FileReader(ANGRY_FILE)) {
				final AttributeConfig initialConfig = GSON.fromJson(json, AttributeConfig.class);
				if (initialConfig != null) {
					for (AttributeAddition addition : initialConfig.additionList()) {
						if (addition.entity().equals("insert:mob_here") || addition.entity().isEmpty()) {
							continue;
						}
						additionMap.put(addition.entity(), new AdditionValues(addition.attribute(), addition.value()));
					}
				} else {
					AngryMobs.LOGGER.error("Could not load Angry Mobs' attribute config from {}.", fileName);
				}
			} catch (final Exception e) {
				AngryMobs.LOGGER.error("Unable to load file {}. Please make sure it's a valid json.", fileName);
				AngryMobs.LOGGER.trace("Exception: ", e);
			}
		}
	}

	public static record AdditionValues(String attribute, double value) {
	}
}
