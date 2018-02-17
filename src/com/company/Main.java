package com.company;

import java.sql.*;

public class Main {

  public static void main(String[] args) throws SQLException {
    // укажи нужные данные (название БД, имя пользователя, пароль, поменяй скрипт на любой селект)
    // и должно сработать: будет распечатан сначала текст скрипта, потом результат выборки
    final String connStr = "jdbc:custom:mysql://localhost:3306/<dbname>?user=<username>&password=<password>";
    DriverManager.registerDriver(new CustomDriver());
    final Connection conn = DriverManager.getConnection(connStr);
    final PreparedStatement ps = conn.prepareStatement("SELECT * FROM <table>");
    final ResultSet rs = ps.executeQuery();
    final ResultSetMetaData md = rs.getMetaData();
    final int colCount = md.getColumnCount();
    final StringBuilder sb = new StringBuilder();
    while (rs.next()) {
      int i = 1;
      while (i < colCount) {
        sb.append(rs.getObject(i)).append(", ");
        ++i;
      }

      sb.append(rs.getObject(i)).append('\n');
    }

    conn.close();
    System.out.println(sb.toString());
  }
}
