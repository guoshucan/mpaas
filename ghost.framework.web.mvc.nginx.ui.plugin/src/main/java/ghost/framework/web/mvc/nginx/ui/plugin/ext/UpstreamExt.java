package ghost.framework.web.mvc.nginx.ui.plugin.ext;


import ghost.framework.web.mvc.nginx.ui.plugin.entity.Upstream;
import ghost.framework.web.mvc.nginx.ui.plugin.entity.UpstreamServer;

import java.util.List;

public class UpstreamExt {
	Upstream upstream;
	List<UpstreamServer> upstreamServerList;
	String serverStr;
	String paramJson;
	
	
	public String getParamJson() {
		return paramJson;
	}

	public void setParamJson(String paramJson) {
		this.paramJson = paramJson;
	}

	public String getServerStr() {
		return serverStr;
	}

	public void setServerStr(String serverStr) {
		this.serverStr = serverStr;
	}

	public Upstream getUpstream() {
		return upstream;
	}

	public void setUpstream(Upstream upstream) {
		this.upstream = upstream;
	}

	public List<UpstreamServer> getUpstreamServerList() {
		return upstreamServerList;
	}

	public void setUpstreamServerList(List<UpstreamServer> upstreamServerList) {
		this.upstreamServerList = upstreamServerList;
	}

}
