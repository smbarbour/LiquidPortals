package cah.melonar.LiquidPortals;

import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import net.minecraft.server.ModLoader;
import net.minecraft.server.TileEntity;
import net.minecraft.server.WorldServer;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockRedstoneEvent;

import buildcraft.api.ILiquidContainer;
import buildcraft.api.Orientations;
import buildcraft.api.Position;

public class LPListener implements Listener {
	private LiquidPortals plugin;
	public final Logger log = Logger.getLogger("Minecraft");
	
	public LPListener(LiquidPortals liquidPortals) {
		this.plugin = liquidPortals;
	}

	@EventHandler
	public void onRedstonePower(BlockRedstoneEvent event) {
		Block recipient = event.getBlock();
		Block sittingOn = recipient.getRelative(BlockFace.DOWN,1);
		//log.info("[LP] Redstone event on " + recipient.getType().toString() + " @ " + recipient.getX() + "," + recipient.getZ() + " (" + recipient.getY() + ")");
		if (recipient.getType() == Material.SIGN_POST) {
			Sign s = (Sign) recipient.getState();
			if (s.getLine(1).toLowerCase().contains("[liquidportal]")) {
				//log.info("[LP] It's a good sign!");
				if (sittingOn.getType() == Material.OBSIDIAN) {
					if (!fill(Material.STATIONARY_LAVA.getId(),sittingOn))
					{
						if (sittingOn.getRelative(BlockFace.DOWN,1).isEmpty()) {
							sittingOn.getRelative(BlockFace.DOWN,1).setType(Material.STATIONARY_LAVA);
						}
					}
				} else if (sittingOn.getType() == Material.LAPIS_BLOCK) {
					if (!fill(Material.STATIONARY_WATER.getId(),sittingOn))
					{
						if (sittingOn.getRelative(BlockFace.DOWN,1).isEmpty()) {
							sittingOn.getRelative(BlockFace.DOWN,1).setType(Material.STATIONARY_WATER);
						}
					}
				} else if (sittingOn.getType() == Material.WOOL && sittingOn.getData() == 15) {
					if (!fill(163,sittingOn))
					{
						if (sittingOn.getRelative(BlockFace.DOWN,1).isEmpty()) {
							sittingOn.getRelative(BlockFace.DOWN,1).setTypeId(163); // BuildCraft Oil							
						}
					}
				}
			}
		}
	}

	private boolean fill(int liquidType, Block portalBlock)
	{
		boolean didFill = false;
		for (int i = 0; i < 6; ++i)
		{
			Position p = new Position(portalBlock.getX(), portalBlock.getY(), portalBlock.getZ(), Orientations.values()[i]);
			p.moveForwards(1);
			
			List<WorldServer> worldList = ModLoader.getMinecraftServerInstance().worlds;
			Iterator<WorldServer> worldIterator = worldList.iterator();
			while(worldIterator.hasNext())
			{
				WorldServer currentWS = worldIterator.next();
				if (currentWS.getWorld().getName().equals(portalBlock.getWorld().getName())) {
					TileEntity tile = currentWS.getWorld().getTileEntityAt((int) p.x, (int) p.y, (int) p.z);
					if (tile instanceof ILiquidContainer) {
						System.out.println(((ILiquidContainer) tile).fill(p.orientation.reverse(), buildcraft.api.API.BUCKET_VOLUME, liquidType, true));
						didFill = true;
					}
				}
			}		
		}
		return didFill;
	}
	
}