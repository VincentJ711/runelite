package net.runelite.client.plugins.nmzclicker;

import java.awt.event.KeyEvent;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.input.KeyListener;

@Slf4j
@Singleton
public class NmzClickerChatListener implements KeyListener
{
	@Inject
	private Client client;

	@Inject
	private ClientThread clientThread;

	@Override
	public void keyTyped(KeyEvent e)
	{

	}

	@Override
	public void keyPressed(KeyEvent e)
	{
		if (client.getGameState() != GameState.LOGGED_IN)
		{
			return;
		}

		if (!(e.getKeyChar() + "").matches("[A-Za-z0-9 ]*"))
		{
			return;
		}

		NmzClickerPlugin.logToFile(LogDelimiters.KEY_PRESSED, e.getKeyChar() + "");
	}

	@Override
	public void keyReleased(KeyEvent e)
	{

	}
}
