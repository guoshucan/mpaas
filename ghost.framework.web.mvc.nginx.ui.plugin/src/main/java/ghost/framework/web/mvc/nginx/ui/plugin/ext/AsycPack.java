package ghost.framework.web.mvc.nginx.ui.plugin.ext;

import ghost.framework.web.mvc.nginx.ui.plugin.entity.*;

import java.util.List;

public class AsycPack {
//	List<Cert> certList;
	List<Http> httpList;
	List<Server> serverList;
	List<Location> locationList;
	List<Upstream> upstreamList;
	List<UpstreamServer> upstreamServerList;
	List<Stream> streamList;
	List<Param> paramList;

	ConfExt confExt;

	String decompose;

	public List<Param> getParamList() {
		return paramList;
	}

	public void setParamList(List<Param> paramList) {
		this.paramList = paramList;
	}

	public String getDecompose() {
		return decompose;
	}

	public void setDecompose(String decompose) {
		this.decompose = decompose;
	}

	public ConfExt getConfExt() {
		return confExt;
	}

	public void setConfExt(ConfExt confExt) {
		this.confExt = confExt;
	}

	public List<Stream> getStreamList() {
		return streamList;
	}

	public void setStreamList(List<Stream> streamList) {
		this.streamList = streamList;
	}

//	public List<Cert> getCertList() {
//		return certList;
//	}

	public List<Location> getLocationList() {
		return locationList;
	}

	public void setLocationList(List<Location> locationList) {
		this.locationList = locationList;
	}

//	public void setCertList(List<Cert> certList) {
//		this.certList = certList;
//	}

	public List<Http> getHttpList() {
		return httpList;
	}

	public void setHttpList(List<Http> httpList) {
		this.httpList = httpList;
	}

	public List<Server> getServerList() {
		return serverList;
	}

	public void setServerList(List<Server> serverList) {
		this.serverList = serverList;
	}

	public List<Upstream> getUpstreamList() {
		return upstreamList;
	}

	public void setUpstreamList(List<Upstream> upstreamList) {
		this.upstreamList = upstreamList;
	}

	public List<UpstreamServer> getUpstreamServerList() {
		return upstreamServerList;
	}

	public void setUpstreamServerList(List<UpstreamServer> upstreamServerList) {
		this.upstreamServerList = upstreamServerList;
	}

}
