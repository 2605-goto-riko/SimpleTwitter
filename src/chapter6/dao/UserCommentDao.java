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

	public List<UserComment> select(Connection connection, Integer messageId, int num) {

		log.info(new Object() {}.getClass().getEnclosingClass().getName() +
				" : " + new Object() {}.getClass().getEnclosingMethod().getName());

		PreparedStatement ps = null;
		try {

			StringBuilder sql = new StringBuilder();
			sql.append("SELECT m.id AS message_id, u.id AS user_id, u.name, u.account, ");
			sql.append("c.id AS comment_id, c.text,c.created_date ");
			sql.append("FROM comments c ");
			sql.append("INNER JOIN messages m ");
			sql.append("ON c.message_id = m.id ");
			sql.append("INNER JOIN users u ");
			sql.append("ON c.user_id = u.id ");
			sql.append("ORDER BY created_date ASC limit " + num);

			ps = connection.prepareStatement(sql.toString());

			ResultSet rs = ps.executeQuery();

			List<UserComment> userComment = toUserMessageComment(rs);

			return userComment;

		} catch (SQLException e) {
			log.log(Level.SEVERE, new Object(){}.getClass().getEnclosingClass().getName() + " : " + e.toString(), e);
			throw new SQLRuntimeException(e);
		} finally {
			close(ps);
		}
	}


	private List<UserComment> toUserMessageComment(ResultSet rs) throws SQLException {

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
