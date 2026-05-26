<%@page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page isELIgnored="false"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<link href="css/style.css" rel="stylesheet" type="text/css">
	<title>簡易Twitter</title>
</head>
<body>
	<!-- 未：ログイン -->
	<div class="header">
		<c:if test="${ empty loginUser }">
			<a href="login">ログイン</a>
			<a href="signup">登録する</a>
		</c:if>
		<!-- 済：ログイン -->
		<c:if test="${ not empty loginUser }">
			<a href="./">ホーム</a>
			<a href="setting">設定</a>
			<a href="logout">ログアウト</a>
		</c:if>
	</div>
	<!-- ログイン情報がある場合、画面に表示する -->
	<c:if test="${ not empty loginUser }">
		<div class="profile">
			<div class="name">
				<h2>
					<c:out value="${loginUser.name}" />
				</h2>
			</div>
			<div class="account">
				@
				<c:out value="${loginUser.account}" />
			</div>
			<div class="description">
				<c:out value="${loginUser.description}" />
			</div>
		</div>
	</c:if>
	<c:if test="${ not empty errorMessages }">
		<div class="errorMessages">
			<ul>
				<c:forEach items="${errorMessages}" var="errorMessage">
					<li><c:out value="${errorMessage}" />
				</c:forEach>
			</ul>
		</div>
		<c:remove var="errorMessages" scope="session" />
	</c:if>
	<!-- つぶやき機能 ログイン時のみ表示-->
	<div class="form-area">
		<c:if test="${ isShowMessageForm }">
			<form action="message" method="post">
				いま、どうしてる？<br />
				<textarea name="text" cols="100" rows="5" class="tweet-box"></textarea>
				<br /> <input type="submit" value="つぶやく">（140文字まで）
			</form>
		</c:if>
	</div>
	<!-- つぶやきを表示-->
	<div class="messages">
		<c:forEach items="${messages}" var="message">
			<div class="message">
				<div class="account-name">
					<!-- アカウント名をリンクにする -->
					<a href="./?user_id=<c:out value="${message.userId}"/> ">
					<span class="account"><c:out value="${message.account}" /></span>
					</a> <span class="name"><c:out value="${message.name}" /></span>
				</div>
				<div class="text">
					<pre><c:out value="${message.text}" /></pre>
				</div>
				<div>
					<div class="date">
						<fmt:formatDate value="${message.createdDate}" pattern="yyyy/MM/dd HH:mm:ss" />
					</div>
					<div class="buttons">
						<c:if test="${ loginUser.id == message.userId}">
							<form class="button" action="deleteMessage" method="post">
								<input type="hidden" name="messageId" value="${message.id}">
								<input type="submit" value="削除" class="btn">
							</form>
							<form action="edit" method="get">
								<input type="hidden" name="messageId" value="${message.id}">
								<input type="submit" value="編集">
							</form>
						</c:if>
					</div>
					<!-- 返信 -->
					<div class=comments>
						<!-- 返信コメントを表示 -->
						<!-- コメントのリストを表示する -->
						<c:forEach items="${comments}" var="comment">
							<c:if test="${comment.messageId == message.id }">
								<!-- コメントしたユーザー情報 -->
								<div class="account-name">
									<a href="./?user_id=<c:out value="${comment.userId}"/> ">
										<span class="account"><c:out value="${comment.account}" /></span>
									</a>
									<span class="name"><c:out value="${comment.name}" /></span>
								</div>
								<span class="comment"><c:out value="${comment.text}" /></span>
								<div class="messageDate">
									<fmt:formatDate value="${comment.createdDate}" pattern="yyyy/MM/dd HH:mm:ss" />
								</div>
							</c:if>
						</c:forEach>
						<c:if test="${ isShowMessageForm }">
							<form action="comment" method="post">
								<input type="hidden" name="messageId" value="${message.id}">
								<input type="hidden" name="userId" value="${loginUser.id }">
								<textarea name="text" cols="100" rows="5" class="tweet-box"></textarea>
								<div>
									<input class="button" type="submit" value="返信">
								</div>
							</form>
						</c:if>
					</div>
				</div>
			</div>
		</c:forEach>
	</div>
	<div class="copyright">Copyright(c)GotoRiko</div>
</body>
</html>