package dev.zawarudo.holo.commands.music.cmds;

import dev.zawarudo.holo.utils.annotations.Command;
import dev.zawarudo.holo.commands.CommandCategory;
import dev.zawarudo.holo.commands.music.AbstractMusicCommand;
import dev.zawarudo.holo.commands.music.GuildMusicManager;
import dev.zawarudo.holo.commands.music.PlayerManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

@Command(name = "stop",
		description = "Stops the current song and clears the queue.",
		ownerOnly = true,
		category = CommandCategory.MUSIC)
public class StopCmd extends AbstractMusicCommand {

	@Override
	public void onCommand(@NotNull MessageReceivedEvent event) {
		deleteInvoke(event);
		
		EmbedBuilder builder = new EmbedBuilder();
		GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(event.getGuild());

		if (musicManager.scheduler.audioPlayer.getPlayingTrack() == null && musicManager.scheduler.queue.isEmpty()) {
			builder.setTitle("Error");
			builder.setDescription("I'm currently idle!");
			sendEmbed(event, builder, false, 15, TimeUnit.SECONDS);
			return;
		}

		musicManager.clear();
		
		builder.setTitle("Success");
		builder.setDescription("Stopped current track and cleared queue!");
		sendEmbed(event, builder, false, 15, TimeUnit.SECONDS);
	}
}