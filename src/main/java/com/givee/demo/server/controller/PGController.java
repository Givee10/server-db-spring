package com.givee.demo.server.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.givee.demo.server.HasLogger;
import com.givee.demo.server.domain.ColumnDto;
import com.givee.demo.server.domain.TableDto;
import com.givee.demo.server.utils.BeanUtil;
import com.givee.demo.server.utils.StringUtil;
import org.postgresql.ds.PGSimpleDataSource;
import org.springframework.web.bind.annotation.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Base64;
import java.util.List;

@RestController
@RequestMapping({"api/v1/postgres/"})
public class PGController implements HasLogger {
	public static final String APPLICATION_JSON = "application/json;charset=UTF-8";

	private Connection initDatabase(String auth, String database) {
		String[] splitHeader = auth.split(" ");
		byte[] bytes = Base64.getDecoder().decode(splitHeader[1]);
		String decoded = new String(bytes);
		String[] splitDecoded = decoded.split(":");
		String username = splitDecoded[0];
		StringBuilder builder = new StringBuilder();
		// This is for case of symbol ":" in password
		for (int i = 1; i < splitDecoded.length; i++) {
			builder.append(splitDecoded[i]).append(":");
		}
		builder.deleteCharAt(builder.length()-1);
		String password = builder.toString();

		PGSimpleDataSource source = new PGSimpleDataSource();
		source.setApplicationName("Demo App");
		source.setServerNames(new String[]{"localhost"});
		source.setPortNumbers(new int[]{5432});
		source.setDatabaseName(database);
		source.setUser(username);
		source.setPassword(password);
		try {
			return source.getConnection();
		} catch (SQLException e) {
			getLogger().error(e.getLocalizedMessage());
			throw new UnauthorizedException(e.getLocalizedMessage());
		}
	}

	private String postSQLRequest(Connection connection, String query) {
		ObjectMapper objectMapper = BeanUtil.getBean(ObjectMapper.class);
		ArrayNode arrayNode = objectMapper.createArrayNode();
		try {
			Statement statement = connection.createStatement();
			ResultSet rs = statement.executeQuery(query);
			while (rs.next()) {
				ObjectNode objectNode = objectMapper.createObjectNode();
				int total_rows = rs.getMetaData().getColumnCount();
				for (int i = 0; i < total_rows; i++) {
					objectNode.put(rs.getMetaData().getColumnLabel(i + 1).toLowerCase(), rs.getString(i + 1));
				}
				arrayNode.add(objectNode);
			}
			rs.close();
			statement.close();
			connection.close();
			return arrayNode.toString();
		} catch (SQLException e) {
			getLogger().error(e.getMessage());
			return arrayNode.toString();
		}
	}

	@PostMapping(value = "login", produces = APPLICATION_JSON)
	@ResponseBody
	private String doLogin(
			@RequestHeader("Authorization") String auth,
			@RequestParam("database") String database) {
		Connection connection = initDatabase(auth, database);
		try {
			String schema = connection.getSchema();
			connection.close();
			return schema;
		} catch (SQLException e) {
			getLogger().error(e.getLocalizedMessage());
			return e.getLocalizedMessage();
		}
	}

	@GetMapping(value = "tables", produces = APPLICATION_JSON)
	@ResponseBody
	private List<TableDto> getTables(
			@RequestHeader("Authorization") String auth,
			@RequestParam("database") String database) {
		try {
			Connection connection = initDatabase(auth, database);
			String query = "SELECT * FROM information_schema.tables " +
					"WHERE table_schema = '" + connection.getSchema() + "';";
			return StringUtil.writeStringAsArray(postSQLRequest(connection, query), TableDto[].class);
		} catch (SQLException e) {
			getLogger().error(e.getMessage());
			return null;
		}
	}

	@GetMapping(value = "tables/{tableName}/columns", produces = APPLICATION_JSON)
	@ResponseBody
	private List<ColumnDto> getTableColumns(
			@RequestHeader("Authorization") String auth,
			@RequestParam("database") String database,
			@PathVariable String tableName) {
		try {
			Connection connection = initDatabase(auth, database);
			String query = "SELECT * FROM information_schema.columns " +
					"WHERE table_schema = '" + connection.getSchema() + "' " +
					"AND table_name = '" + tableName + "';";
			return StringUtil.writeStringAsArray(postSQLRequest(connection, query), ColumnDto[].class);
		} catch (SQLException e) {
			getLogger().error(e.getMessage());
			return null;
		}
	}

	@GetMapping(value = "tables/{tableName}/values", produces = APPLICATION_JSON)
	@ResponseBody
	private List<ObjectNode> getTableValues(
			@RequestHeader("Authorization") String auth,
			@RequestParam("database") String database,
			@PathVariable String tableName) {
		try {
			Connection connection = initDatabase(auth, database);
			String query = "SELECT * FROM " + connection.getSchema() + ".\"" + tableName + "\";";
			return StringUtil.writeStringAsArray(postSQLRequest(connection, query), ObjectNode[].class);
		} catch (SQLException e) {
			getLogger().error(e.getMessage());
			return null;
		}
	}
}
