package dev.esophose.playerparticles.util.inputparser.parsable;

import dev.esophose.playerparticles.particles.PPlayer;
import dev.esophose.playerparticles.util.inputparser.Parsable;
import java.util.List;
import java.util.Set;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class ParsableLocation extends Parsable<Location> {

    public ParsableLocation() {
        super(Location.class);
    }

    @Override
    public Location parse(PPlayer pplayer, List<String> inputs) {
        String input = inputs.remove(0);

        Player player = pplayer.getPlayer();
        if (input.equalsIgnoreCase("looking")) {
            Block targetBlock = player.getTargetBlock((Set<Material>) null, 8); // Need the Set<Material> cast for 1.9 support
            int maxDistanceSqrd = 6 * 6;
            if (targetBlock.getLocation().distanceSquared(player.getLocation()) > maxDistanceSqrd)
                return null; // Looking at a block too far away

            return targetBlock.getLocation().clone().add(0.5, 0.5, 0.5); // Center of block
        }

        String input2 = inputs.remove(0);
        String input3 = inputs.remove(0);

        double x, y, z;

        if (input.startsWith("~")) {
            if (input.equals("~")) x = player.getLocation().getX();
            else x = player.getLocation().getX() + Double.parseDouble(input.substring(1));
        } else {
            x = Double.parseDouble(input);
        }

        if (input2.startsWith("~")) {
            if (input2.equals("~")) y = player.getLocation().getY();
            else y = player.getLocation().getY() + Double.parseDouble(input2.substring(1));
        } else {
            y = Double.parseDouble(input);
        }

        if (input3.startsWith("~")) {
            if (input3.equals("~")) z = player.getLocation().getZ();
            else z = player.getLocation().getZ() + Double.parseDouble(input3.substring(1));
        } else {
            z = Double.parseDouble(input);
        }

        return new Location(player.getWorld(), x, y, z);
    }

}
