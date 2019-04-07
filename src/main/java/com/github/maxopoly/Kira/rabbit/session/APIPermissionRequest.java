package com.github.maxopoly.Kira.rabbit.session;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import org.json.JSONObject;

import com.github.maxopoly.Kira.KiraMain;
import com.github.maxopoly.Kira.api.token.APIDataType;
import com.github.maxopoly.Kira.api.token.APIToken;
import com.github.maxopoly.Kira.command.model.top.InputSupplier;

public class APIPermissionRequest extends RequestSession {

	private UUID playerUUID;
	private InputSupplier supplier;
	private Collection<APIDataType> dataRequested;
	private long expirationTime;

	public APIPermissionRequest(UUID player, InputSupplier supplier, Collection<APIDataType> dataRequested,
			long expirationTime) {
		super("apiperms");
		this.playerUUID = player;
		this.supplier = supplier;
		this.dataRequested = dataRequested;
		this.expirationTime = expirationTime;
	}

	@Override
	public JSONObject getRequest() {
		JSONObject json = new JSONObject();
		json.put("uuid", playerUUID.toString());
		return json;
	}

	@Override
	public void handleReply(JSONObject json) {
		List<String> snitchGroups = new LinkedList<>();
		if (dataRequested.contains(APIDataType.SNITCH)) {
			json.getJSONArray("snitches").forEach( s -> snitchGroups.add((String)s));
		}
		List<String> chatGroups = new LinkedList<>();
		if (dataRequested.contains(APIDataType.CHAT)) {
			json.getJSONArray("read_chat").forEach( s -> chatGroups.add((String)s));
		}
		boolean skynet = dataRequested.contains(APIDataType.SKYNET);
		APIToken token = APIToken.generate(supplier.getUser(), snitchGroups, chatGroups, skynet, expirationTime);
		KiraMain.getInstance().getAPISessionManager().getTokenManager().registerToken(token);
		supplier.reportBack("Your token is: " + token.getToken());
	}

}
