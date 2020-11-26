package ghost.framework.web.mvc.nginx.ui.plugin.controller;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.ZipUtil;
import ghost.framework.beans.annotation.injection.Autowired;
import ghost.framework.web.angular1x.context.controller.ControllerBase;
import ghost.framework.web.context.bind.annotation.RequestMapping;
import ghost.framework.web.context.http.responseContent.DataResponse;
import ghost.framework.web.mvc.context.ModelAndView;
import ghost.framework.web.mvc.context.bind.annotation.Controller;
import ghost.framework.web.mvc.context.bind.annotation.ResponseBody;
import ghost.framework.web.mvc.nginx.ui.plugin.config.InitConfig;
import ghost.framework.web.mvc.nginx.ui.plugin.model.Bak;
import ghost.framework.web.mvc.nginx.ui.plugin.service.SettingService;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
//@Controller("/adminPage/bak")
@Controller("/api")
public class BakController extends ControllerBase {
	@Autowired
	SettingService settingService;

	@RequestMapping("")
	public ModelAndView index(HttpSession httpSession, ModelAndView modelAndView) {
		List<Bak> bakList = getBakList();

		CollectionUtil.sort(bakList, new Comparator<Bak>() {

			@Override
			public int compare(Bak o1, Bak o2) {
				return StrUtil.compare(o2.getTime(), o1.getTime(), true);
			}
		});

		modelAndView.addObject("bakList", bakList);
		modelAndView.setViewName("/bak/index");
		return modelAndView;
	}

	private List<Bak> getBakList() {
		List<Bak> list = new ArrayList<Bak>();

		String bakPath = InitConfig.home + "/bak";
		if (StrUtil.isNotEmpty(bakPath) && FileUtil.exist(bakPath)) {
			File dir = new File(bakPath);

			File[] fileList = dir.listFiles();
			if (fileList != null) {
				for (File file : fileList) {
					if (file.getName().contains("nginx.conf") && file.getName().endsWith(".bak")) {
						Bak bak = new Bak();
						bak.setPath(file.getPath().replace("\\", "/"));
						DateTime date = DateUtil.parse(file.getName().replace("nginx.conf.", "").replace(".bak", ""), "yyyy-MM-dd_HH-mm-ss");
						bak.setTime(DateUtil.format(date, "yyyy-MM-dd HH:mm:ss"));

						list.add(bak);
					}
				}
			}
		}

		return list;
	}

	@RequestMapping("content")
	@ResponseBody
	public DataResponse content(String path) {
		String str = FileUtil.readString(path, Charset.forName("UTF-8"));
		return DataResponse.success(str);
	}

	@RequestMapping("replace")
	@ResponseBody
	public DataResponse replace(String path) {
		String nginxPath = settingService.get("nginxPath");

		if (StrUtil.isNotEmpty(nginxPath)) {
			File pathFile = new File(nginxPath);

			FileUtil.copy(path, nginxPath, true);
			FileUtil.del(pathFile.getParent() + "/conf.d");
			ZipUtil.unzip(path.replace(".bak", ".zip"), pathFile.getParent() + "/conf.d");
			return DataResponse.success();
		} else {
//			return renderError(m.get("bakStr.pathNotice"));
			return DataResponse.success();
		}

	}

	@RequestMapping("del")
	@ResponseBody
	public DataResponse del(String path) {
		FileUtil.del(path);
		FileUtil.del(path.replace(".bak", ".zip"));
		return DataResponse.success();
	}

	@RequestMapping("delAll")
	@ResponseBody
	public DataResponse delAll() {
		List<Bak> list = getBakList();
		for (Bak bak : list) {
			del(bak.getPath());
		}
		return DataResponse.success();
	}
}
