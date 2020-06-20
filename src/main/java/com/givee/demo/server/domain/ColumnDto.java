package com.givee.demo.server.domain;

public class ColumnDto {
	private String table_catalog;
	private String table_schema;
	private String table_name;
	private String column_name;
	private String is_nullable;
	private String data_type;
	private String character_maximum_length;
	private String character_octet_length;

	public String getTable_catalog() {
		return table_catalog;
	}

	public void setTable_catalog(String table_catalog) {
		this.table_catalog = table_catalog;
	}

	public String getTable_schema() {
		return table_schema;
	}

	public void setTable_schema(String table_schema) {
		this.table_schema = table_schema;
	}

	public String getTable_name() {
		return table_name;
	}

	public void setTable_name(String table_name) {
		this.table_name = table_name;
	}

	public String getColumn_name() {
		return column_name;
	}

	public void setColumn_name(String column_name) {
		this.column_name = column_name;
	}

	public String getIs_nullable() {
		return is_nullable;
	}

	public void setIs_nullable(String is_nullable) {
		this.is_nullable = is_nullable;
	}

	public String getData_type() {
		return data_type;
	}

	public void setData_type(String data_type) {
		this.data_type = data_type;
	}

	public String getCharacter_maximum_length() {
		return character_maximum_length;
	}

	public void setCharacter_maximum_length(String character_maximum_length) {
		this.character_maximum_length = character_maximum_length;
	}

	public String getCharacter_octet_length() {
		return character_octet_length;
	}

	public void setCharacter_octet_length(String character_octet_length) {
		this.character_octet_length = character_octet_length;
	}
}
