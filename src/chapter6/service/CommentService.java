package chapter6.service;

import static chapter6.utils.CloseableUtil.*;
import static chapter6.utils.DBUtil.*;

import java.sql.Connection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import chapter6.beans.Comment;
import chapter6.beans.UserComment;
import chapter6.dao.CommentDao;
import chapter6.dao.UserCommentDao;
import chapter6.logging.InitApplication;

public class CommentService {

	/**
	* ロガーインスタンスの生成
	*/
	Logger log = Logger.getLogger("twitter");

	/**
	* デフォルトコンストラクタ
	* アプリケーションの初期化を実施する。
	*/
	public CommentService() {
		InitApplication application = InitApplication.getInstance();
		application.init();
	}


	/*コメントを登録する
	 * 引数：Comment
	 * 戻り値：無し*/
	public void insert(Comment comment) {

		log.info(new Object(){}.getClass().getEnclosingClass().getName() +
				" : " + new Object() {}.getClass().getEnclosingMethod().getName());

		Connection connection = null;
		try {
			connection = getConnection();
			//Daoメソッドを呼び出す
			new CommentDao().insert(connection, comment);
			commit(connection);
		} catch (RuntimeException e) {
			rollback(connection);
			log.log(Level.SEVERE, new Object(){}.getClass().getEnclosingClass().getName() + " : " + e.toString(), e);
			throw e;
		} catch (Error e) {
			rollback(connection);
			log.log(Level.SEVERE, new Object(){}.getClass().getEnclosingClass().getName() + " : " + e.toString(), e);
			throw e;
		} finally {
			close(connection);
		}
	}


/*コメントを取得
 * 引数：メッセージID(String)
 * 戻り値：UserComment(List)*/
	public List<UserComment> select() {

		log.info(new Object(){}.getClass().getEnclosingClass().getName() +
				" : " + new Object(){}.getClass().getEnclosingMethod().getName());

		//1000行の制限を定義
		final int LIMIT_NUM = 1000;

		Connection connection = null;

		try {
			connection = getConnection();

			//Daoメソッドを呼び出す
			List<UserComment> userComment = new UserCommentDao().select(connection, LIMIT_NUM);
			commit(connection);

			return userComment;
		} catch (RuntimeException e) {
			rollback(connection);
			log.log(Level.SEVERE, new Object(){}.getClass().getEnclosingClass().getName() + " : " + e.toString(), e);
			throw e;
		} catch (Error e) {
			rollback(connection);
			log.log(Level.SEVERE, new Object(){}.getClass().getEnclosingClass().getName() + " : " + e.toString(), e);
			throw e;
		} finally {
			close(connection);
		}
	}
}
