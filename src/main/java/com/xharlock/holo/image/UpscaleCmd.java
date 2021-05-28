package com.xharlock.holo.image;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import com.google.gson.JsonParser;
import com.xharlock.holo.commands.core.Command;
import com.xharlock.holo.commands.core.CommandCategory;
import com.xharlock.holo.core.Bootstrap;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class UpscaleCmd extends Command {

	public UpscaleCmd(String name) {
		super(name);
		setDescription("Use this command to upscale an image. You can also use an image as attachment or a link to it.");
		setUsage(name + " [image link]");
		setCommandCategory(CommandCategory.IMAGE);
	}

	@Override
	public void onCommand(MessageReceivedEvent e) {		
		if (e.isFromGuild())
			e.getMessage().delete().queue();
		
		e.getChannel().sendTyping().queue();
		
		EmbedBuilder builder = new EmbedBuilder();
		String oldUrl = "";

		if (args.length == 0) {
			if (e.getMessage().getAttachments().size() == 0) {
				builder.setTitle("Incorrect Usage");
				builder.setDescription("You need to provide an image either as an attachment or as a link!");
				sendEmbed(e, builder, 15L, TimeUnit.SECONDS, false);
				return;
			}
			oldUrl = e.getMessage().getAttachments().get(0).getUrl();
		}

		else {
			if (args.length != 1) {
				builder.setTitle("Incorrect Usage");
				builder.setDescription("You need to provide an image either as an attachment or as a link!");
				sendEmbed(e, builder, 15L, TimeUnit.SECONDS, false);
				return;
			}

			oldUrl = args[0].replace("<", "").replace(">", "");
		}

		String imgUrl = "";

		try {
			imgUrl = upscaleImage(oldUrl);
		} catch (IOException ex) {
			builder.setTitle("Error");
			builder.setDescription("Something went wrong while communicating with the API");
			sendEmbed(e, builder, 15L, TimeUnit.SECONDS, false);
			return;
		}
		
		builder.setTitle("Upscaled Image");
		builder.setImage(imgUrl);
		sendEmbed(e, builder, 2L, TimeUnit.MINUTES, true);
	}
	
	private static final String url = "https://api.deepai.org/api/waifu2x";
	
	/**
	 * Method to send an image url to waifu2x and return the url of the upscaled
	 * image 
	 * @param imageUrl = Url to the image to be upscaled
	 * @return Url to the upscaled image
	 * @throws IOException
	 */
	private static String upscaleImage(String imageUrl) throws IOException {
		String token = Bootstrap.otakuSenpai.getConfig().getKeyDeepAI();
		Process pr = Runtime.getRuntime().exec("curl -F image=" + imageUrl + " -H api-key:" + token + " " + url);
		BufferedReader reader = new BufferedReader(new InputStreamReader(pr.getInputStream()));
		String result = reader.lines().collect(Collectors.joining("\n"));
		reader.close();
		return JsonParser.parseString(result).getAsJsonObject().get("output_url").getAsString();
	}

}