package net.runelite.client.plugins.wildylevellines;

import java.awt.Color;
import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("wildyLevelLines")
public interface WildyLevelLinesConfig extends Config
{
	@ConfigItem(
		keyName = "lineColor",
		name = "line color",
		description = "pick a color for the color of the wildy lines",
		position = 1
	)
	default Color lineColor()
	{
		return Color.ORANGE;
	}
}
