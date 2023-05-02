package net.runelite.client.plugins.autologhop;

import com.google.inject.Provides;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import lombok.AccessLevel;
import lombok.Getter;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.ItemID;
import net.runelite.api.Player;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.events.ConfigChanged;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.PlayerSpawned;
import net.runelite.api.events.PlayerDespawned;
import net.runelite.api.events.PlayerMenuOptionClicked;
import net.runelite.api.events.WidgetLoaded;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetID;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.PluginType;
import net.runelite.client.ui.overlay.OverlayManager;

@PluginDescriptor(
	name = "Auto Log/Hop",
	description = "Automatically log out or hop worlds when player is detected",
	tags = {"auto", "log", "hop", "wilderness", "pvp"},
	type = PluginType.PVP,
	enabledByDefault = false
)
public class AutoLogHopPlugin extends Plugin
{
	private static final int TICK_THRESHOLD = 2;
	private static final int MAX_SKULL_WATCH_TIMER = 12;

	@Inject
	private Client client;

	@Inject
	private OverlayManager overlayManager;

	@Inject
	private AutoLogHopOverlay overlay;

	@Inject
	private AutoLogHopConfig config;

	@Inject
	private ClientThread clientThread;

	@Getter(AccessLevel.PACKAGE)
	private List<String> ignoredPlayers = new ArrayList<>();

	private Player targetPlayer;
	private boolean isTeleporting;
	private boolean isTeleportWidgetOpen;
	private boolean isLogoutWidgetOpen;
	private int logoutWatchTimer;
	private int skullWatchTimer;

	@Provides
	AutoLogHopConfig getConfig(ConfigManager configManager)
	{
		return configManager.getConfig(AutoLogHopConfig.class);
	}

	@Override
	protected void startUp() throws Exception
	{
		overlayManager.add(overlay);
		ignoredPlayers = parseIgnoreList(config.whitelist());
		logoutWatchTimer = 0;
		skullWatchTimer = 0;
	}

	@Override
	protected void shutDown() throws Exception
	{
		overlayManager.remove(overlay);
	}

	@Subscribe
	private void onConfigChanged(ConfigChanged event)
	{
		if (!event.getGroup().equals("autologhop"))
		{
			return;
		}

		ignoredPlayers = parseIgnoreList(config.whitelist());
	}

	@Subscribe
	private void onGameStateChanged(GameStateChanged event)
	{
		if (event.getGameState() == GameState.LOGIN_SCREEN)
		{
			logoutWatchTimer = 0;
			isTeleporting = false;
			isTeleportWidgetOpen = false;
			isLogoutWidgetOpen = false;
		}
	}

	@Subscribe
	private void onGameTick(GameTick event)
	{
		if (!shouldCheck())
		{
			return;
		}

		if (isTeleporting)
		{
			checkTeleportWidget();
		}
		else if (isLogoutWidgetOpen)
		{
			checkLogoutWidget();
	
