package net.sinou.patterns.spring.batch.minimal.configuration;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLSyntaxErrorException;
import java.sql.Statement;
import java.util.Properties;
import java.util.Scanner;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.jdbc.datasource.init.ScriptUtils;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

/**
 * Initialize database persistence, correct driver library must be provided via
 * dependency management
 */
public class SetUpSqlEnv {

	private final static Logger logger = LoggerFactory.getLogger(SetUpSqlEnv.class);

	private static final String KEY_DB_DRIVER_CLASS = "db.driver";
	private static final String KEY_DB_URL = "db.url";
	private static final String KEY_DB_NAME = "db.name";
	private static final String KEY_DB_ADMIN = "db.admin";
	private static final String KEY_DB_ADMIN_PWD = "db.admin.pwd";
	private static final String KEY_DB_USER = "db.username";
	private static final String KEY_DB_PASSWORD = "db.password";
	private static final String KEY_BATCH_CLEAN = "batch.clean.script";
	private static final String KEY_BATCH_SCHEMA = "batch.schema.script";
	private static final String KEY_BUSINESS_SCHEMA = "business.schema.script";

	final private Properties setupProps;
	final private ResourceLoader resourceLoader;

	private DataSource dataSource;

	public SetUpSqlEnv(Properties setupProperties) throws Exception {
		this.setupProps = setupProperties;
		resourceLoader = new DefaultResourceLoader();
		// Creates the table
		cleanDatabase();

		// Creates the datasource
		HikariConfig dataSourceConfig = new HikariConfig();
		dataSourceConfig.setDriverClassName(setupProperties.getProperty(KEY_DB_DRIVER_CLASS));
		String url = setupProperties.getProperty(KEY_DB_URL) + "/" + setupProperties.getProperty(KEY_DB_NAME);
		dataSourceConfig.setJdbcUrl(url);
		dataSourceConfig.setUsername(setupProperties.getProperty(KEY_DB_ADMIN));
		dataSourceConfig.setPassword(setupProperties.getProperty(KEY_DB_ADMIN_PWD));
		dataSource = new HikariDataSource(dataSourceConfig);
	}

	private void cleanDatabase() throws SQLException {
		// Creates a simple connection
		Connection conn = DriverManager.getConnection(setupProps.getProperty(KEY_DB_URL),
				setupProps.getProperty(KEY_DB_ADMIN), setupProps.getProperty(KEY_DB_ADMIN_PWD));
		if (logger.isDebugEnabled())
			logger.debug("Found driver: " + conn.getMetaData().getDriverName() + " - v"
					+ conn.getMetaData().getDriverVersion());
		Statement statement = conn.createStatement();

		// creates the table
		String dbName = setupProps.getProperty(KEY_DB_NAME);
		statement.executeUpdate("DROP DATABASE IF EXISTS " + dbName + ";");
		statement.executeUpdate("CREATE DATABASE " + dbName + ";");

		// Creates a corresponding user for the app if necessary, we should not use an
		// admin user during normal operations
		String userName = setupProps.getProperty(KEY_DB_USER);
		String statementStr = "DROP USER IF EXISTS '" + userName + "';";
		try {
			statement.executeUpdate(statementStr);
		} catch (SQLSyntaxErrorException e) {
			String dbProductName = conn.getMetaData().getDatabaseProductName();
			String dbProductVersion = conn.getMetaData().getDatabaseProductVersion();
			String msg = "Cannot execute statement: '" + statement
					+ "'\nThis usually indicate an older version, current " + dbProductName + " - v" + dbProductVersion
					+ " but we need v10.1.3+";
			logger.error(msg);

			statementStr = "DROP USER '" + userName + "';";
			try {
				statement.executeUpdate(statementStr);
			} catch (SQLException e2) {
				if (e2.getErrorCode() == 1396)
					logger.warn("User " + userName + " does not exists, cannot drop.");
				else
					throw new RuntimeException(e2);
			}
		}
		String pwd = setupProps.getProperty(KEY_DB_PASSWORD);
		statement.executeUpdate("CREATE USER '" + userName + "' IDENTIFIED BY '" + pwd + "';");

		// update corresponding permissions
		statement.executeUpdate("GRANT SELECT, UPDATE, INSERT, DELETE ON " + dbName + ".* TO " + userName + ";");
	}

	// private void loadClass() {
	// try {
	// Class<?> driverClass =
	// this.getClass().getClassLoader().loadClass("org.springframework.batch.core.Entity");
	// driverClass.newInstance();
	//
	// // String prop = setupProps.getProperty(KEY_BATCH_CLEAN);
	// // False : we must remove the classpath: prefix
	// // new ClassPathResource(prop).exists();
	// // true: with the ResourceLoader
	// // resourceLoader.getResource(prop).exists();
	//
	// } catch (Exception e) {
	// throw new RuntimeException("Failed to load class", e);
	// }
	//
	// }

	private void setUpBatchEnv() {
		try (Connection conn = dataSource.getConnection()) {
			String prop = setupProps.getProperty(KEY_BATCH_CLEAN);
			Resource resource = resourceLoader.getResource(prop);
			resource.exists();
			ScriptUtils.executeSqlScript(conn, resource);
			ScriptUtils.executeSqlScript(conn, resourceLoader.getResource(setupProps.getProperty(KEY_BATCH_SCHEMA)));
		} catch (SQLException e) {
			throw new RuntimeException("Cannot create batch database", e);
		}
	}

	private void setUpBusinessEnv() {
		try (Connection conn = dataSource.getConnection()) {
			ScriptUtils.executeSqlScript(conn, resourceLoader.getResource(setupProps.getProperty(KEY_BUSINESS_SCHEMA)));
		} catch (SQLException e) {
			throw new RuntimeException("Cannot create batch database", e);
		}
	}

	public static void main(String[] args) throws Exception {

		System.out.println("Please provide a properties file with DB configuration parameters: \n"
				+ "An example file can be found in the resource base folder:");
		String example = System.getProperty("user.dir") + "/src/main/resources/mysql-setup-example.properties";
		System.out.println(example);

		Properties props = new Properties();
		// try (InputStream input = new FileInputStream(new File(example))) {
		// props.load(input);
		// } catch (IOException ex) {
		// }

		Path path = null;
		try (Scanner in = new Scanner(System.in)) {
			while (path == null) {
				String pathStr = in.nextLine().trim();
				Path currPath = Paths.get(pathStr);
				if (currPath.toFile().exists()) {
					try (InputStream input = new FileInputStream(currPath.toFile())) {
						props.load(input);
					} catch (IOException ex) {
						System.out.println("Cannot load properties from " + pathStr + ".\nCorresponding stack: \n");
						ex.printStackTrace();
						System.out.println("Please try again.\n");
					}
					break;
				} else
					System.out.println("Path at " + pathStr + " cannot be found.\nPlease try again.\n");
			}
		}

		SetUpSqlEnv installer = new SetUpSqlEnv(props);
		installer.setUpBatchEnv();
		installer.setUpBusinessEnv();
		logger.info("System has been setup.");
	}
}
