package chapter6.dao;

import static chapter6.utils.CloseableUtil.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import chapter6.beans.UserComment;
import chapter6.exception.SQLRuntimeException;
import chapter6.logging.InitApplication;

public class UserCommentDao {

	/**
	* ロガーインスタンスの生成
	*/
	Logger log = Logger.getLogger("twitter");

	/**
	* デフォルトコンストラクタ
	* アプリケーションの初期化を実施する。
	*/
	public UserCommentDao() {
		InitApplication application = InitApplication.getInstance();
		application.init();
	}


	/*
	 * コメントを取得する
	 * 引数：Connection(Connection),メッセージid(Integer),最大行(int)
	 * 戻り値：UserComment(List)
	 */
	public List<UserComment> select(Connection connection, int num) {

		log.info(new Object() {}.getClass().getEnclosingClass().getName() +
				" : " + new Object() {}.getClass().getEnclosingMethod().getName());

		PreparedStatement ps = null;
		try {

			StringBuilder sql = new StringBuilder();
			sql.append("SELECT ");
			sql.append("    users.id AS user_id, ");
			sql.append("    users.name, users.account, ");
			sql.append("    comments.id AS comment_id, ");
			sql.append("    comments.message_id AS message_id, ");
			sql.append("    comments.text, ");
			sql.append("    comments.created_date ");
			sql.append("FROM comments ");
			sql.append("INNER JOIN users ");
			sql.append("ON comments.user_id = users.id ");
			sql.append("ORDER BY comments.created_date ASC limit " + num);

			ps = connection.prepareStatement(sql.toString());

			ResultSet rs = ps.executeQuery();

			//取得した値をUserCommentBeanに詰めてリストにする
			List<UserComment> userComment = toUserComment(rs);

			return userComment;

		} catch (SQLException e) {
			log.log(Level.SEVERE, new Object(){}.getClass().getEnclosingClass().getName() + " : " + e.toString(), e);
			throw new SQLRuntimeException(e);
		} finally {
			close(ps);
		}
	}


	private List<UserComment> toUserComment(ResultSet rs) throws SQLException {

		log.info(new Object(){}.getClass().getEnclosingClass().getName() +
				" : " + new Object(){}.getClass().getEnclosingMethod().getName());

		List<UserComment> userComments = new ArrayList<UserComment>();
		try {
			while (rs.next()) {
				UserComment userComment = new UserComment();
				userComment.setCommentId(rs.getInt("comment_id"));
				userComment.setMessageId(rs.getInt("message_id"));
				userComment.setUserId(rs.getInt("user_id"));
				userComment.setName(rs.getString("name"));
				userComment.setAccount(rs.getString("account"));
				userComment.setText(rs.getString("text"));
				userComment.setCreatedDate(rs.getTimestamp("created_date"));

				userComments.add(userComment);
			}
			return userComments;
		} finally {
			close(rs);
		}
	}
}
