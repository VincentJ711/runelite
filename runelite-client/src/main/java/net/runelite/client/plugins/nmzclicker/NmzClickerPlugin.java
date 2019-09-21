package net.runelite.client.plugins.nmzclicker;

import com.google.gson.JsonObject;
import com.google.inject.Provides;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Arrays;
import java.util.Timer;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.InventoryID;
import net.runelite.api.Item;
import net.runelite.api.ItemContainer;
import net.runelite.api.ItemID;
import net.runelite.api.Skill;
import net.runelite.api.Varbits;
import net.runelite.api.events.ConfigChanged;
import net.runelite.api.events.GameTick;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.input.KeyManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.ClientUI;
import net.runelite.client.ui.overlay.OverlayManager;

@Slf4j
@PluginDescriptor(
	name = "Nightmare Zone Clicker",
	description = "helps you with NMZ :)",
	tags = {"nmz"}
)
public class NmzClickerPlugin extends Plugin
{
	private static final Path LOG_FILE = Paths.get(Paths.get(System.getProperty("user.home")).toString(),
		"desktop", "nmz-auto-clicker", "runelite.log");
	private static final String LOG_DELIMITER_REGULAR = "REGULAR";
	static final String LOG_DELIMITER_KEY_PRESSED = "KEY_PRESSED";
	private static final int[] NMZ_MAP_REGION = {9033};

	private String invy = "";
	private int absPts;
	private Timer timer;
	private BufferedWriter writer;
	private long lastFTime = 0;
	private long ticks = 0;

	@Inject
	private Client client;

	@Inject ClientUI ui;

	@Inject
	private OverlayManager overlayManager;

	@Inject
	private NmzClickerConfig config;

	@Inject
	private KeyManager keyManager;

	@Inject
	private NmzClickerChatListener chatKeyboardListener;

	@Subscribe
	public void onConfigChanged(ConfigChanged event)
	{

	}

	@Provides
	NmzClickerConfig getConfig(ConfigManager configManager)
	{
		return configManager.getConfig(NmzClickerConfig.class);
	}


	@Override
	protected void startUp() throws Exception
	{
		deleteLogFile();
		keyManager.registerKeyListener(chatKeyboardListener);
	}

	@Override
	protected void shutDown() throws Exception
	{
		keyManager.unregisterKeyListener(chatKeyboardListener);
	}

	@Subscribe
	public void onGameTick(GameTick event)
	{
		if (ticks == 0)
		{
			ticks++;

			if (client.getGameState() != GameState.LOGGED_IN)
			{
				return;
			}

			JsonObject o = new JsonObject();

			setInvy();
			setAbsPts();

			if (invy == null)
			{
				return;
			}

			o.addProperty("nmz", Arrays.equals(client.getMapRegions(), NMZ_MAP_REGION));
			o.addProperty("attack", client.getBoostedSkillLevel(Skill.ATTACK));
			o.addProperty("strength", client.getBoostedSkillLevel(Skill.STRENGTH));
			o.addProperty("defence", client.getBoostedSkillLevel(Skill.DEFENCE));
			o.addProperty("magic", client.getBoostedSkillLevel(Skill.MAGIC));
			o.addProperty("ranged", client.getBoostedSkillLevel(Skill.RANGED));
			o.addProperty("hitpoints", client.getBoostedSkillLevel(Skill.HITPOINTS));
			o.addProperty("prayer", client.getBoostedSkillLevel(Skill.PRAYER));
			o.addProperty("abs", absPts);
			o.addProperty("invy", invy);

			o.addProperty("bot", config.bot());
			o.addProperty("soundType", config.soundType().toString());
			o.addProperty("skipFrequency", config.skipFrequency());
			o.addProperty("minAtt", config.attackThreshold());
			o.addProperty("minStr", config.strengthThreshold());
			o.addProperty("minDef", config.defenceThreshold());
			o.addProperty("minRng", config.rangeThreshold());
			o.addProperty("minMage", config.magicThreshold());
			o.addProperty("minPray", config.prayerThreshold());
			o.addProperty("minAbs", config.absThreshold());

			logToFile(LOG_DELIMITER_REGULAR, o.toString());
		}
		else if (ticks == 1)
		{
			ticks = 0;
		}

	}

