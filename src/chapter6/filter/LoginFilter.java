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
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import chapter6.beans.User;

@WebFilter(urlPatterns = {"/setting", "/edit" })
public class LoginFilter implements Filter {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {

		/*型変換
		 *ServletRequest→HttpRequest */
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse= (HttpServletResponse) response;

		//セッション領域のログイン情報を取得
		HttpSession session = httpRequest.getSession();
		User user = (User) session.getAttribute("loginUser");

		/*①セッション領域にログイン情報が存在する(ログインしている)
			→サーブレットを実行
			②ログインしていない
			→ログインページに遷移、エラーメッセージを出力*/
		if (user != null) {
			chain.doFilter(request, response); // サーブレットを実行
		} else {
			session.setAttribute("errorMessages", "ログインしてください");
			httpResponse.sendRedirect("login.jsp");
		}
	}

	@Override
	public void init(FilterConfig config) {
	}

	@Override
	public void destroy() {
	}

}
