package net.sinou.patterns.spring.batch.minimal.util;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Some basic methods to ease development and debugging without adding
 * additional dependencies
 */
public class JdbcUtils {
	private final static Logger logger = LoggerFactory.getLogger(JdbcUtils.class);

	private final static int FIRST_LINE_NB = 10;

	/**
	 * Writes info and first 10 lines (if any) for this table using a logger with
	 * INFO level
	 */
	public static void listDbTableFirstLines(DataSource dataSource, String tableName) {
		try {
			Connection con = dataSource.getConnection();
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM " + tableName);
			StringBuilder builder = new StringBuilder();
			builder.append("Displaying ").append(FIRST_LINE_NB).append(" (at most) first lines of table: ").append(tableName)
					.append("\n");

			builder.append("Column names:\n");
			ResultSetMetaData metadata = rs.getMetaData();
			for (int i = 1; i <= metadata.getColumnCount(); i++)
				builder.append(metadata.getColumnLabel(i)).append("; ");

			builder.delete(builder.length() - 2, builder.length()).append("\nContent: \n");
			int i = 0;
			while (rs.next() && i++ < FIRST_LINE_NB) {
				for (int j = 1; j <= metadata.getColumnCount(); j++)
					builder.append(rs.getString(j)).append("; ");
				builder.delete(builder.length() - 2, builder.length()).append("\n");
			}
			logger.info(builder.toString());

		} catch (SQLException e) {
			logger.error("Cannot display first lines of " + tableName, e);
		}
	}

	/**
	 * Easily lists all tables of accessible with this datasource using a
	 * {@code Logger} with INFO level
	 */
	public static void listAllDbTables(DataSource dataSource) {
		try {
			Connection con = dataSource.getConnection();
			DatabaseMetaData md = con.getMetaData();
			ResultSet rs = md.getTables(null, null, "%", null);
			StringBuilder builder = new StringBuilder();
			builder.append("Listing all tables: \n");
			while (rs.next()) {
				// hard coded table name is third column
				builder.append(rs.getString(3)).append("\n");
			}
			logger.info(builder.toString());
		} catch (SQLException e) {
			logger.error("Cannot display tables of " + dataSource.toString(), e);
		}
	}

	private JdbcUtils() {
	}
}
