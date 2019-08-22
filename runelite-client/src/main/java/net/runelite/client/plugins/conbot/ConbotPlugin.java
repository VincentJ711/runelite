/*
 * Copyright (c) 2018, Joris K <kjorisje@gmail.com>
 * Copyright (c) 2018, Lasse <cronick@zytex.dk>
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
package net.runelite.client.plugins.conbot;

import com.google.inject.Provides;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.NPC;
import net.runelite.api.NpcID;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.NpcSpawned;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDependency;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.xptracker.XpTrackerPlugin;

@Slf4j
@PluginDescriptor(
	name = "Conbot",
	description = "bots con",
	tags = { }
)
@PluginDependency(XpTrackerPlugin.class)
public class ConbotPlugin extends Plugin
{
	@Inject
	private Client client;

	@Provides
	ConbotConfig getConfig(ConfigManager configManager)
	{
		return configManager.getConfig(ConbotConfig.class);
	}

	private NPC butler;
	private String butlerLocation;


	@Override
	protected void startUp() throws Exception
	{
	}

	@Override
	protected void shutDown() throws Exception
	{
	}

	@Subscribe
	public void onGameTick(GameTick gameTick)
	{
		if (butler != null)
		{
			String prevLocation = butlerLocation;
			WorldPoint bloc = butler.getWorldLocation();
			WorldPoint ploc = client.getLocalPlayer().getWorldLocation();
			int bx = bloc.getX();
			int by = bloc.getY();
			int px = ploc.getX();
			int py = ploc.getY();

			if ((Math.abs(bx - px) + Math.abs(by - py)) != 1)
			{
				return;
			}

			if (by < py)
			{
				butlerLocation = "south";
			}
			else if (by > py)
			{
				butlerLocation = "north";
			}
			else if (bx < px)
			{
				butlerLocation = "west";
			}
			else if (bx > px)
			{
				butlerLocation = "east";
			}

			if (butlerLocation != prevLocation)
			{
				log.info("butler is now " + butlerLocation);
			}
		}
	}

	@Subscribe
	public void onNpcSpawned(NpcSpawned npcSpawned)
	{
		final NPC npc = npcSpawned.getNpc();

		if (npc.getId() == NpcID.DEMON_BUTLER)
		{
			butler = npc;
		}
	}
}
