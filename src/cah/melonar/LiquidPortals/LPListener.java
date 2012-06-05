package cah.melonar.LiquidPortals;

import java.util.logging.Logger;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockRedstoneEvent;

public class LPListener implements Listener {
	public static LiquidPortals plugin;
	public final Logger log = Logger.getLogger("Minecraft");
	
	@EventHandler
	public void onRedstonePower(BlockRedstoneEvent event) {
		Block recipient = event.getBlock();
		Block sittingOn = recipient.getRelative(BlockFace.DOWN,1);
		//log.info("[LP] Redstone event on " + recipient.getType().toString() + " @ " + recipient.getX() + "," + recipient.getZ() + " (" + recipient.getY() + ")");
		if (recipient.getType() == Material.SIGN_POST) {
			Sign s = (Sign) recipient.getState();
			if (s.getLine(1).toLowerCase().contains("[liquidportal]")) {
				//log.info("[LP] It's a good sign!");
				if (sittingOn.getType() == Material.OBSIDIAN && sittingOn.getRelative(BlockFace.DOWN,1).isEmpty()) {
					sittingOn.getRelative(BlockFace.DOWN,1).setType(Material.LAVA);
				} else if (sittingOn.getType() == Material.LAPIS_BLOCK && sittingOn.getRelative(BlockFace.DOWN,1).isEmpty()) {
					sittingOn.getRelative(BlockFace.DOWN,1).setType(Material.WATER);
				} else if (sittingOn.getType() == Material.WOOL && sittingOn.getData() == 15 && sittingOn.getRelative(BlockFace.DOWN,1).isEmpty()) {
					sittingOn.getRelative(BlockFace.DOWN,1).setTypeId(162); // BuildCraft Oil
				}
			}
		}
	}
}