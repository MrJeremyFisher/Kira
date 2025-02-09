package com.github.maxopoly.kira.command.discord.admin;

import net.civmc.kira.Kira;
import com.github.maxopoly.kira.command.model.discord.ArgumentBasedCommand;
import com.github.maxopoly.kira.command.model.top.InputSupplier;
import com.github.maxopoly.kira.user.DiscordRoleManager;
import com.github.maxopoly.kira.user.KiraUser;
import com.github.maxopoly.kira.user.UserManager;

public class DeauthDiscordCommand extends ArgumentBasedCommand {

	public DeauthDiscordCommand() {
		super("deauth", 1, "unrole");
	}

	@Override
	public String getFunctionality() {
		return "Removes a users auth connection";
	}

	@Override
	public String getRequiredPermission() {
		return "admin";
	}

	@Override
	public String getUsage() {
		return "deauth [user]";
	}

	@Override
	public String handle(InputSupplier sender, String[] args) {
		StringBuilder reply = new StringBuilder();
		UserManager userManager = Kira.Companion.getInstance().getUserManager();
		DiscordRoleManager authManager = Kira.Companion.getInstance().getDiscordRoleManager();
		KiraUser user = userManager.parseUser(args[0], reply);
		if (user == null) {
			reply.append("User not found, no action was taken\n");
		} else {
			authManager.takeDiscordRole(Kira.Companion.getInstance().getGuild(), user)
					.whenComplete((worked, error) -> {
						reply.append("Unregistered user with given id found in discord, role removal "
								+ (worked ? "successfull" : "unsuccessfull") + "\n");
						if (worked) {
							user.updateIngame(null, null);
							Kira.Companion.getInstance().getDao().updateUser(user);
						}
					});
		}
		return reply.toString();
	}
}
