package net.runelite.client.plugins.soxsautoclicker;


import com.google.inject.Provides;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.Point;
import net.runelite.api.events.GameTick;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.input.KeyManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.util.HotkeyListener;
import org.jetbrains.annotations.NotNull;
import org.pf4j.Extension;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Extension
@PluginDescriptor(
	name = "Soxs' AutoClicker",
	description = "Auto clicks. Lovely to pair with 1-click plugins.",
	tags = {"clicker", "auto", "autoclicker", "auto clicker", "soxs"},
	enabledByDefault = true,
	hidden = false
)
@Slf4j
@SuppressWarnings("unused")
@Singleton
public class AutoClickerPlugin extends Plugin
{
	@Inject
	private Client client;

	@Inject
	private AutoClickerConfig config;

	@Inject
	private KeyManager keyManager;

	private ExecutorService executorService;
	private Point point;
	private Random random;
	private boolean run;


	@Provides
	AutoClickerConfig getConfig(ConfigManager configManager)
	{
		return configManager.getConfig(AutoClickerConfig.class);
	}

	@Inject
	ReflectBreakHandler breakHandler;

	@Override
	protected void startUp()
	{
		keyManager.registerKeyListener(hotkeyListener);
		executorService = Executors.newSingleThreadExecutor();
		random = new Random();
		breakHandler.registerPlugin(this);
	}

	@Override
	protected void shutDown()
	{
		keyManager.unregisterKeyListener(hotkeyListener);
		executorService.shutdown();
	}

	public Plugin getClickerPlugin() {
		return this;
	}

	private final HotkeyListener hotkeyListener = new HotkeyListener(() -> config.toggle())
	{
		@Override
		public void hotkeyPressed()
		{
			run = !run;

			if (!run)
			{
				breakHandler.stopPlugin(getClickerPlugin());
				return;
			} else {
				breakHandler.startPlugin(getClickerPlugin());
			}

			point = client.getMouseCanvasPosition();
			executorService.submit(() ->
			{
				while (run)
				{
					if (breakHandler.isBreakActive(getClickerPlugin()))
					{
						return;
					}

					if (breakHandler.shouldBreak(getClickerPlugin()))
					{
						breakHandler.startBreak(getClickerPlugin());
					}

					if (random.nextInt(100) < config.frequencyAFK())
					{
						try
						{
							Thread.sleep(randWeightedInt(config.minDelayAFK(), config.maxDelayAFK()));
						}
						catch (InterruptedException e)
						{
							e.printStackTrace();
						}
					}


					if (client.getGameState() == GameState.LOGGED_IN)
					{
						click(point);
					}

					try
					{
						Thread.sleep(getBetweenClicksDelay());
					}
					catch (InterruptedException e)
					{
						e.printStackTrace();
					}
				}
				breakHandler.stopPlugin(getClickerPlugin());
			});
		}
	};

	public long getBetweenClicksDelay() {
			return config.weightedDistribution() ?
					(long) clamp((-Math.log(Math.abs(random.nextGaussian()))) * config.deviation() + config.target())
					:
					(long) clamp(Math.round(random.nextGaussian() * config.deviation() + config.target()));
	}

	private double clamp(double val)
	{
		return Math.max(config.minDelay(), Math.min(config.maxDelay(), val));
	}

	public void click(Point p)
	{
		assert !client.isClientThread();

		if (client.isStretchedEnabled())
		{
			final Dimension stretched = client.getStretchedDimensions();
			final Dimension real = client.getRealDimensions();
			final double width = (stretched.width / real.getWidth());
			final double height = (stretched.height / real.getHeight());
			final Point point = new Point((int) (p.getX() * width), (int) (p.getY() * height));
			mouseEvent(501, point);
			mouseEvent(502, point);
			mouseEvent(500, point);
			return;
		}
		mouseEvent(501, p);
		mouseEvent(502, p);
		mouseEvent(500, p);
	}

	private void mouseEvent(int id, @NotNull Point point)
	{
		MouseEvent e = new MouseEvent(
				client.getCanvas(), id,
				System.currentTimeMillis(),
				0, point.getX(), point.getY(),
				1, false, 1
		);

		client.getCanvas().dispatchEvent(e);
	}

	private int randWeightedInt(int min, int max) {
		int ra = randBellWeight(min, max);
		int sorted = Math.min(max, Math.max(min, ra));
		if (min >= 0 && max > 0)
			return Math.abs(sorted);
		else
			return sorted;
	}

	private int randBellWeight(int min, int max) {
		if (max <= min)
			max = min + 1;
		return (int) nextSkewedBoundedDouble(min, max, config.weightSkewAFK() / 10d, config.weightBiasAFK() / 10d);
	}

	private double nextSkewedBoundedDouble(double min, double max, double skew, double bias) {
		double range = max - min;
		double mid = min + range / 2.0;
		double unitGaussian = random.nextGaussian();
		double biasFactor = Math.exp(bias);
		return mid + (range * (biasFactor / (biasFactor + Math.exp(-unitGaussian / skew)) - 0.5));
	}

}
