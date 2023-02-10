package dev.zawarudo.holo.general;

import dev.zawarudo.holo.annotations.Command;
import dev.zawarudo.holo.core.AbstractCommand;
import dev.zawarudo.holo.core.CommandCategory;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Command(name = "serverroles",
		description = "Shows all the roles of the server",
		category = CommandCategory.GENERAL)
public class ServerRolesCmd extends AbstractCommand {

	@Override
	public void onCommand(@NotNull MessageReceivedEvent e) {
		deleteInvoke(e);
		
		EmbedBuilder builder = new EmbedBuilder();
		builder.setTitle("Roles of " + e.getGuild().getName());
		
		List<Role> roles = e.getGuild().getRoles();

		if (roles.isEmpty()) {
			builder.setDescription("This server doesn't have any roles");
			sendEmbed(e, builder, true, 1, TimeUnit.MINUTES);
			return;
		}
		
		StringBuilder s = new StringBuilder();
		int counter = 0;
		
		for (Role r : roles) {
			String role = r.getAsMention() + "\n(" + r.getId() + ")";
			if (s.length() + role.length() > 1024) {
				builder.addField(String.valueOf(counter++), s.toString(), true);
				s = new StringBuilder(role + "\n");
			} else {
				s.append(role).append("\n");
			}
		}
		builder.addField(String.valueOf(counter + 1), s.toString(), true);
		sendEmbed(e, builder, true, 2, TimeUnit.MINUTES);
	}
}