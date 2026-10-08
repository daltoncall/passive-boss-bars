package com.gosker.aggrobossbars;

import net.fabricmc.api.ModInitializer;
import net.totobirdcreations.mobbossbars.MobBossBar;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class AggroBossBars implements ModInitializer {
    public static final String MOD_ID = "aggrobossbars";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        AggroBossBarsConfig config = AggroBossBarsConfig.load();
        MobBossBar.addPredicate(new AggroBossBarPredicate(config));

        LOGGER.info(
                "Aggro Boss Bars enabled: linger={} ticks, restrictToListedEntities={}",
                config.lingerTicks,
                config.restrictToListedEntities
        );
    }
}
