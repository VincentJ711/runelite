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

		if ((tokens.length == 0) || !tokens[0].equals("!S"))
		{
			return;
		}

		if (tokens.length == 1)
		{
			toChat("xpt [lvl] [skill], xp [startlvl] [endlvl]");
			toChat("drt [lvl], dr [startlvl] [endlvl] (assumes 60k xp/hr)");
			toChat("brt [lvl], br [startlvl] [endlvl] (assumes 74k xp/hr)");
			toChat("wrt [lvl], wr [startlvl] [endlvl] (assumes 88k xp/hr)");
			toChat("bpt [lvl] [xp/hr ie 75], bp [startlvl] [endlvl] [xp/hr]");
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

		if (cmd.equals("xpt"))
		{
			handleXpToCmd(cmdArgs);
		}
		else if (cmd.equals("xp"))
		{
			handleXpCmd(cmdArgs);
		}
		else if (cmd.equals("drt"))
		{
			handleDRunesToCmd(cmdArgs);
		}
		else if (cmd.equals("dr"))
		{
			handleDRunesCmd(cmdArgs);
		}
		else if (cmd.equals("brt"))
		{
			handleBRunesToCmd(cmdArgs);
		}
		else if (cmd.equals("br"))
		{
			handleBRunesCmd(cmdArgs);
		}
		else if (cmd.equals("wrt"))
		{
			handleWRunesToCmd(cmdArgs);
		}
		else if (cmd.equals("wr"))
		{
			handleWRunesCmd(cmdArgs);
		}
		else if (cmd.equals("bpt"))
		{
			handleBpToCmd(cmdArgs);
		}
		else if (cmd.equals("bp"))
		{
			handleBpCmd(cmdArgs);
		}
	}

	private void handleBpToCmd(String[] args)
	{
		if ((args == null) || args.length != 2)
		{
			return;
		}

		try
		{
			int endlvl = Integer.parseInt(args[0]);
			int xpRate = Integer.parseInt(args[1]) * 1000;
			int xpDelta = Experience.getXpForLevel(endlvl) - client.getSkillExperience(Skill.RANGED);
			double hours = (double) xpDelta / xpRate;
			int dartsNeeded = (int) Math.floor(800 * hours);
			int scalesNeeded = (int) Math.floor(2000 * hours);
			int price = im.getItemPrice(ItemID.ZULRAHS_SCALES);
			int totalPrice = price * scalesNeeded;
			toChat(scalesNeeded + " scales @ " + price + "gp ea => " + totalPrice + "gp, " + dartsNeeded + " darts, " + String.format("%.2f", hours) + " hours");
		}
		catch (Throwable e)
		{

		}
	}

	private void handleBpCmd(String[] args)
	{
		if ((args == null) || args.length != 3)
		{
			return;
		}

		try
		{
			int startlvl = Integer.parseInt(args[0]);
			int endlvl = Integer.parseInt(args[1]);
			int xpRate = Integer.parseInt(args[2]) * 1000;
			int xpDelta = Experience.getXpForLevel(endlvl) - Experience.getXpForLevel(startlvl);
			double hours = (double) xpDelta / xpRate;
			int dartsNeeded = (int) Math.floor(800 * hours);
			int scalesNeeded = (int) Math.floor(2000 * hours);
			int price = im.getItemPrice(ItemID.ZULRAHS_SCALES);
			int totalPrice = price * scalesNeeded;
			toChat(scalesNeeded + " scales @ " + price + "gp ea => " + totalPrice + "gp, " + dartsNeeded + " darts, " + String.format("%.2f", hours) + " hours");
		}
		catch (Throwable e)
		{

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
		handleRunesToCmd(im.getItemPrice(ItemID.DEATH_RUNE), 60000, args);
	}

	private void handleDRunesCmd(String args[])
	{
		handleRunesCmd(im.getItemPrice(ItemID.DEATH_RUNE), 60000, args);
	}

	private void handleBRunesToCmd(String args[])
	{
		handleRunesToCmd(im.getItemPrice(ItemID.BLOOD_RUNE), 74000, args);
	}

	private void handleBRunesCmd(String args[])
	{
		handleRunesCmd(im.getItemPrice(ItemID.BLOOD_RUNE), 74000, args);
	}

	private void handleWRunesToCmd(String args[])
	{
		handleRunesToCmd(im.getItemPrice(ItemID.WRATH_RUNE), 88000, args);
	}

	private void handleWRunesCmd(String args[])
	{
		handleRunesCmd(im.getItemPrice(ItemID.WRATH_RUNE), 88000, args);
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
			double hours = (double) xpDelta / xpRate;
			int runesNeeded = (int) Math.floor(hours * 1200);
			toChat(runesNeeded + " needed @ " + runecost + "gp ea => " + runesNeeded * runecost + "gp, " + String.format("%.2f", hours) + " hours");
		}
		catch (Throwable e)
		{

		}
	}

	private int getXpToLevel(int lvl, String skillName)
	{
		Skill skill = Skill.valueOf(skillName.toUpperCase());
		int startxp = client.getSkillExperience(skill);
		int endxp = Experience.getXpForLevel(lvl);
		return endxp - startxp;
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