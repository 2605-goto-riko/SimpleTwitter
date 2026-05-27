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

import chapter6.beans.Comment;
import chapter6.logging.InitApplication;
import chapter6.service.CommentService;

@WebServlet(urlPatterns = { "/comment" })
public class CommentServlet extends HttpServlet {

	/**
	* ロガーインスタンスの生成
	*/
	Logger log = Logger.getLogger("twitter");

	/**
	* デフォルトコンストラクタ
	* アプリケーションの初期化を実施する。
	*/
	public CommentServlet() {
		InitApplication application = InitApplication.getInstance();
		application.init();
	}


	/*返信ボタン押下後の登録処理*/
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		log.info(new Object(){}.getClass().getEnclosingClass().getName() +
				" : " + new Object(){}.getClass().getEnclosingMethod().getName());

		//エラーメッセージリストを作成
		List<String> errorMessages = new ArrayList<String>();

		//セッションを取得
		HttpSession session = request.getSession();

		//画面から情報を取得
		String text = request.getParameter("text");
		Integer messageId = Integer.parseInt(request.getParameter("messageId"));
		Integer userId = Integer.parseInt(request.getParameter("userId"));

		//引数用にcommentBeanに画面から取得した情報を詰める
		Comment comment = new Comment();
		comment.setText(text);
		comment.setUserId(userId);
		comment.setMessageId(messageId);

		//バリデーションチェック
		if (!isValid(text, errorMessages)) {
			//ホーム画面にRedirectするのでsessionにエラーメッセージを設定
			session.setAttribute("errorMessages", errorMessages);
			response.sendRedirect("./");
			return;
		}
		//Serviceメソッドを呼び出す
		new CommentService().insert(comment);
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
