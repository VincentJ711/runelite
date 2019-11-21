package net.runelite.client.plugins.playerlogger;

import java.util.HashMap;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.Player;
import net.runelite.api.coords.WorldPoint;
import net.runelite.client.events.ConfigChanged;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.GameTick;
import net.runelite.client.chat.ChatColorType;
import net.runelite.client.chat.ChatMessageBuilder;
import net.runelite.client.chat.ChatMessageManager;
import net.runelite.client.chat.QueuedMessage;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;

@Slf4j
@PluginDescriptor(
	name = "Player Logger",
	description = "logs players nearby in wildy",
	tags = {"wildy", "players", "scamp"}
)
public class PlayerLoggerPlugin extends Plugin
{
	@Inject
	private Client client;

	@Inject
	ChatMessageManager chatMessageManager;

	private HashMap<String, Boolean> m = new HashMap();

	@Subscribe
	public void onConfigChanged(ConfigChanged event)
	{

	}


	@Override
	protected void startUp() throws Exception
	{

	}

	@Override
	protected void shutDown() throws Exception
	{

	}

	@Subscribe
	public void onGameStateChanged(GameStateChanged e)
	{
		if (e.getGameState() == GameState.HOPPING)
		{
			m.clear();
		}
	}

	@Subscribe
	public void onGameTick(GameTick t)
	{
		if (client.getPlane() != 0)
		{
			return;
		}

		String myName = client.getLocalPlayer().getName();

		client.getPlayers().forEach(player ->
		{
			String name = player.getName();
			if (!name.equals(myName) && (m.get(name) == null) && isInMainWildy(player))
			{
				String chatMessage = new ChatMessageBuilder()
					.append(ChatColorType.HIGHLIGHT)
					.append(name + ": " + player.getCombatLevel())
					.build();

				chatMessageManager.queue(QueuedMessage.builder()
					.type(ChatMessageType.CONSOLE)
					.runeLiteFormattedMessage(chatMessage)
					.build());

				m.put(name, true);
			}
		});

	}

	private boolean isInMainWildy(Player p)
	{
		WorldPoint wp = WorldPoint.fromLocal(client, p.getLocalLocation());
		int x = wp.getX();
		int y = wp.getY();
		return (x >= 2944) && (x <= 3392) && (y >= 3525);

	}
}