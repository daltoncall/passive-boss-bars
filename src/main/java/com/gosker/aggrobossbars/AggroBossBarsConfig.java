package com.gosker.aggrobossbars;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.Entity;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public final class AggroBossBarsConfig {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path PATH = FabricLoader.getInstance()
            .getConfigDir()
            .resolve("aggro-boss-bars.json");

    /**
     * When false, the predicate affects every MobEntity for which Mob Boss Bars
     * has an enabled bar. When true, only namespaces and exact IDs below match.
     */
    public boolean restrictToListedEntities = false;

    /** Mod namespaces to include when restrictToListedEntities is true. */
    public List<String> entityNamespaces = new ArrayList<>();

    /** Exact entity IDs to include when restrictToListedEntities is true. */
    public List<String> entityIds = new ArrayList<>();

    /** Exact IDs that should never be changed by this add-on. */
    public List<String> excludedEntityIds = new ArrayList<>(List.of("minecraft:ender_dragon"));

    /**
     * Keep a bar visible this many ticks after the mob loses its target.
     * 20 ticks = roughly one second at normal server speed.
     */
    public int lingerTicks = 40;

    /**
     * false: everyone tracking the aggressive mob sees the bar.
     * true: if the target is a player, only that targeted player sees it.
     */
    public boolean showOnlyToTargetedPlayer = false;

    public static AggroBossBarsConfig load() {
        if (Files.notExists(PATH)) {
            AggroBossBarsConfig config = new AggroBossBarsConfig();
            config.save();
            return config;
        }

        try (Reader reader = Files.newBufferedReader(PATH)) {
            AggroBossBarsConfig config = GSON.fromJson(reader, AggroBossBarsConfig.class);
            if (config == null) {
                throw new IOException("Config file contained no JSON object");
            }
            config.normalize();
            return config;
        } catch (Exception exception) {
            AggroBossBars.LOGGER.error(
                    "Could not read {}. Defaults will be used; the broken file was left untouched.",
                    PATH,
                    exception
            );
            return new AggroBossBarsConfig();
        }
    }

    public boolean appliesTo(Entity entity) {
        Identifier id = Registries.ENTITY_TYPE.getId(entity.getType());
        String fullId = id.toString();

        if (excludedEntityIds.contains(fullId)) {
            return false;
        }

        if (!restrictToListedEntities) {
            return true;
        }

        return entityNamespaces.contains(id.getNamespace()) || entityIds.contains(fullId);
    }

    private void normalize() {
        if (entityNamespaces == null) {
            entityNamespaces = new ArrayList<>();
        }
        if (entityIds == null) {
            entityIds = new ArrayList<>();
        }
        if (excludedEntityIds == null) {
            excludedEntityIds = new ArrayList<>();
        }
        lingerTicks = Math.max(0, lingerTicks);
    }

    private void save() {
        try {
            Files.createDirectories(PATH.getParent());
            try (Writer writer = Files.newBufferedWriter(PATH)) {
                GSON.toJson(this, writer);
            }
        } catch (IOException exception) {
            AggroBossBars.LOGGER.error("Could not create default config at {}", PATH, exception);
        }
    }
}
