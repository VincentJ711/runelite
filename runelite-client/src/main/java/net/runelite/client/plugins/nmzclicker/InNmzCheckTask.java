package net.runelite.client.plugins.nmzclicker;

import java.util.TimerTask;

public class InNmzCheckTask extends TimerTask
{
	NmzClickerPlugin plugin;

	public InNmzCheckTask(NmzClickerPlugin plugin)
	{
		this.plugin = plugin;
	}

	public void run()
	{
		plugin.checkInvy(true);
		plugin.checkInNmz(true);
	}
}
