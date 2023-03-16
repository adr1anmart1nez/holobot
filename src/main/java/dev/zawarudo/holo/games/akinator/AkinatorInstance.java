package dev.zawarudo.holo.games.akinator;

import com.github.markozajc.akiwrapper.Akiwrapper;
import com.github.markozajc.akiwrapper.AkiwrapperBuilder;
import com.github.markozajc.akiwrapper.core.exceptions.ServerNotFoundException;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import dev.zawarudo.holo.exceptions.APIException;
import dev.zawarudo.holo.misc.EmbedColor;
import dev.zawarudo.holo.misc.Emote;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.ArrayList;
import java.util.List;

public class AkinatorInstance {

    private final Akiwrapper akiwrapper;
    private final EventWaiter waiter;
    private final MessageReceivedEvent event;

    private boolean isFinished = false;
    private final List<String> wrongGuesses = new ArrayList<>();

    public AkinatorInstance(MessageReceivedEvent event, EventWaiter waiter) throws APIException {
        this.event = event;
        this.waiter = waiter;

        try {
            akiwrapper = new AkiwrapperBuilder().build();
        } catch (ServerNotFoundException e) {
            throw new APIException(e.getMessage(), e);
        }
    }

    /**
     * Starts the new Akinator instance and begins to ask questions in the respective channel.
     */
    public void start() {
        EmbedBuilder embedBuilder = new EmbedBuilder() {{
            setTitle("Akinator");
            setThumbnail(AkinatorSprite.ICON);
            setColor(EmbedColor.AKINATOR.getColor());

            String description = String.format("To start the game, please think about a real or fictional " +
                    "character. I will try to guess who it is by asking some questions.\nIf you are ready, " +
                    "please react with %s, or if you want to cancel the game, react with %s.",
                    Emote.TICK.getAsText(), Emote.CROSS.getAsText());
            setDescription(description);
        }};

        Message msg = event.getChannel().sendMessageEmbeds(embedBuilder.build()).complete();
    }

    /**
     * Removes all reactions. This method is called when Akinator makes a guess, when
     * the correct character was guessed or if the user beat him.
     */
    private void cleanup() {

    }

    private void addStartReactions(Message msg) {
        List<Emote> reactions = List.of(Emote.TICK, Emote.CROSS);
        for (Emote reaction : reactions) {
            msg.addReaction(reaction.getAsEmoji()).queue(v -> {}, e -> {});
        }
    }

    private void addInGameReactions(Message msg) {
        List<Emote> reactions = List.of(Emote.ONE, Emote.TWO, Emote.THREE, Emote.FOUR, Emote.FIVE, Emote.UNDO, Emote.CROSS);
        for (Emote reaction : reactions) {
            msg.addReaction(reaction.getAsEmoji()).queue(v -> {}, e -> {});
        }
    }

    private void addGuessReactions(Message msg) {
        List<Emote> reactions = List.of(Emote.TICK, Emote.CONTINUE, Emote.CROSS);
        for (Emote reaction : reactions) {
            msg.addReaction(reaction.getAsEmoji()).queue(v -> {}, e -> {});
        }
    }
}