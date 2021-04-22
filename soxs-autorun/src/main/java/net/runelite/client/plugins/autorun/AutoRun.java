/*
 * Copyright (c) 2018, Adam <Adam@sigterm.info>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package net.runelite.client.plugins.autorun;


import javax.inject.Inject;
import javax.inject.Singleton;

import com.google.inject.Provides;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.events.GameTick;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.EventBus;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.game.WorldService;
import net.runelite.client.plugins.*;
import net.runelite.client.ui.overlay.OverlayManager;
import org.pf4j.Extension;

import java.util.Random;
import java.util.concurrent.ExecutorService;

@Extension
@PluginDescriptor(
	name = "AutoRunEnable",
	description = "Auto enables run.",
	tags = {"run", "enable run", "run energy"},
	enabledByDefault = true,
	hidden = false
)
@Slf4j
@SuppressWarnings("unused")
@Singleton
public class AutoRun extends Plugin
{
	@Inject
	private Client client;

	@Inject
	private PluginManager pluginManager;

	@Inject
	private OverlayManager overlayManager;

	@Inject
	private AutoRunConfig config;

	@Inject
	private EventBus eventBus;

	@Inject
	private ClientThread clientThread;

	@Inject
	private WorldService worldService;

	@Inject
	private ExecutorService executor;

	private final Random rand = new Random();

	private int nextRunThreshhold = -1;

	@Provides
	AutoRunConfig getConfig(ConfigManager configManager)
	{
		return configManager.getConfig(AutoRunConfig.class);
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
	public void onGameTick(GameTick event)
	{
		if (nextRunThreshhold <= 0)
			nextRunThreshhold = randInt(config.minRun(), config.maxRun());
		else {
			if (!isRunning() && client.getEnergy() > nextRunThreshhold) {
				toggleRun();
				nextRunThreshhold = -1;
			}
		}
	}

	public boolean isRunning() {
		return client.getVarpValue(173) == 1;
	}

	public void toggleRun()
	{
		executor.submit(() ->
		{
			Widget depositInventoryWidget = client.getWidget(WidgetInfo.MINIMAP_TOGGLE_RUN_ORB);
			client.invokeMenuAction("", "", 1, MenuAction.CC_OP.getId(), -1, depositInventoryWidget.getId());
		});
	}

	public int randInt(Random r, int min, int max) {
		return r.nextInt((max - min) + 1) + min;
	}

	public int randInt(int min, int max) {
		if (max < min)
			max = min;
		return randInt(rand, min, max);
	}

}
