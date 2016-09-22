package com.esophose.playerparticles.styles;

import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import com.esophose.playerparticles.PPlayer;
import com.esophose.playerparticles.ParticleCreator;
import com.esophose.playerparticles.manager.ConfigManager;
import com.esophose.playerparticles.manager.PermissionManager;

public class ParticleStyleMove extends ParticleStyleNone implements Listener {

	public String getName() {
		return "move";
	}
	
	/**
	 * The event used to update the move style
	 * 
	 * @param e The event
	 */
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent e) {
		PPlayer pplayer = ConfigManager.getInstance().getPPlayer(e.getPlayer().getUniqueId());
		if (pplayer.getParticleStyle() == DefaultStyles.MOVE) {
			if (PermissionManager.hasStylePermission(e.getPlayer(), DefaultStyles.MOVE)) {
				Location loc = e.getPlayer().getLocation();
				loc.setY(loc.getY() + 0.05);
				ParticleCreator.displayParticles(pplayer, DefaultStyles.MOVE.getParticles(pplayer, loc));
			}
		}
	}

}
