package net.runelite.client.plugins.soxsautoclicker;

import net.runelite.client.config.*;

@ConfigGroup("soxsautoclicker")
public interface AutoClickerConfig extends Config
{

    @ConfigSection(
            keyName = "title",
            name = "Soxs' AutoClicker",
            description = "",
            position = 0
    )
    String title = "Soxs' AutoClicker";

    @ConfigItem(
            keyName = "toggle",
            name = "Toggle",
            description = "Toggles the auto-clicker.",
            position = 1,
            section = title
    )
    default Keybind toggle()
    {
        return Keybind.NOT_SET;
    }

    @ConfigSection(
            keyName = "clickerConfig",
            name = "Clicker Config",
            description = "",
            position = 4
    )
    String clickerConfig = "Clicker Config";

    @ConfigItem(
            keyName = "mainClickerActive",
            name = "Main Clicker Active",
            description = "Whether the main interval clicker is active",
            position = 5,
            section = clickerConfig
    )
    default boolean mainClickerActive()
    {
        return true;
    }

    @ConfigItem(
            keyName = "minDelay",
            name = "Minimum Delay (ms)",
            description = "Minimum delay between mouse clicks.",
            position = 6,
            section = clickerConfig
    )
    default int minDelay()
    {
        return 1000;
    }

    @ConfigItem(
            keyName = "maxDelay",
            name = "Maximum Delay (ms)",
            description = "Maximum delay between mouse clicks.",
            position = 7,
            section = clickerConfig
    )
    default int maxDelay()
    {
        return 2000;
    }

    @ConfigItem(
            keyName = "target",
            name = "Delay Target",
            description = "",
            position = 8,
            section = clickerConfig
    )
    default int target()
    {
        return 1500;
    }

    @ConfigItem(
            keyName = "deviation",
            name = "Delay Deviation",
            description = "",
            position = 9,
            section = clickerConfig
    )
    default int deviation()
    {
        return 100;
    }

    @ConfigItem(
            keyName = "weightedDistribution",
            name = "Weighted Distribution",
            description = "Shifts the random distribution towards the lower end at the target, otherwise it will be an even distribution",
            position = 10,
            section = clickerConfig
    )
    default boolean weightedDistribution()
    {
        return false;
    }

    @ConfigItem(
            keyName = "followMouse",
            name = "Follow Mouse",
            description = "Click at the mouse location.",
            position = 12,
            section = clickerConfig
    )
    default boolean followMouse()
    {
        return true;
    }

    @ConfigItem(
            keyName = "disableRealMouse",
            name = "Disable Real Mouse",
            description = "Disable the real mouse after the clicker has started, to prevent interference after setting it up.",
            position = 14,
            section = clickerConfig
    )
    default boolean disableRealMouse()
    {
        return true;
    }


    @ConfigItem(
            keyName = "chinBreakHandler",
            name = "ChinBreakHandler",
            description = "Enable Chin Break Handler",
            position = 15,
            section = clickerConfig
    )
    default boolean chinBreakHandler()
    {
        return false;
    }

    @ConfigItem(
            keyName = "disableUI",
            name = "Disable UI",
            description = "",
            position = 16,
            section = clickerConfig
    )
    default boolean disableUI()
    {
        return false;
    }

    @ConfigSection(
            keyName = "clickerFilters",
            name = "Clicker Filters",
            description = "",
            position = 17
    )
    String clickerFilters = "Clicker Filters";

    @ConfigItem(
            keyName = "skipOnMoving",
            name = "Skip When Moving",
            description = "",
            position = 18,
            section = clickerFilters
    )
    default boolean skipOnMoving()
    {
        return false;
    }

    @ConfigItem(
            keyName = "skipOnInteraction",
            name = "Skip On Interaction",
            description = "",
            position = 19,
            section = clickerFilters
    )
    default boolean skipOnInteraction()
    {
        return false;
    }

    @ConfigItem(
            keyName = "skipOnAnimating",
            name = "Skip On Animating",
            description = "",
            position = 20,
            section = clickerFilters
    )
    default boolean skipOnAnimating()
    {
        return false;
    }


    @ConfigItem(
            keyName = "mouseOnNPC",
            name = "Mouse On NPC",
            description = "",
            position = 22,
            section = clickerFilters
    )
    default boolean mouseOnNPC()
    {
        return false;
    }

    @ConfigItem(
            keyName = "mouseOnNPCID",
            name = "NPC ID",
            description = "",
            position = 23,
            section = clickerFilters,
            hidden = true,
            unhide = "mouseOnNPC"
    )
    default int mouseOnNPCID()
    {
        return 0;
    }

    @ConfigItem(
        keyName = "mouseOnGameObject",
        name = "Mouse On Game Object",
        description = "",
        position = 24,
        section = clickerFilters
    )
    default boolean mouseOnGameObject()
    {
        return false;
    }

    @ConfigItem(
        keyName = "mouseOnGameObjectID",
        name = "Game Object ID",
        description = "",
        position = 25,
        section = clickerFilters,
        hidden = true,
        unhide = "mouseOnGameObject"
    )
    default int mouseOnGameObjectID()
    {
        return 0;
    }



    @ConfigSection(
            keyName = "clickOn",
            name = "Click On Event",
            description = "",
            position = 35
    )
    String clickOn = "Click On Event";

    @ConfigItem(
            keyName = "eventMinDelay",
            name = "Minimum Delay (ms)",
            description = "Minimum event click delay.",
            position = 36,
            section = clickOn
    )
    default int eventMinDelay()
    {
        return 100;
    }

