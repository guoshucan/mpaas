package ghost.framework.web.mvc.nginx.ui.plugin.ext;


import ghost.framework.web.mvc.nginx.ui.plugin.entity.Location;
import ghost.framework.web.mvc.nginx.ui.plugin.entity.Upstream;

public class LocationExt {
	Location location;
	Upstream upstream;

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public Upstream getUpstream() {
		return upstream;
	}

	public void setUpstream(Upstream upstream) {
		this.upstream = upstream;
	}
}