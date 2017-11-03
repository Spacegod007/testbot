package decision;

import execution.timecheck.VoiceChannelTimeCheck;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.events.guild.GuildJoinEvent;
import net.dv8tion.jda.core.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.util.*;

/**
 * A class that manages all guild actions executed
 */
public class GuildManager extends ListenerAdapter
{
    @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
    private final Map timers;
    private final String autoVoiceChannelCategoryName;

    /**
     * Manages all automated guild interactions
     * @param guilds the bot got added to
     * @param properties containing a "VoiceCreateCategory" key value
     */
    public GuildManager(List<Guild> guilds, Properties properties)
    {
        autoVoiceChannelCategoryName = properties.getProperty("VoiceCreateCategory");

        timers = new HashMap<String, Timer>();

        //make timers for existing guilds
        for (Guild guild : guilds)
        {
            addVoiceChannelTimerCheck(guild);
        }
    }

    @Override
    public void onGuildJoin(GuildJoinEvent event)
    {
        //add channeltimer for new server
        addVoiceChannelTimerCheck(event.getGuild());
        super.onGuildJoin(event);
    }

    @Override
    public void onGuildLeave(GuildLeaveEvent event)
    {
        //remove channeltimer for old server
        removeVoiceChannelTimerCheck(event.getGuild());
        super.onGuildLeave(event);
    }

    /**
     * General method that creates a new timer and adds it to the mapping of guild timers
     * @param guild where the timer is dedicated to
     */
    private void addVoiceChannelTimerCheck(Guild guild)
    {
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new VoiceChannelTimeCheck(guild, autoVoiceChannelCategoryName),0, 2000);
        //noinspection unchecked
        timers.put(guild.getName(), timer);
    }

    private void removeVoiceChannelTimerCheck(Guild guild)
    {
        timers.remove(guild.getName());
    }
}
