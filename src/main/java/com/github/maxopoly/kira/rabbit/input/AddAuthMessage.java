package com.github.maxopoly.kira.rabbit.input;

import java.util.UUID;

import org.json.JSONObject;

import net.civmc.kira.Kira;
import com.github.maxopoly.kira.rabbit.RabbitInputSupplier;

public class AddAuthMessage extends RabbitMessage {

	public AddAuthMessage() {
		super("addauth");
	}

	@Override
	public void handle(JSONObject json, RabbitInputSupplier supplier) {
		UUID uuid = UUID.fromString(json.getString("uuid"));
		String code = json.getString("code");
		String name = json.getString("name");
		Kira.Companion.getInstance().getAuthManager().putCode(uuid, name, code);
	}

}
