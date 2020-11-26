package ghost.framework.web.mvc.nginx.ui.plugin.controller;

import cn.hutool.core.util.StrUtil;
import ghost.framework.beans.annotation.injection.Autowired;
import ghost.framework.web.angular1x.context.controller.ControllerBase;
import ghost.framework.web.context.bind.annotation.RequestMapping;
import ghost.framework.web.context.http.responseContent.DataResponse;
import ghost.framework.web.mvc.context.ModelAndView;
import ghost.framework.web.mvc.context.bind.annotation.Controller;
import ghost.framework.web.mvc.context.bind.annotation.ResponseBody;
import ghost.framework.web.mvc.nginx.ui.plugin.entity.Stream;
import ghost.framework.web.mvc.nginx.ui.plugin.service.StreamService;

import javax.servlet.http.HttpSession;
import java.sql.SQLException;
import java.util.List;

@Controller("/api")
//@RequestMapping("/adminPage/stream")
public class StreamController extends ControllerBase {
	@Autowired
	StreamService streamService;

	@RequestMapping("")
	public ModelAndView index(HttpSession httpSession, ModelAndView modelAndView) {
//		List<Stream> streamList = sqlHelper.findAll(new Sort("seq", Direction.ASC), Stream.class);
		List<Stream> streamList = sessionFactory.findAll(Stream.class);
		modelAndView.addObject("streamList", streamList);
		modelAndView.setViewName("/stream/index");
		return modelAndView;
	}

	@RequestMapping("addOver")
	@ResponseBody
	public DataResponse addOver(Stream stream) throws SQLException {
		if (StrUtil.isEmpty(stream.getId())) {
			stream.setSeq(streamService.buildOrder());
		}
//		sqlHelper.insertOrUpdate(stream);
		sessionFactory.insert(stream);
		return DataResponse.success();
	}

	@RequestMapping("detail")
	@ResponseBody
	public DataResponse detail(String id) throws SQLException {
		return DataResponse.success(sessionFactory.findById(Stream.class, id));
	}

	@RequestMapping("del")
	@ResponseBody
	public DataResponse del(String id) throws SQLException {
//		sqlHelper.deleteById(id, Stream.class);
		sessionFactory.deleteById(Stream.class, id);
		return DataResponse.success();
	}

	@RequestMapping("setOrder")
	@ResponseBody
	public DataResponse setOrder(String id, Integer count) {
		streamService.setSeq(id, count);
		return DataResponse.success();
	}
}