package net.runelite.client.plugins.nmzclicker;

import com.google.inject.Provides;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.Timer;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
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
	private static final int[] NMZ_MAP_REGION = {9033};
	private static final String LOG_DELIMITER = "MY_NMZ_LOG";

	static void logToFile(String delimiter)
	{
		logToFile(delimiter, "");
	}

	static void logToFile(String delimiter, String text)
	{
		try
		{
			BufferedWriter x = Files.newBufferedWriter(LOG_FILE, StandardCharsets.UTF_8,
				StandardOpenOption.CREATE, StandardOpenOption.APPEND);

			x.write("[" + delimiter + "] " + text + "\n");
			x.close();
		}
		catch (IOException e)
		{
		}
	}

	private final int[] lastSkillLevels = new int[Skill.values().length - 1];
	boolean wasInNmz = false;
	private int lastAbsPts = 0;
	private int lastLoggedAbsPts = 0;
	private String lastInvy = "";
	private Timer timer;

	@Inject
	private Client client;

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
		timer = new Timer();
		timer.scheduleAtFixedRate(new InNmzCheckTask(this), 0, 5000);
		keyManager.registerKeyListener(chatKeyboardListener);
		init();
	}

	@Override
	protected void shutDown() throws Exception
	{
		timer.cancel();
		timer = null;
		keyManager.unregisterKeyListener(chatKeyboardListener);
	}

	private void exitedNmz()
	{
		logToFile(LogDelimiters.NMZ_OFF);
		wasInNmz = false;
	}

	private void init()
	{
		lastAbsPts = 0;
		lastLoggedAbsPts = 0;
		lastInvy = "";
		Arrays.fill(lastSkillLevels, -1);
	}

	private int getAbsorptionPts()
	{
		return client.getVar(Varbits.NMZ_ABSORPTION);
	}

	public boolean isInNightmareZone()
	{
		return Arrays.equals(client.getMapRegions(), NMZ_MAP_REGION);
	}

	private void checkStatChange(Skill skill)
	{
		int skillIdx = skill.ordinal();
		int last = lastSkillLevels[skillIdx];
		int cur = client.getBoostedSkillLevel(skill);

		if (cur == last)
		{
			return;
		}

		if (cur == (last + 1))
		{
			if (skill.equals(Skill.HITPOINTS))
			{
				logToFile(LogDelimiters.HP_INC, cur + "");
			}
		}
		else if (cur < last)
		{
			if (skill.equals(Skill.ATTACK))
			{
				logToFile(LogDelimiters.ATTACK_DEC, cur + "");
			}
			else if (skill.equals(Skill.STRENGTH))
			{
				logToFile(LogDelimiters.STRENGTH_DEC, cur + "");
			}
			else if (skill.equals(Skill.DEFENCE))
			{
				logToFile(LogDelimiters.DEFENCE_DEC, cur + "");
			}
			else if (skill.equals(Skill.RANGED))
			{
				logToFile(LogDelimiters.RANGE_DEC, cur + "");
			}
			else if (skill.equals(Skill.MAGIC))
			{
				logToFile(LogDelimiters.MAGIC_DEC, cur + "");
			}
			else if (skill.equals(Skill.PRAYER))
			{
				logToFile(LogDelimiters.PRAYER_DEC, cur + "");
			}
		}

		lastSkillLevels[skillIdx] = cur;
	}

	@Subscribe
	public void onGameTick(GameTick event)
	{
		checkInNmz();

		if (wasInNmz)
		{
			int currAbsPts = getAbsorptionPts();

			if ((currAbsPts != 0) && (currAbsPts < lastAbsPts) && (currAbsPts <= (lastLoggedAbsPts - 50)))
			{
				lastLoggedAbsPts = currAbsPts;
				logToFile(LogDelimiters.ABSORPTION_DEC, currAbsPts + "");
			}

			if (currAbsPts > lastAbsPts)
			{
				lastLoggedAbsPts = currAbsPts;
			}

			lastAbsPts = currAbsPts;

			checkStatChange(Skill.ATTACK);
			checkStatChange(Skill.STRENGTH);
			checkStatChange(Skill.DEFENCE);
			checkStatChange(Skill.MAGIC);
			checkStatChange(Skill.RANGED);
			checkStatChange(Skill.HITPOINTS);
			checkStatChange(Skill.PRAYER);
			checkInvy();
		}
	}

	void checkInNmz()
	{
		checkInNmz(false);
	}

	void checkInNmz(boolean alwaysLogToFile)
	{
		boolean nowInNmz = isInNightmareZone();
		String delimiter = null;

		if (wasInNmz && !nowInNmz)
		{
			delimiter = LogDelimiters.NMZ_OFF;
			wasInNmz = false;
		}
		else if (!wasInNmz && nowInNmz)
		{
			delimiter = LogDelimiters.NMZ_ON;
			wasInNmz = true;
			init();
		}

		if (delimiter != null)
		{
			logToFile(delimiter);
		}
		else if (alwaysLogToFile)
		{
			logToFile(nowInNmz ? LogDelimiters.NMZ_ON : LogDelimiters.NMZ_OFF);
		}
	}

	private void checkInvy()
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

		String next = tempInvy.trim();

		if (!next.equals(lastInvy))
		{
			logToFile(LogDelimiters.INVY_CHANGED, next);
			lastInvy = next;
		}
	}
}
