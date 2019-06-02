package net.runelite.client.plugins.wildylevellines;

import net.runelite.client.eventbus.Subscribe;
import com.google.inject.Provides;
import java.awt.Rectangle;
import java.awt.geom.GeneralPath;
import javax.inject.Inject;
import lombok.Getter;
import net.runelite.api.Client;
import net.runelite.api.Constants;
import net.runelite.api.GameState;
import net.runelite.api.Perspective;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.events.ConfigChanged;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.geometry.Geometry;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;
import lombok.extern.slf4j.Slf4j;

@PluginDescriptor(
	name = "Wildy Level Lines",
	description = "Displays a line for each wildy level.",
	tags = {"wildy", "level", "lines"}
)
@Slf4j
public class WildyLevelLinesPlugin extends Plugin
{
	@Inject
	private Client client;

	@Inject
	private ClientThread clientThread;

	@Inject
	private WildyLevelLinesConfig config;

	@Inject
	private WildyLevelLinesOverlay overlay;

	@Inject
	private OverlayManager overlayManager;

	@Getter
	private GeneralPath[] paths;

	private int currentPlane;

	private WildyLines lines;

	@Provides
	WildyLevelLinesConfig getConfig(ConfigManager configManager)
	{
		return configManager.getConfig(WildyLevelLinesConfig.class);
	}

	@Override
	protected void startUp() throws Exception
	{
		overlayManager.add(overlay);
		lines = new WildyLines();
		paths = new GeneralPath[Constants.MAX_Z];
		clientThread.invokeLater(() ->
		{
			if (client.getGameState() == GameState.LOGGED_IN)
			{
				findLinesInScene();
				return true;
			}
			else
			{
				return false;
			}
		});
	}

	@Override
	protected void shutDown() throws Exception
	{
		overlayManager.remove(overlay);
		paths = null;
		lines = null;
	}

	private void transformWorldToLocal(float[] coords)
	{
		LocalPoint lp = LocalPoint.fromWorld(client, (int)coords[0], (int)coords[1]);
		coords[0] = lp.getX() - Perspective.LOCAL_TILE_SIZE / 2;
		coords[1] = lp.getY() - Perspective.LOCAL_TILE_SIZE / 2;
	}

	private void findLinesInScene()
	{
		Rectangle sceneRect = new Rectangle(
			client.getBaseX() + 1, client.getBaseY() + 1,
			Constants.SCENE_SIZE - 2, Constants.SCENE_SIZE - 2);

		for (int i = 0; i < paths.length; i++)
		{
			paths[i] = null;
		}

		for (WildyLine l : lines)
		{
			if (l.intersectsRectangle(sceneRect))
			{
				final WildyLine innerLine = l.subtractFromRectangle(sceneRect);
				final int p = innerLine.getPlane();

				if (paths[p] == null)
				{
					paths[p] = new GeneralPath();
				}

				paths[p].moveTo(innerLine.getX1(), innerLine.getY1());
				paths[p].lineTo(innerLine.getX2(), innerLine.getY2());
			}
		}

		for (int i = 0; i < paths.length; i++)
		{
			if (paths[i] != null)
			{
				log.info(i + "");
				paths[i] = Geometry.transformPath(paths[i], this::transformWorldToLocal);
			}
		}
	}

	@Subscribe
	public void onConfigChanged(ConfigChanged event)
	{
		log.info("config changed");
	}

	@Subscribe
	public void onGameStateChanged(GameStateChanged event)
	{
		if (event.getGameState() == GameState.LOGGED_IN)
		{
			findLinesInScene();
		}
	}
}
