package net.runelite.client.plugins.wildylevellines;

import java.util.ArrayList;

public class WildyLines extends ArrayList<WildyLine>
{
	WildyLines()
	{
		super();

		// main wilderness above ground
		add(new WildyLine(0, 2944, 3392, 3525));
		for (int i = 2; i <= 56; i++)
		{
			add(new WildyLine(0, 2944, 3392, 3528 + (i - 2) * 8));
		}

		// edge dungeon
		for (int i = 9920; i <= 9999; i += 8)
		{
			add(new WildyLine(0, 3077, 3135, i));
		}

		// rev cave
		for (int i = 10056; i <= 10234; i += 8)
		{
			add(new WildyLine(0, 3143, 3264, i));
		}

		// wildy gwd top
		for (int i = 10144; i <= 10165; i += 8)
		{
			add(new WildyLine(3, 3047, 3071, i));
		}

		// wildy gwd bottom
		for (int i = 10120; i <= 10167; i += 8)
		{
			add(new WildyLine(0, 3014, 3068, i));
		}
	}
}