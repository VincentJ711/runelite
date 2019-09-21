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
		description = "have the script do the work for you",
		position = 5
	)
	default boolean bot()
	{
		return false;
	}

	@ConfigItem(
		keyName = "soundType",
		name = "Sound Type",
		description = "",
		position = 10
	)
	default SoundType soundType()
	{
		return SoundType.NONE;
	}

	@ConfigItem(
		keyName = "absorption threshold",
		name = "Absorption Threshold",
		description = "",
		position = 20
	)
	default int absThreshold()
	{
		return 200;
	}

	@ConfigItem(
		keyName = "attack threshold",
		name = "Attack Threshold",
		description = "",
		position = 30
	)
	default int attackThreshold()
	{
		return 0;
	}

	@ConfigItem(
		keyName = "strength threshold",
		name = "Strength Threshold",
		description = "",
		position = 40
	)
	default int strengthThreshold()
	{
		return 0;
	}

	@ConfigItem(
		keyName = "defence threshold",
		name = "Defence Threshold",
		description = "",
		position = 50
	)
	default int defenceThreshold()
	{
		return 0;
	}


	@ConfigItem(
		keyName = "magic threshold",
		name = "Magic Threshold",
		description = "",
		position = 60
	)
	default int magicThreshold()
	{
		return 0;
	}

	@ConfigItem(
		keyName = "range threshold",
		name = "Range Threshold",
		description = "",
		position = 70
	)
	default int rangeThreshold()
	{
		return 0;
	}

	@ConfigItem(
		keyName = "prayer threshold",
		name = "Prayer Threshold",
		description = "",
		position = 80
	)
	default int prayerThreshold()
	{
		return 0;
	}

	@ConfigItem(
		keyName = "skip frequency(0-99)",
		name = "Skip Frequency",
		description = "how often you want to skip a guzzle",
		position = 90
	)
	default int skipFrequency()
	{
		return 15;
	}
}