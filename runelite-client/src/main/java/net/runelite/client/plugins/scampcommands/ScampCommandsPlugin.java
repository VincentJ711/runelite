package net.runelite.client.plugins.scampcommands;

import java.util.Arrays;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.Experience;
import net.runelite.api.ItemID;
import net.runelite.api.Skill;
import net.runelite.api.events.ChatMessage;
import net.runelite.api.events.ConfigChanged;
import net.runelite.client.chat.ChatColorType;
import net.runelite.client.chat.ChatMessageBuilder;
import net.runelite.client.chat.ChatMessageManager;
import net.runelite.client.chat.QueuedMessage;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.game.ItemManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;

@Slf4j
@PluginDescriptor(
	name = "Scamp Commands",
	description = "enables certain chat commands prefixed with !scamp",
	tags = {"chat", "commands", "scamp"}
)
public class ScampCommandsPlugin extends Plugin
{
	@Inject
	private Client client;

	@Inject
	private ItemManager im;

	@Inject
	ChatMessageManager chatMessageManager;


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
	public void onChatMessage(ChatMessage e)
	{
		String m = e.getMessage();
		String tokens[] = m.split(" ");
		String cmd = "";
		String cmdArgs[] = null;

		if (client.getLocalPlayer() == null)
		{
			return;
		}

		log.info(e.getName());
		log.info(client.getLocalPlayer().getName());

		log.info(client.getLocalPlayer().getName().equals(e.getName()) + "");

		log.info(im.getItemPrice(ItemID.DEATH_RUNE) + "");
		log.info(im.getItemPrice(ItemID.DEATH_RUNE_6432) + "");
		log.info(im.getItemPrice(ItemID.DEATH_RUNE_11692) + "");
		log.info(im.getItemPrice(ItemID.DEATH_RUNE_NZ) + "");


//		if (!e.getName().equals(client.getLocalPlayer().getName()))
//		{
//			return;
//		}

		if ((tokens.length == 0) || !tokens[0].equals("!Scamp"))
		{
			return;
		}

		if (tokens.length == 1)
		{
			toChat("xpto [lvl] [skill], xp [startlvl] [endlvl]");
			toChat("drunesto [lvl], drunes [startlvl] [endlvl] (assumes 60k xp/hr)");
			toChat("brunesto [lvl], brunes [startlvl] [endlvl] (assumes 74k xp/hr)");
			toChat("wrunesto [lvl], wrunes [startlvl] [endlvl] (assumes 88k xp/hr)");
			toChat("bpto [lvl] [xp/hr ie 75], bp [startlvl] [endlvl] [xp/hr]");
			return;
		}

		if (tokens.length > 1)
		{
			cmd = tokens[1];
		}

		if (tokens.length > 2)
		{
			cmdArgs = Arrays.copyOfRange(tokens, 2, tokens.length);
		}

		if (cmd.equals("xpto"))
		{
			handleXpToCmd(cmdArgs);
		}
		else if (cmd.equals("xp"))
		{
			handleXpCmd(cmdArgs);
		}
		else if (cmd.equals("drunesto"))
		{
			handleDRunesToCmd(cmdArgs);
		}
		else if (cmd.equals("drunes"))
		{
			handleDRunesCmd(cmdArgs);
		}
		else if (cmd.equals("brunesto"))
		{
			handleBRunesToCmd(cmdArgs);
		}
		else if (cmd.equals("brunes"))
		{
			handleBRunesCmd(cmdArgs);
		}
		else if (cmd.equals("wrunesto"))
		{
			handleWRunesToCmd(cmdArgs);
		}
		else if (cmd.equals("wrunes"))
		{
			handleWRunesCmd(cmdArgs);
		}
	}

	private void handleXpToCmd(String args[])
	{
		if ((args == null) || args.length != 2)
		{
			return;
		}

		try
		{
			toChat(getXpToLevel(Integer.parseInt(args[0]), args[1]) + "");
		}
		catch (Throwable e)
		{

		}
	}

	private void handleXpCmd(String args[])
	{
		if ((args == null) || args.length != 2)
		{
			return;
		}

		try
		{
			toChat(getXpToLevel(Integer.parseInt(args[0]), Integer.parseInt(args[1])) + "");
		}
		catch (Throwable e)
		{

		}
	}

	private void handleDRunesToCmd(String args[])
	{
		handleRunesToCmd(200, 60000, args);
	}

	private void handleDRunesCmd(String args[])
	{
		handleRunesCmd(200, 60000, args);
	}

	private void handleBRunesToCmd(String args[])
	{
		handleRunesToCmd(300, 74000, args);
	}

	private void handleBRunesCmd(String args[])
	{
		handleRunesCmd(300, 74000, args);
	}

	private void handleWRunesToCmd(String args[])
	{
		handleRunesToCmd(400, 88000, args);
	}

	private void handleWRunesCmd(String args[])
	{
		handleRunesCmd(400, 88000, args);
	}

	private void handleRunesToCmd(int runecost, int xpRate, String args[])
	{
		if ((args == null) || args.length != 1)
		{
			return;
		}
		handleRunes(runecost, xpRate, client.getSkillExperience(Skill.MAGIC), args[0]);
	}

	private void handleRunesCmd(int runecost, int xpRate, String args[])
	{
		if ((args == null) || args.length != 2)
		{
			return;
		}

		try
		{
			handleRunes(runecost, xpRate, Experience.getXpForLevel(Integer.parseInt(args[0])), args[1]);
		}
		catch (Throwable e)
		{

		}
	}

	private void handleRunes(int runecost, int xpRate, int startxp, String endlvl)
	{
		try
		{
			int endxp = Experience.getXpForLevel(Integer.parseInt(endlvl));
			int xpDelta = endxp - startxp;
			int runesNeeded = (int) Math.floor((xpDelta / xpRate) * 1200);
			toChat(runesNeeded + " runes needed @ " + runecost + "gp ea => " + runesNeeded * runecost + "gp");
		}
		catch (Throwable e)
		{

		}
	}

	private int getXpToLevel(int lvl, String skillName)
	{
		Skill skill = Skill.valueOf(skillName.toUpperCase());
		return getXpToLevel(client.getRealSkillLevel(skill), lvl);
	}

	private int getXpToLevel(int startlvl, int endlvl)
	{
		return Experience.getXpForLevel(endlvl) - Experience.getXpForLevel(startlvl);
	}

	private void toChat(String m)
	{
		String chatMessage = new ChatMessageBuilder()
			.append(ChatColorType.HIGHLIGHT)
			.append(m)
			.build();

		chatMessageManager.queue(QueuedMessage.builder()
			.type(ChatMessageType.CONSOLE)
			.runeLiteFormattedMessage(chatMessage)
			.build());
	}
}