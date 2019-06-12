package com.github.maxopoly.kira.util;

import java.util.function.Consumer;

import com.github.maxopoly.kira.KiraMain;
import com.github.maxopoly.kira.user.KiraUser;

import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.PrivateChannel;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;

public class DiscordMessageDivider {

	private static final int allowedLength = 1950;

	public static void sendTextChannelMessage(KiraUser user, TextChannel channel, String msg) {
		sendMessageInternal(user, channel.getGuild(), s -> {
			channel.sendMessage(s).queue();
		}, msg);
	}

	public static void sendPrivateMessage(KiraUser user, String msg) {
		JDA jda = KiraMain.getInstance().getJDA();
		User discordUser = jda.getUserById(user.getDiscordID());
		PrivateChannel pm = discordUser.openPrivateChannel().complete();
		sendMessageInternal(null, null, s -> {
			pm.sendMessage(s).queue();
		}, msg);
	}

	/**
	 * Splits up arbitrary messages into ones not exceeding the character limit of
	 * 2000 per message and sends them
	 * 
	 * @param user     User to ping at the beginning of each message, may be null
	 *                 for no ping
	 * @param guild    Guild sending to. May be null, but must not be null when
	 *                 pinging a user
	 * @param receiver Consumer for actually sending the message. Having a consumer
	 *                 here allows using this for both pms and text channels
	 * @param msg      Message to send
	 */
	private static void sendMessageInternal(KiraUser user, Guild guild, Consumer<String> receiver, String msg) {
		String tag = "";
		if (guild != null && user != null) {
			Member member = guild.getMemberById(user.getDiscordID());
			if (member != null) {
				tag = member.getAsMention() + "\n";
			}
			if (msg.length() + tag.length() > allowedLength) {
				receiver.accept(tag + msg);
				return;
			}
		}
		int allowedLengthWithoutTag = allowedLength - tag.length();
		String[] split = msg.split("\n");
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < split.length; i++) {
			String currString = split[i];
			if (sb.length() + currString.length() > allowedLengthWithoutTag) {
				if (sb.length() == 0) {
					if (currString.length() > allowedLengthWithoutTag) {
						int beginIndex = 0;
						while (beginIndex != currString.length()) {
							int endIndex = Math.min(beginIndex + allowedLengthWithoutTag, currString.length());
							String subPart = currString.substring(beginIndex, endIndex);
							receiver.accept(tag + subPart);
							beginIndex = endIndex;
						}
					} else {
						receiver.accept(tag + currString);
					}
				} else {
					receiver.accept(sb.toString());
					sb = new StringBuilder();
				}
			} else {
				if (sb.length() == 0) {
					sb.append(tag);
				}
				sb.append(currString);
				sb.append('\n');
			}
		}
		if (sb.length() != 0) {
			receiver.accept(sb.toString());
		}
	}

	private DiscordMessageDivider() {

	}

}
