package chapter6.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;

import chapter6.beans.Message;
import chapter6.logging.InitApplication;
import chapter6.service.MessageService;

@WebServlet(urlPatterns = { "/edit" })
public class EditServlet extends HttpServlet{


	/**
	* ロガーインスタンスの生成
	*/
	Logger log = Logger.getLogger("twitter");

	/**
	* デフォルトコンストラクタ
	* アプリケーションの初期化を実施する。
	*/
	public EditServlet() {
		InitApplication application = InitApplication.getInstance();
		application.init();
	}

	/*更新画面遷移時*/
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		log.info(new Object(){}.getClass().getEnclosingClass().getName() +
				" : " + new Object(){}.getClass().getEnclosingMethod().getName());

		HttpSession session = request.getSession();
		String Id = request.getParameter("messageId");

		if (StringUtils.isBlank(Id) || !Id.matches("^[0-9]+$")) {
			session.setAttribute("errorMessages", "不正なパラメータが入力されました");
			response.sendRedirect("./");
			return;
		}

		Integer messageId =Integer.parseInt(Id);

		Message editMessage = new MessageService().select(messageId);

		if(editMessage == null) {
			session.setAttribute("errorMessages", "不正なパラメータが入力されました");
			response.sendRedirect("./");
			return;
		}

		request.setAttribute("editMessage", editMessage);
		request.getRequestDispatcher("/edit.jsp").forward(request, response);
	}

/*更新ボタン押下後の処理*/
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		log.info(new Object(){}.getClass().getEnclosingClass().getName() +
				" : " + new Object(){}.getClass().getEnclosingMethod().getName());

		List<String> errorMessages = new ArrayList<String>();

		int messageId = Integer.parseInt(request.getParameter("messageId"));
		String text = request.getParameter("text");

		/*画面保持用に値をセットする*/
		Message editMessage = new Message();
		editMessage.setText(text);
		editMessage.setId(messageId);

		if (!isValid(text, errorMessages)) {
			request.setAttribute("errorMessages", errorMessages);
			request.setAttribute("editMessage", editMessage);
			request.getRequestDispatcher("/edit.jsp").forward(request, response);
			return;
		}
		new MessageService().update(editMessage);
		response.sendRedirect("./");
	}


	private boolean isValid(String text, List<String> errorMessages) {

		log.info(new Object(){}.getClass().getEnclosingClass().getName() +
				" : " + new Object(){}.getClass().getEnclosingMethod().getName());

		if (StringUtils.isBlank(text)) {
			errorMessages.add("メッセージを入力してください");
		} else if (140 < text.length()) {
			errorMessages.add("140文字以下で入力してください");
		}
		if (errorMessages.size() != 0) {
			return false;
		}
		return true;
	}
}