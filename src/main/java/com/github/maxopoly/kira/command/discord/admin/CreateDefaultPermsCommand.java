package com.github.maxopoly.kira.command.discord.admin;

import net.civmc.kira.Kira;
import com.github.maxopoly.kira.command.model.discord.Command;
import com.github.maxopoly.kira.command.model.top.InputSupplier;

public class CreateDefaultPermsCommand extends Command {

	public CreateDefaultPermsCommand() {
		super("createdefaultperms");
	}

	@Override
	public String getFunctionality() {
		return "Initializes basic permissions";
	}

	@Override
	public String getRequiredPermission() {
		return "admin";
	}

	@Override
	public String getUsage() {
		return "createdefaultperms";
	}

	@Override
	public String handleInternal(String argument, InputSupplier sender) {
		Kira.Companion.getInstance().getKiraRoleManager().setupDefaultPermissions();
		return "Setup basic permissions";
	}
}