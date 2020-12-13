package dev.esophose.playerparticles.particles.version;

import dev.esophose.playerparticles.particles.ParticleEffect;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("StaticInitializerReferencesSubClass")
public abstract class VersionMapping {

    public static final VersionMapping _17 = new VersionMapping17();
    public static final VersionMapping _16 = new VersionMapping16();
    public static final VersionMapping _15 = new VersionMapping15();

    private final Map<ParticleEffect, Integer> particleEffectNameMapping;

    public VersionMapping() {
        this.particleEffectNameMapping = new HashMap<>();
    }

    public abstract Map<Integer, ParticleEffect> getParticleEffectIdMapping();

    public Map<ParticleEffect, Integer> getParticleEffectNameMapping() {
        if (this.particleEffectNameMapping.isEmpty())
            this.getParticleEffectIdMapping().forEach((key, value) -> this.particleEffectNameMapping.put(value, key));
        return this.particleEffectNameMapping;
    }

    public static VersionMapping getVersionMapping(int version) {
        switch (version) {
            case 17:
                return _17;
            case 16:
                return _16;
            case 15:
                return _15;
        }

        return new VersionMapping() {
            @Override
            public Map<Integer, ParticleEffect> getParticleEffectIdMapping() {
                return new HashMap<>();
            }
        };
    }

}
