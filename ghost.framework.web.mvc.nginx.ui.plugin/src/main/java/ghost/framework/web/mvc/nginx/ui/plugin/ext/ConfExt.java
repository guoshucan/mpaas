package ghost.framework.web.mvc.nginx.ui.plugin.ext;

import java.util.List;

public class ConfExt {
	String conf;

	List<ConfFile> fileList;

	public String getConf() {
		return conf;
	}

	public void setConf(String conf) {
		this.conf = conf;
	}

	public List<ConfFile> getFileList() {
		return fileList;
	}

	public void setFileList(List<ConfFile> fileList) {
		this.fileList = fileList;
	}


}
