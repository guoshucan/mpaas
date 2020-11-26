package ghost.framework.web.mvc.nginx.ui.plugin.ext;


import ghost.framework.web.mvc.nginx.ui.plugin.entity.Location;
import ghost.framework.web.mvc.nginx.ui.plugin.entity.Server;

import java.util.List;

public class ServerExt {
	Server server;
	List<Location> locationList;
	String locationStr;
	String paramJson;
	

	public String getParamJson() {
		return paramJson;
	}

	public void setParamJson(String paramJson) {
		this.paramJson = paramJson;
	}

	public String getLocationStr() {
		return locationStr;
	}

	public void setLocationStr(String locationStr) {
		this.locationStr = locationStr;
	}

	public Server getServer() {
		return server;
	}

	public void setServer(Server server) {
		this.server = server;
	}

	public List<Location> getLocationList() {
		return locationList;
	}

	public void setLocationList(List<Location> locationList) {
		this.locationList = locationList;
	}



}
