package net.runelite.client.plugins.autorun;

import net.runelite.client.config.*;

@ConfigGroup("autorun")
public interface AutoRunConfig extends Config
{

    @ConfigSection(
            keyName = "title",
            name = "Soxs' AutoRun",
            description = "",
            position = 0
    )
    String title = "Soxs' AutoRun";

    @Range(min = 1, max = 50)
    @ConfigItem(
            keyName = "minRun",
            name = "Enable Run (Min.)",
            description = "The minimum run-energy to wait for before re-enabling run.",
            position = 10,
            section = title
    )
    default int minRun()
    {
        return 5;
    }


    @Range(min = 2, max = 99)
    @ConfigItem(
            keyName = "maxRun",
            name = "Enable Run (Max.)",
            description = "The maximum run-energy to wait for before re-enabling run.",
            position = 12,
            section = title
    )
    default int maxRun()
    {
        return 35;
    }


}
