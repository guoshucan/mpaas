package ghost.framework.web.mvc.nginx.ui.plugin.controller;

import cn.hutool.core.util.StrUtil;
import ghost.framework.web.angular1x.context.controller.ControllerBase;
import ghost.framework.web.context.bind.annotation.RequestMapping;
import ghost.framework.web.mvc.context.bind.annotation.Controller;
import ghost.framework.web.mvc.context.bind.annotation.ResponseBody;
import ghost.framework.web.mvc.nginx.ui.plugin.ext.TreeNode;
import ghost.framework.web.mvc.nginx.ui.plugin.utils.SystemTool;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Controller("/api")
//@RequestMapping("/adminPage/root")
public class RootController extends ControllerBase {

	@ResponseBody
	@RequestMapping("getList")
	public List<TreeNode> getList(String id) {
		if (StrUtil.isEmpty(id)) {
			id = "/";
		}

		List<TreeNode> list = new ArrayList<TreeNode>();

		File[] fileList = null;
		if (SystemTool.isWindows() && id.equals("/")) {
			fileList = File.listRoots();
		} else {
			fileList = new File(id).listFiles();
		}

		for (File temp : fileList) {

			TreeNode treeNode = new TreeNode();
			treeNode.setId(temp.getPath());
			if (StrUtil.isNotEmpty(temp.getName())) {
				treeNode.setName(temp.getName());
			} else {
				treeNode.setName(temp.getPath());
			}

			if (temp.isDirectory()) {
				treeNode.setIsParent("true");
			} else {
				treeNode.setIsParent("false");
			}

			list.add(treeNode);

		}

		// 按文件夹进行排序
		list.sort(new Comparator<TreeNode>() {

			@Override
			public int compare(TreeNode o1, TreeNode o2) {

				if (o1.getIsParent().equals("true") && o2.getIsParent().equals("false")) {
					return -1;
				}
				if (o1.getIsParent().equals("false") && o2.getIsParent().equals("true")) {
					return 1;
				}


				return o1.getName().compareToIgnoreCase(o2.getName());
			}
		});
		return list;
	}
}
