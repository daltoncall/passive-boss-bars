package com.gosker.aggrobossbars;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.totobirdcreations.mobbossbars.MobBossBar;

import java.util.Map;
import java.util.UUID;
import java.util.WeakHashMap;

/**
 * Mob Boss Bars removes a player from a bar whenever any registered predicate
 * returns true. This predicate returns true while a configured mob has no live
 * attack target, with an optional short linger after combat ends.
 */
public final class AggroBossBarPredicate implements MobBossBar.Predicate {
    private final AggroBossBarsConfig config;

    // Weak keys allow unloaded entities and their cached combat state to be collected.
    // Each entity's state is evaluated at most once per world tick, regardless of how
    // many tracking players Mob Boss Bars checks that tick.
    private final Map<Entity, AggroState> states = new WeakHashMap<>();

    public AggroBossBarPredicate(AggroBossBarsConfig config) {
        this.config = config;
    }

    @Override
    public boolean appliesTo(Entity entity) {
        return entity instanceof MobEntity && config.appliesTo(entity);
    }

    @Override
    public boolean hideFromPlayer(Entity entity, ServerPlayerEntity player) {
        if (!(entity instanceof MobEntity mob)) {
            return false;
        }

        long currentTick = entity.getWorld().getTime();
        AggroState state = states.computeIfAbsent(entity, ignored -> new AggroState());

        if (state.evaluatedTick != currentTick) {
            refreshState(mob, state, currentTick);
        }

        if (!state.visible) {
            return true;
        }

        return shouldHideFromNonTargetedPlayer(player, state.targetPlayerUuid);
    }

    private void refreshState(MobEntity mob, AggroState state, long currentTick) {
        state.evaluatedTick = currentTick;

        LivingEntity target = mob.getTarget();
        if (target != null && target.isAlive()) {
            state.lastAggroTick = currentTick;
            state.targetPlayerUuid = target instanceof ServerPlayerEntity targetPlayer
                    ? targetPlayer.getUuid()
                    : null;
            state.visible = true;
            return;
        }

        if (state.lastAggroTick != Long.MIN_VALUE
                && currentTick - state.lastAggroTick <= config.lingerTicks) {
            state.visible = true;
            return;
        }

        state.targetPlayerUuid = null;
        state.visible = false;
    }

    private boolean shouldHideFromNonTargetedPlayer(ServerPlayerEntity player, UUID targetPlayerUuid) {
        return config.showOnlyToTargetedPlayer
                && targetPlayerUuid != null
                && !targetPlayerUuid.equals(player.getUuid());
    }

    private static final class AggroState {
        private long evaluatedTick = Long.MIN_VALUE;
        private long lastAggroTick = Long.MIN_VALUE;
        private UUID targetPlayerUuid;
        private boolean visible;
    }
}
