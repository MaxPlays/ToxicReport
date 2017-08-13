package me.MaxPlays.ToxicReport.io;

import me.MaxPlays.ToxicReport.ToxicReport;

import java.sql.*;


public class SQL {

	private String HOST = "";
	private String DATABASE = "";
	private String USER = "";
	private String PORT = "";
	private String PASSWORD = "";

	private String filename = "";

	private boolean mysql;

	private ToxicReport plugin;

	private Connection con;

	public SQL(String host, String database, String user, String password, String port, ToxicReport plugin){
		this.HOST = host;
		this.DATABASE = database;
		this.USER = user;
		this.PASSWORD = password;
		this.PORT = port;
		this.plugin = plugin;
		this.mysql = true;
	}
	public SQL(String filename, ToxicReport plugin){
		this.filename = filename;
		this.plugin = plugin;
		this.mysql = false;
	}

	public void connect(){
		if(connected())
			return;
		try{
			if(mysql){
				con = DriverManager.getConnection("jdbc:MySQL://" + HOST + ":" + PORT + "/" + DATABASE + "?autoreconnect=true", USER, PASSWORD);
				System.out.println("[SQL] Connection established");
			}else{
				Class.forName("org.sqlite.JDBC");

				if(plugin.getDataFolder().exists())
					plugin.getDataFolder().mkdir();
				con = DriverManager.getConnection("jdbc:sqlite:plugins/" + plugin.getDescription().getName() + "/" + this.filename + ".db");
				System.out.println("[SQL] Connection established");
			}

		}catch(Exception e){
			System.out.println("[SQL] Connection failed! Error: " + e.getMessage());
		}
	}

	public void disconnect(){
		try{
			if(connected()){
			con.close();
			System.out.println("[SQL] Disconnected");
			
			}
		
		}catch(SQLException e){
			System.out.println("[SQL] Error while disconnecting: " + e.getMessage());
		}
	}
	public boolean connected(){
		return con == null ? false : true;
	}

	public Connection getCon() {
		return con;
	}

	public void update(String qry){
		try {
			Statement st = con.createStatement();
			st.executeUpdate(qry);
			st.close();
		} catch (SQLException e) {
			System.err.println(e);
		}
	}
	public ResultSet query(String qry){
		try {
			return con.createStatement().executeQuery(qry);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
}

