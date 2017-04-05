package com.esophose.playerparticles.styles;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;

import com.esophose.playerparticles.PPlayer;
import com.esophose.playerparticles.styles.api.PParticle;
import com.esophose.playerparticles.styles.api.ParticleStyle;

public class ParticleStyleArrows implements ParticleStyle, Listener {

	private String[] arrowEntityNames = new String[] { "ARROW", "SPECTRAL_ARROW", "TIPPED_ARROW" };
	private List<Arrow> arrows = new ArrayList<Arrow>();

	public PParticle[] getParticles(PPlayer pplayer, Location location) {
		List<PParticle> particles = new ArrayList<PParticle>();

		for (Arrow arrow : arrows) {
			if (((Player) arrow.getShooter()).getUniqueId() == pplayer.getUniqueId()) {
				particles.add(new PParticle(arrow.getLocation(), 0.05F, 0.05F, 0.05F, 0.0F));
			}
		}

		return particles.toArray(new PParticle[particles.size()]);
	}

	/**
	 * Removes all arrows that are considered dead
	 */
	public void updateTimers() {
		for (int i = arrows.size() - 1; i >= 0; i--) {
			Arrow arrow = arrows.get(i);
			if (arrow.getTicksLived() >= 1200 || arrow.isDead() || !arrow.isValid()) arrows.remove(i);
		}
	}

	public String getName() {
		return "arrows";
	}

	public boolean canBeFixed() {
		return false;
	}

	/**
	 * The event used to get all arrows fired by players
	 * Adds all arrows fired from players to the array
	 * 
	 * @param e The EntityShootBowEvent
	 */
	@EventHandler
	public void onArrowFired(EntityShootBowEvent e) {
		if (e.getEntityType() == EntityType.PLAYER) {
			String entityName = e.getProjectile().getType().toString();
			boolean match = false;
			for (String name : arrowEntityNames) {
				if (entityName.equalsIgnoreCase(name)) {
					match = true;
					break;
				}
			}
			
			if (match) arrows.add((Arrow) e.getProjectile());
		}
	}

}
