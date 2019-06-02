package net.runelite.client.plugins.wildylevellines;

import java.util.ArrayList;

public class WildyLines extends ArrayList<WildyLine>
{
	WildyLines()
	{
		super();
		add(new MainWildyLine(3525));
		add(new MainWildyLine(3528));

		for (int i = 3; i <= 56; i++)
		{
			add(new MainWildyLine(3528 + (i - 2) * 8));
		}
	}
}