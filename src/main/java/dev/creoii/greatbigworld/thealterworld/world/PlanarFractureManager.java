package dev.creoii.greatbigworld.thealterworld.world;

import com.mojang.serialization.Codec;
import net.minecraft.core.UUIDUtil;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.SavedDataType;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.UUID;

public class PlanarFractureManager extends SavedData {
    public static final Codec<PlanarFractureManager> CODEC = Codec.unboundedMap(UUIDUtil.AUTHLIB_CODEC, Vec3.CODEC).xmap(uuidVec3dMap -> {
        PlanarFractureManager manager = new PlanarFractureManager();
        manager.entities = new HashMap<>(uuidVec3dMap);
        return manager;
    }, planarFractureManager -> planarFractureManager.entities);
    private static final SavedDataType<PlanarFractureManager> STATE_TYPE = new SavedDataType<>("gbw_planar_fractures", PlanarFractureManager::new, CODEC, null);
    public HashMap<UUID, Vec3> entities = new HashMap<>();

    @Nullable
    public Vec3 getReturnPos(LivingEntity living) {
        if (living.level().isClientSide())
            return null;
        PlanarFractureManager manager = getServerState(living.level().getServer());
        return manager.entities.computeIfAbsent(living.getUUID(), uuid -> null);
    }

    public void setReturnPos(LivingEntity living, Vec3 pos) {
        entities.put(living.getUUID(), pos);
    }

    public void clearReturnPos(LivingEntity living) {
        entities.remove(living.getUUID());
    }

    public static PlanarFractureManager getServerState(MinecraftServer server) {
        PlanarFractureManager manager = server.getLevel(Level.OVERWORLD).getDataStorage().computeIfAbsent(STATE_TYPE);
        manager.setDirty();
        return manager;
    }
}

