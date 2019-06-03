package net.runelite.client.plugins.wildylevellines;

public class MainWildyLine extends WildyLine
{
	private static int LEFT_BOUND = 2944;
	private static int RIGHT_BOUND = 3392;

	MainWildyLine(int longitude)
	{
		super(0, LEFT_BOUND, RIGHT_BOUND, longitude);
	}
}
