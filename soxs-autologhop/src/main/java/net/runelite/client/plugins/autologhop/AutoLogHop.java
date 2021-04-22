package net.runelite.client.plugins.autologhop;


import javax.inject.Inject;
import javax.inject.Singleton;

import com.google.inject.Provides;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.events.GameTick;
import net.runelite.api.widgets.Widget;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.EventBus;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.game.WorldService;
import net.runelite.client.plugins.*;
import net.runelite.client.ui.overlay.OverlayManager;
import net.runelite.client.util.WorldUtil;
import net.runelite.http.api.worlds.World;
import net.runelite.http.api.worlds.WorldResult;
import org.pf4j.Extension;

import java.util.Collections;
import java.util.List;

@Extension
@PluginDescriptor(
	name = "AutoLogHop",
	description = "Auto hops/logs out when another player is seen.",
	tags = {"logout", "hop worlds", "auto log", "auto hop"},
	enabledByDefault = false,
	hidden = false
)
@Slf4j
@SuppressWarnings("unused")
@Singleton
public class AutoLogHop extends Plugin
{
	@Inject
	private Client client;

	@Inject
	private PluginManager pluginManager;

	@Inject
	private OverlayManager overlayManager;

	@Inject
	private AutoLogHopConfig config;

	@Inject
	private EventBus eventBus;

	@Inject
	private ClientThread clientThread;

	@Inject
	private WorldService worldService;

	@Provides
	AutoLogHopConfig getConfig(ConfigManager configManager)
	{
		return configManager.getConfig(AutoLogHopConfig.class);
	}

	@Override
	protected void startUp()
	{

	}

	@Override
	protected void shutDown()
	{

	}

	@Subscribe
	public void onGameTick(GameTick event){
		if (nearPlayer())
		{
			if (config.hop())
				hopToWorld(getValidWorld());
			else {
				logout();
			}
		}
	}

	private boolean nearPlayer() {
		List<Player> players = client.getPlayers();
		for (Player p : players)
		{
			if (p == client.getLocalPlayer())
				continue;

			return true;
		}
		return false;
	}

	private int getValidWorld() {
		WorldResult result = worldService.getWorlds();
		if (result == null)
			return -1;
		List<World> worlds = result.getWorlds();
		Collections.shuffle(worlds);
		for (World w : worlds)
		{
			if (client.getWorld() == w.getId())
				continue;

			if (w.getTypes().contains(net.runelite.http.api.worlds.WorldType.HIGH_RISK) ||
					w.getTypes().contains(net.runelite.http.api.worlds.WorldType.PVP) ||
					w.getTypes().contains(net.runelite.http.api.worlds.WorldType.SKILL_TOTAL) ||
					w.getTypes().contains(net.runelite.http.api.worlds.WorldType.BOUNTY))
				continue;
			return w.getId();
		}
		return -1;
	}

	private void hopToWorld(int worldId)
	{
		assert client.isClientThread();

		WorldResult worldResult = worldService.getWorlds();
		// Don't try to hop if the world doesn't exist
		World world = worldResult.findWorld(worldId);
		if (world == null)
		{
			return;
		}

		final net.runelite.api.World rsWorld = client.createWorld();
		rsWorld.setActivity(world.getActivity());
		rsWorld.setAddress(world.getAddress());
		rsWorld.setId(world.getId());
		rsWorld.setPlayerCount(world.getPlayers());
		rsWorld.setLocation(world.getLocation());
		rsWorld.setTypes(WorldUtil.toWorldTypes(world.getTypes()));

		if (client.getGameState() == GameState.LOGIN_SCREEN)
		{
			// on the login screen we can just change the world by ourselves
			client.changeWorld(rsWorld);
			return;
		}
		client.hopToWorld(rsWorld);
	}

	private void logout() {
		Widget logoutButton = client.getWidget(182, 8);
		Widget logoutDoorButton = client.getWidget(69, 23);
		int param1 = -1;
		if (logoutButton != null)
		{
			param1 = logoutButton.getId();
		}
		else if (logoutDoorButton != null)
		{
			param1 = logoutDoorButton.getId();
		}
		if (param1 == -1)
		{
			return;
		}
		client.invokeMenuAction(
				"Logout",
				"",
				1,
				MenuAction.CC_OP.getId(),
				-1,
				param1
		);
	}

}
