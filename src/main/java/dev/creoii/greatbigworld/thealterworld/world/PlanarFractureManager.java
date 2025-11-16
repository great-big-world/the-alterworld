package dev.creoii.greatbigworld.thealterworld.world;

import com.mojang.serialization.Codec;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Uuids;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.PersistentState;
import net.minecraft.world.PersistentStateType;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.UUID;

public class PlanarFractureManager extends PersistentState {
    public static final Codec<PlanarFractureManager> CODEC = Codec.unboundedMap(Uuids.CODEC, Vec3d.CODEC).xmap(uuidVec3dMap -> {
        PlanarFractureManager manager = new PlanarFractureManager();
        manager.entities = new HashMap<>(uuidVec3dMap);
        return manager;
    }, planarFractureManager -> planarFractureManager.entities);
    private static final PersistentStateType<PlanarFractureManager> STATE_TYPE = new PersistentStateType<>("gbw_planar_fractures", PlanarFractureManager::new, CODEC, null);
    public HashMap<UUID, Vec3d> entities = new HashMap<>();

    @Nullable
    public Vec3d getReturnPos(LivingEntity living) {
        if (living.getEntityWorld().isClient())
            return null;
        PlanarFractureManager manager = getServerState(living.getEntityWorld().getServer());
        return manager.entities.computeIfAbsent(living.getUuid(), uuid -> null);
    }

    public void setReturnPos(LivingEntity living, Vec3d pos) {
        entities.put(living.getUuid(), pos);
    }

    public void clearReturnPos(LivingEntity living) {
        entities.remove(living.getUuid());
    }

    public static PlanarFractureManager getServerState(MinecraftServer server) {
        PlanarFractureManager manager = server.getWorld(World.OVERWORLD).getPersistentStateManager().getOrCreate(STATE_TYPE);
        manager.markDirty();
        return manager;
    }
}

