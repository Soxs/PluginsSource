package net.runelite.client.plugins.autologhop;

import net.runelite.client.config.*;

@ConfigGroup("autologhop")
public interface AutoLogHopConfig extends Config
{

    @ConfigSection(
            keyName = "title",
            name = "Soxs' AutoLogHop",
            description = "",
            position = 0
    )
    String title = "Soxs' AutoLogHop";

    @ConfigItem(
            keyName = "hop",
            name = "Hop Worlds",
            description = "Hop world instead of log out.",
            position = 10,
            section = title
    )
    default boolean hop()
    {
        return false;
    }


    @ConfigItem(
            keyName = "disableWildyChecks",
            name = "Disable Wilderness Checks",
            description = "Disable wilderness checks. Makes plugin work everywhere.",
            position = 12,
            section = title
    )
    default boolean disableWildyChecks()
    {
        return false;
    }

    @ConfigItem(
            keyName = "whitelist",
            name = "Whitelist",
            description = "Players to ignore - separate with ,",
            position = 14,
            section = title
    )
    default String whitelist()
    {
        return "";
    }

}
