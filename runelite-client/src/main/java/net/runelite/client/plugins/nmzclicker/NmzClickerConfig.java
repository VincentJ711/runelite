package net.runelite.client.plugins.nmzclicker;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("nmzClicker")
public interface NmzClickerConfig extends Config
{
	@ConfigItem(
		keyName = "bot",
		name = "Bot",
		description = "this will move around and drink for you when low on something.<br/>" +
			"i recommend turning this on only after you've<br/>rock caked/overloaded down " +
			"and absorptioned up on nmz start",
		position = 5
	)
	default boolean bot()
	{
		return false;
	}

	@ConfigItem(
		keyName = "sound",
		name = "Sound",
		description = "plays when you go beneath a threshold or outside of nmz",
		position = 10
	)
	default boolean sound()
	{
		return true;
	}

	@ConfigItem(
		keyName = "absorption threshold",
		name = "Absorption Threshold",
		description = "drink/play sound when lte this",
		position = 20
	)
	default int absThreshold()
	{
		return 200;
	}

	@ConfigItem(
		keyName = "attack threshold",
		name = "Attack Threshold",
		description = "drink/play sound when lte this",
		position = 30
	)
	default int attackThreshold()
	{
		return 0;
	}

	@ConfigItem(
		keyName = "strength threshold",
		name = "Strength Threshold",
		description = "drink/play sound when lte this",
		position = 40
	)
	default int strengthThreshold()
	{
		return 0;
	}

	@ConfigItem(
		keyName = "defence threshold",
		name = "Defence Threshold",
		description = "drink/play sound when lte this",
		position = 50
	)
	default int defenceThreshold()
	{
		return 0;
	}


	@ConfigItem(
		keyName = "magic threshold",
		name = "Magic Threshold",
		description = "drink/play sound when lte this",
		position = 60
	)
	default int magicThreshold()
	{
		return 0;
	}

	@ConfigItem(
		keyName = "range threshold",
		name = "Range Threshold",
		description = "drink/play sound when lte this",
		position = 70
	)
	default int rangeThreshold()
	{
		return 0;
	}

	@ConfigItem(
		keyName = "prayer threshold",
		name = "Prayer Threshold",
		description = "drink/play sound when lte this",
		position = 80
	)
	default int prayerThreshold()
	{
		return 0;
	}

	@ConfigItem(
		keyName = "skip frequency",
		name = "Skip Frequency /100",
		description = "how often you want to skip a guzzle",
		position = 90
	)
	default int skipFrequency()
	{
		return 15;
	}
}