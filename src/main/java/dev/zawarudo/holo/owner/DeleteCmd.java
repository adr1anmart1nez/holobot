package dev.zawarudo.holo.owner;

import dev.zawarudo.holo.annotations.Command;
import dev.zawarudo.holo.core.AbstractCommand;
import dev.zawarudo.holo.core.CommandCategory;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

@Command(name = "delete",
		description = "Deletes a message of your choice. This works by either passing the message id or replying to a message.",
		usage = "[msg id]",
		alias = {"d"},
		ownerOnly = true,
		category = CommandCategory.OWNER)
public class DeleteCmd extends AbstractCommand {

	@Override
	public void onCommand(@NotNull MessageReceivedEvent e) {
		deleteInvoke(e);
		EmbedBuilder builder = new EmbedBuilder();

		// Delete message user is replying to
		if (e.getMessage().getReferencedMessage() != null) {
			e.getMessage().getReferencedMessage().delete().queue();
			return;
		}
		
		// No argument was given
		if (args.length != 1) {
			builder.setTitle("Incorrect Usage");
			builder.setDescription("Please only provide the id of the message you want to delete!");
			sendToOwner(e, builder);
			return;
		}

		long id;

		try {
			id = Long.parseLong(args[0]);
		} catch (NumberFormatException ex) {
			builder.setTitle("Error");
			builder.setDescription("Please provide the id of the message you want to delete!");
			sendToOwner(e, builder);
			return;
		}

		e.getChannel().retrieveMessageById(id).complete().delete().queue(v -> {}, err -> {});
	}
}