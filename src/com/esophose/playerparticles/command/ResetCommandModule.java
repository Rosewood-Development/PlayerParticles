package com.esophose.playerparticles.command;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.esophose.playerparticles.manager.DataManager;
import com.esophose.playerparticles.manager.LangManager;
import com.esophose.playerparticles.manager.PermissionManager;
import com.esophose.playerparticles.manager.LangManager.Lang;
import com.esophose.playerparticles.particles.PPlayer;
import com.esophose.playerparticles.particles.ParticleGroup;

public class ResetCommandModule implements CommandModule {

    public void onCommandExecute(PPlayer pplayer, String[] args) {
        if (args.length >= 1) {
            String altPlayerName = args[0];
            if (!PermissionManager.canUseForceReset(pplayer.getPlayer())) {
                LangManager.sendMessage(pplayer, Lang.FAILED_EXECUTE_NO_PERMISSION, altPlayerName);
            } else {
                Player targetPlayer = null;
                for (Player p : Bukkit.getOnlinePlayers()) {
                    if (p.getName().toLowerCase().contains(altPlayerName.toLowerCase())) {
                        targetPlayer = p;
                        break;
                    }
                }

                if (targetPlayer == null) {
                    LangManager.sendMessage(pplayer, Lang.FAILED_EXECUTE_NOT_FOUND, altPlayerName);
                } else {
                    DataManager.saveParticleGroup(targetPlayer.getUniqueId(), ParticleGroup.getDefaultGroup());
                    LangManager.sendMessage(targetPlayer, Lang.RESET);

                    LangManager.sendMessage(pplayer, Lang.EXECUTED_FOR_PLAYER, targetPlayer.getName());
                }
            }
        } else {
            DataManager.saveParticleGroup(pplayer.getUniqueId(), ParticleGroup.getDefaultGroup());
            LangManager.sendMessage(pplayer, Lang.RESET);
        }
    }

    public List<String> onTabComplete(PPlayer pplayer, String[] args) {
        return null;
    }

    public String getName() {
        return "reset";
    }

    public Lang getDescription() {
        return Lang.RESET_COMMAND_DESCRIPTION;
    }

    public String getArguments() {
        return "";
    }

    public boolean requiresEffects() {
        return false;
    }

}
