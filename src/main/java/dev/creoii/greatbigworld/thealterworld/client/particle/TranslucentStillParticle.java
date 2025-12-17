package dev.creoii.greatbigworld.thealterworld.client.particle;

import dev.creoii.greatbigworld.thealterworld.util.TranslucentStillParticleOptions;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.client.renderer.state.QuadParticleRenderState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.data.AtlasIds;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;

public class TranslucentStillParticle extends SingleQuadParticle {
    private final Vec3 rotation;

    protected TranslucentStillParticle(ClientLevel clientLevel, double d, double e, double f, double g, double h, double i, TextureAtlasSprite textureAtlasSprite, int lifetime, Vec3 rotation) {
        super(clientLevel, d, e, f, g, h, i, textureAtlasSprite);
        this.lifetime = lifetime;
        hasPhysics = false;
        this.rotation = rotation;
        quadSize = .5f;
    }

    @Override
    public void move(double d, double e, double f) {
    }

    @Override
    public void extract(QuadParticleRenderState quadParticleRenderState, Camera camera, float f) {
        setAlpha((float) (lifetime - age) / lifetime);

        Quaternionf quaternionf = new Quaternionf();

        quaternionf.rotateXYZ((float) Math.toRadians(rotation.x), (float) Math.toRadians(rotation.y), (float) Math.toRadians(rotation.z));

        Vec3 vec3 = camera.position();
        float g = (float)(Mth.lerp(f, xo, x) - vec3.x());
        float h = (float)(Mth.lerp(f, yo, y) - vec3.y());
        float i = (float)(Mth.lerp(f, zo, z) - vec3.z());

        quadParticleRenderState.add(getLayer(), g, h, i, quaternionf.x, quaternionf.y, quaternionf.z, quaternionf.w, getQuadSize(f), getU0(), getU1(), getV0(), getV1(), ARGB.colorFromFloat(alpha, rCol, gCol, bCol), getLightColor(f));
    }

    @Override
    protected Layer getLayer() {
        return Layer.TRANSLUCENT;
    }

    @Environment(EnvType.CLIENT)
    public record Factory() implements ParticleProvider<TranslucentStillParticleOptions> {
        public static final Factory INSTANCE = new Factory();

        public Particle createParticle(TranslucentStillParticleOptions particleOptions, ClientLevel clientLevel, double d, double e, double f, double g, double h, double i, RandomSource randomSource) {
            return new TranslucentStillParticle(clientLevel, d, e, f, g, h, i, Minecraft.getInstance().getAtlasManager().getAtlasOrThrow(AtlasIds.PARTICLES).getSprite(particleOptions.sprite()), particleOptions.lifetime(), particleOptions.rotation());
        }
    }
}
