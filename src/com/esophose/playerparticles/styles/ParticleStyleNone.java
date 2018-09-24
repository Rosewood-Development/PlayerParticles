package com.esophose.playerparticles.styles;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.Location;

import com.esophose.playerparticles.particles.ParticleEffect;
import com.esophose.playerparticles.particles.ParticlePair;
import com.esophose.playerparticles.styles.api.PParticle;
import com.esophose.playerparticles.styles.api.ParticleStyle;

public class ParticleStyleNone implements ParticleStyle {

	public List<PParticle> getParticles(ParticlePair particle, Location location) {
		ParticleEffect particleEffect = particle.getEffect();
		List<PParticle> particles = new ArrayList<PParticle>();

		switch (particleEffect) {
		case AMBIENT_ENTITY_EFFECT:
			return Collections.singletonList(new PParticle(location, 0.6F, 0.6F, 0.6F, 0.0F));
		case ANGRY_VILLAGER:
			return Collections.singletonList(new PParticle(location, 0.6F, 0.6F, 0.6F, 0.0F));
		case BARRIER:
			return Collections.singletonList(new PParticle(location, 0.6F, 0.6F, 0.6F, 0.0F));
		case BLOCK:
			return Collections.singletonList(new PParticle(location, 0.6F, 0.6F, 0.6F, 0.0F));
		case BUBBLE:
			return Collections.singletonList(new PParticle(location, 0.4F, 0.4F, 0.4F, 0.0F));
		case BUBBLE_COLUMN_UP:
			return Collections.singletonList(new PParticle(location, 0.4F, 0.4F, 0.4F, 0.0F));
		case BUBBLE_POP:
			return Collections.singletonList(new PParticle(location, 0.4F, 0.4F, 0.4F, 0.0F));
		case CLOUD:
			return Collections.singletonList(new PParticle(location, 0.4F, 0.4F, 0.4F, 0.0F));
		case CRIT:
			return Collections.singletonList(new PParticle(location, 0.4F, 0.4F, 0.4F, 0.0F));
		case CURRENT_DOWN:
			return Collections.singletonList(new PParticle(location, 0.4F, 0.4F, 0.4F, 0.0F));
		case DAMAGE_INDICATOR:
			return Collections.singletonList(new PParticle(location, 0.4F, 0.4F, 0.4F, 0.0F));
		case DOLPHIN:
			return Collections.singletonList(new PParticle(location, 0.4F, 0.4F, 0.4F, 0.0F));
		case DRAGON_BREATH:
			return Collections.singletonList(new PParticle(location, 0.4F, 0.4F, 0.4F, 0.0F));
		case DRIPPING_LAVA:
			return Collections.singletonList(new PParticle(location, 0.6F, 0.6F, 0.6F, 0.0F));
		case DRIPPING_WATER:
			return Collections.singletonList(new PParticle(location, 0.6F, 0.6F, 0.6F, 0.0F));
		case DUST:
			return Collections.singletonList(new PParticle(location, 0.5F, 0.5F, 0.5F, 0.0F));
		case ENCHANT:
			return Collections.singletonList(new PParticle(location, 0.6F, 0.6F, 0.6F, 0.05F));
		case ENCHANTED_HIT:
			return Collections.singletonList(new PParticle(location, 0.4F, 0.4F, 0.4F, 0.0F));
		case END_ROD:
			return Collections.singletonList(new PParticle(location, 0.4F, 0.4F, 0.4F, 0.0F));
		case ENTITY_EFFECT:
			return Collections.singletonList(new PParticle(location, 0.4F, 0.4F, 0.4F, 0.0F));
		case EXPLOSION:
			return Collections.singletonList(new PParticle(location, 0.4F, 0.4F, 0.4F, 0.0F));
		case EXPLOSION_EMITTER:
			return Collections.singletonList(new PParticle(location, 0.4F, 0.4F, 0.4F, 0.0F));
		case FALLING_DUST:
			for (int i = 0; i < 2; i++)
				particles.add(new PParticle(location.add(0, 0.75, 0), 0.6F, 0.4F, 0.6F, 0.0F));
			return particles;
		case FIREWORK:
			return Collections.singletonList(new PParticle(location, 0.4F, 0.4F, 0.4F, 0.0F));
		case FISHING:
			return Collections.singletonList(new PParticle(location, 0.4F, 0.4F, 0.4F, 0.0F));
		case FLAME:
			return Collections.singletonList(new PParticle(location, 0.1F, 0.1F, 0.1F, 0.05F));
		case FOOTSTEP:
			return Collections.singletonList(new PParticle(location, 0.4F, 0.4F, 0.4F, 0.0F));
		case HAPPY_VILLAGER:
			return Collections.singletonList(new PParticle(location, 0.5F, 0.5F, 0.5F, 0.0F));
		case HEART:
			return Collections.singletonList(new PParticle(location, 0.6F, 0.6F, 0.6F, 0.0F));
		case INSTANT_EFFECT:
			return Collections.singletonList(new PParticle(location, 0.4F, 0.4F, 0.4F, 0.0F));
		case ITEM:
			return Collections.singletonList(new PParticle(location, 0.6F, 0.6F, 0.6F, 0.0F));
		case ITEM_SLIME:
			return Collections.singletonList(new PParticle(location, 0.4F, 0.4F, 0.4F, 0.0F));
		case ITEM_SNOWBALL:
			return Collections.singletonList(new PParticle(location, 0.4F, 0.4F, 0.4F, 0.0F));
		case LARGE_SMOKE:
			return Collections.singletonList(new PParticle(location, 0.4F, 0.4F, 0.4F, 0.0F));
		case LAVA:
			return Collections.singletonList(new PParticle(location, 0.4F, 0.4F, 0.4F, 0.0F));
		case MYCELIUM:
			return Collections.singletonList(new PParticle(location, 0.4F, 0.4F, 0.4F, 0.0F));
		case NAUTILUS:
			return Collections.singletonList(new PParticle(location, 0.5F, 0.5F, 0.5F, 0.05F));
		case NONE:
			return particles;
		case NOTE:
			return Collections.singletonList(new PParticle(location, 0.6F, 0.6F, 0.6F, 0.0F));
		case POOF:
			return Collections.singletonList(new PParticle(location, 0.4F, 0.4F, 0.4F, 0.0F));
		case PORTAL:
			return Collections.singletonList(new PParticle(location, 0.5F, 0.5F, 0.5F, 0.05F));
		case RAIN:
			return Collections.singletonList(new PParticle(location, 0.4F, 0.4F, 0.4F, 0.0F));
		case SMOKE:
			return Collections.singletonList(new PParticle(location, 0.4F, 0.4F, 0.4F, 0.0F));
		case SPELL:
			return Collections.singletonList(new PParticle(location, 0.4F, 0.4F, 0.4F, 0.0F));
		case SPIT:
			return Collections.singletonList(new PParticle(location, 0.6F, 0.6F, 0.6F, 0.0F));
		case SPLASH:
			return Collections.singletonList(new PParticle(location, 0.4F, 0.4F, 0.4F, 0.0F));
		case SQUID_INK:
			return Collections.singletonList(new PParticle(location, 0.6F, 0.6F, 0.6F, 0.0F));
		case SWEEP_ATTACK:
			return Collections.singletonList(new PParticle(location, 0.4F, 0.4F, 0.4F, 0.0F));
		case TOTEM_OF_UNDYING:
			return Collections.singletonList(new PParticle(location, 0.6F, 0.6F, 0.6F, 0.0F));
		case UNDERWATER:
			for (int i = 0; i < 5; i++)
				particles.add(new PParticle(location, 0.5F, 0.5F, 0.5F, 0.0F));
			return particles;
		case WITCH:
			return Collections.singletonList(new PParticle(location, 0.4F, 0.4F, 0.4F, 0.0F));
		default:
			return Collections.singletonList(new PParticle(location, 0.4F, 0.4F, 0.4F, 0.0F));
		}
	}

	public void updateTimers() {

	}

	public String getName() {
		return "none";
	}

	public boolean canBeFixed() {
		return true;
	}

}
