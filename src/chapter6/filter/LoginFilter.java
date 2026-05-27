package chapter6.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;

import chapter6.beans.User;

@WebFilter(urlPatterns = {"/setting", "/edit" })
public class LoginFilter implements Filter {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {

		/*型変換
		 *ServletRequest→HttpRequest */
		HttpServletRequest httpRequest = (HttpServletRequest) request;

		//セッション領域のログイン情報を取得
		User user = (User) httpRequest.getSession().getAttribute("loginUser");

		/*①セッション領域にログイン情報が存在する(ログインしている)
			→サーブレットを実行
			②ログインしていない
			→ログインページに遷移、エラーメッセージを出力*/
		if (user != null) {
			chain.doFilter(request, response); // サーブレットを実行
		} else {
			httpRequest.setAttribute("errorMessages", "ログインしてください");
			httpRequest.getRequestDispatcher("login.jsp").forward(request, response);
		}
	}

	@Override
	public void init(FilterConfig config) {
	}

	@Override
	public void destroy() {
	}

}