    @ConfigItem(
            keyName = "eventMaxDelay",
            name = "Maximum Delay (ms)",
            description = "Maximum event click delay.",
            position = 37,
            section = clickOn
    )
    default int eventMaxDelay()
    {
        return 250;
    }

    @Range(
            min = 5,
            max = 9
    )
    @ConfigItem(
            keyName = "eventWeightSkew",
            name = "Skew (Tightness)",
            description = "The degree to which the event click delay random weights cluster around the mode of the distribution; higher values mean tighter clustering.",
            position = 38,
            section = clickOn
    )
    default int eventWeightSkew()
    {
        return 8;
    }

    @Range(
            min = -10,
            max = 10
    )
    @ConfigItem(
            keyName = "eventWeightBias",
            name = "Bias (Offset)",
            description = "The tendency of the AFK mode to reach the min, max or midpoint value; positive values bias toward max, negative values toward min.",
            position = 39,
            section = clickOn
    )
    default int eventWeightBias()
    {
        return 8;
    }

    @ConfigItem(
            keyName = "clickOnAnimation",
            name = "Click on Animation",
            description = "",
            position = 40,
            section = clickOn
    )
    default boolean clickOnAnimation()
    {
        return false;
    }

    @ConfigItem(
            keyName = "clickOnAnimationID",
            name = "Animation ID",
            description = "",
            position = 41,
            section = clickOn,
            hidden = true,
            unhide = "clickOnAnimation"
    )
    default int clickOnAnimationID()
    {
        return 713;
    }









    @ConfigSection(
            keyName = "afkDelayTitle",
            name = "Random AFK",
            description = "",
            position = 50
    )
    String afkDelayTitle = "Random AFK";

    @Range(
            min = 0,
            max = 99
    )
    @ConfigItem(
            keyName = "frequencyAFK",
            name = "AFK Frequency (%)",
            description = "% chance to go AFK.",
            position = 51,
            section = afkDelayTitle
    )
    default int frequencyAFK()
    {
        return 3;
    }

    @ConfigItem(
            keyName = "minDelayAFK",
            name = "Minimum Delay (ms)",
            description = "Minimum AFK delay.",
            position = 52,
            section = afkDelayTitle
    )
    default int minDelayAFK()
    {
        return 5000;
    }

    @ConfigItem(
            keyName = "maxDelayAFK",
            name = "Maximum Delay (ms)",
            description = "Maximum AFK delay.",
            position = 53,
            section = afkDelayTitle
    )
    default int maxDelayAFK()
    {
        return 20000;
    }


    @Range(
            min = 5,
            max = 9
    )
    @ConfigItem(
            keyName = "weightSkewAFK",
            name = "AFK Skew (Tightness)",
            description = "The degree to which the AFK random weights cluster around the mode of the distribution; higher values mean tighter clustering.",
            position = 54,
            section = afkDelayTitle
    )
    default int weightSkewAFK()
    {
        return 8;
    }

    @Range(
            min = -10,
            max = 10
    )
    @ConfigItem(
            keyName = "weightBiasAFK",
            name = "AFK Bias (Offset)",
            description = "The tendency of the AFK mode to reach the min, max or midpoint value; positive values bias toward max, negative values toward min.",
            position = 55,
            section = afkDelayTitle
    )
    default int weightBiasAFK()
    {
        return 8;
    }

    @ConfigItem(
            keyName = "minimumWaitBetweenAFK",
            name = "Minimum Wait Between AFK (ms)",
            description = "Minimum amount of time to wait before going AFK again.",
            position = 56,
            section = afkDelayTitle
    )
    default int minimumWaitBetweenAFK()
    {
        return 60000;
    }



    @ConfigSection(
            keyName = "fatigueTitle",
            name = "Fatigue Settings",
            description = "",
            position = 69
    )
    String fatigueTitle = "Fatigue Settings";

    @ConfigItem(
            keyName = "useFatigue",
            name = "Enable Fatigue",
            description = "Decreases clicker efficiency over time.",
            position = 70,
            section = fatigueTitle
    )
    default boolean useFatigue()
    {
        return false;
    }

    @Range(
            min = 0,
            max = 99
    )
    @ConfigItem(
            keyName = "frequencyFatigue",
            name = "Increase Frequency (%)",
            description = "% chance to increase the amount of fatigue.",
            position = 71,
            section = fatigueTitle
    )
    default int frequencyFatigue()
    {
        return 1;
    }

    @Range(
            min = 0,
            max = 99
    )
    @ConfigItem(
            keyName = "resetFatigue",
            name = "Reset Chance (%)",
            description = "% chance to reset fatigue to 0.",
            position = 72,
            section = fatigueTitle
    )
    default int resetFatigue()
    {
        return 1;
    }

    @ConfigItem(
            keyName = "minFatigue",
            name = "Minimum Fatigue (ms)",
            description = "Minimum amount to increase the fatigue delay by.",
            position = 73,
            section = fatigueTitle
    )
    default int minFatigue()
    {
        return 1;
    }

    @ConfigItem(
            keyName = "maxFatigue",
            name = "Maximum Fatigue (ms)",
            description = "Maximum amount to increase the fatigue delay by.",
            position = 74,
            section = fatigueTitle
    )
    default int maxFatigue()
    {
        return 10;
    }

    @ConfigItem(
            keyName = "targetFatigue",
            name = "Fatigue Target",
            description = "",
            position = 75,
            section = fatigueTitle
    )
    default int targetFatigue()
    {
        return 5;
    }

    @ConfigItem(
            keyName = "deviationFatigue",
            name = "Fatigue Deviation",
            description = "",
            position = 76,
            section = fatigueTitle
    )
    default int deviationFatigue()
    {
        return 4;
    }


}