	private void setAbsPts()
	{
		try
		{
			absPts = client.getVar(Varbits.NMZ_ABSORPTION);
		}
		catch (Error e)
		{
			absPts = 0;
		}
	}

	private void setInvy()
	{
		ItemContainer cont = client.getItemContainer(InventoryID.INVENTORY);
		String tempInvy = "";

		if (cont == null)
		{
			return;
		}

		Item[] items = cont.getItems();

		for (int i = 0; i < 28; i++)
		{
			int id = ((i + 1) > items.length) ? -1 : items[i].getId();

			switch (id)
			{
				case ItemID.DWARVEN_ROCK_CAKE_7510:
				case ItemID.DWARVEN_ROCK_CAKE:
				case ItemID.ROCK_CAKE:
					tempInvy += "0X";
					break;
				case ItemID.SUPER_RANGING_1:
					tempInvy += "1R";
					break;
				case ItemID.SUPER_RANGING_2:
					tempInvy += "2R";
					break;
				case ItemID.SUPER_RANGING_3:
					tempInvy += "3R";
					break;
				case ItemID.SUPER_RANGING_4:
					tempInvy += "4R";
					break;

				case ItemID.SUPER_MAGIC_POTION_1:
					tempInvy += "1M";
					break;
				case ItemID.SUPER_MAGIC_POTION_2:
					tempInvy += "2M";
					break;
				case ItemID.SUPER_MAGIC_POTION_3:
					tempInvy += "3M";
					break;
				case ItemID.SUPER_MAGIC_POTION_4:
					tempInvy += "4M";
					break;

				case ItemID.SUPER_COMBAT_POTION1:
					tempInvy += "1C";
					break;
				case ItemID.SUPER_COMBAT_POTION2:
					tempInvy += "2C";
					break;
				case ItemID.SUPER_COMBAT_POTION3:
					tempInvy += "3C";
					break;
				case ItemID.SUPER_COMBAT_POTION4:
					tempInvy += "4C";
					break;

				case ItemID.PRAYER_POTION1:
				case ItemID.SUPER_RESTORE1:
					tempInvy += "1P";
					break;
				case ItemID.PRAYER_POTION2:
				case ItemID.SUPER_RESTORE2:
					tempInvy += "2P";
					break;
				case ItemID.PRAYER_POTION3:
				case ItemID.SUPER_RESTORE3:
					tempInvy += "3P";
					break;
				case ItemID.PRAYER_POTION4:
				case ItemID.SUPER_RESTORE4:
					tempInvy += "4P";
					break;


				case ItemID.OVERLOAD_1:
					tempInvy += "1V";
					break;
				case ItemID.OVERLOAD_2:
					tempInvy += "2V";
					break;
				case ItemID.OVERLOAD_3:
					tempInvy += "3V";
					break;
				case ItemID.OVERLOAD_4:
					tempInvy += "4V";
					break;

				case ItemID.ABSORPTION_1:
					tempInvy += "1A";
					break;
				case ItemID.ABSORPTION_2:
					tempInvy += "2A";
					break;
				case ItemID.ABSORPTION_3:
					tempInvy += "3A";
					break;
				case ItemID.ABSORPTION_4:
					tempInvy += "4A";
					break;
				default:
					tempInvy += "00";
			}

			tempInvy += " ";
		}

		invy = tempInvy.trim();
	}

	private void deleteLogFile()
	{
		try
		{
			Files.delete(LOG_FILE);
		}
		catch (IOException e)
		{

		}
	}

	void logToFile(String delimiter, String text)
	{
		long currTime = 0;
		boolean doesntExist = false;

		try
		{
			currTime = getCreateTime();
		}
		catch (IOException e)
		{
			doesntExist = true;
		}

		try
		{
			if (doesntExist || (writer == null) || (lastFTime != currTime))
			{
				writer = Files.newBufferedWriter(LOG_FILE, StandardCharsets.UTF_8,
					StandardOpenOption.CREATE, StandardOpenOption.APPEND);
				lastFTime = getCreateTime();
			}

			writer.write("[" + delimiter + "] " + text + "\n");
			writer.flush();
		}
		catch (IOException e)
		{

		}
	}

	private long getCreateTime() throws IOException
	{
		BasicFileAttributes attr = Files.readAttributes(LOG_FILE, BasicFileAttributes.class);
		return attr.creationTime().toMillis();
	}
}
