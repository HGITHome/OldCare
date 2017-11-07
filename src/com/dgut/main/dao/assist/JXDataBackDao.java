package com.dgut.main.dao.assist;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.dgut.main.entity.assist.JXField;



public interface JXDataBackDao {

	public List<String> listTables();
	
	public List<String> listTabels(String catalog);  

	public List<JXField> listFields(String tablename);

	public List<String> listDataBases();
	

	public String createTableDDL(String tablename);
	
	public String getDefaultCatalog()throws SQLException;
	
	public void setDefaultCatalog(String catalog) throws SQLException;

	public List<Object[]> createTableData(String tablename);
	
	public Boolean executeSQL(String sql);
	

}