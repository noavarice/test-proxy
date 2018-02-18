package com.company;

import java.sql.*;
import java.util.Enumeration;
import java.util.Properties;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CustomDriver implements Driver {

  private static final Pattern URL_FORMAT = Pattern.compile("^jdbc:(custom:).+");

  @Override
  public Connection connect(final String s, final Properties properties) throws SQLException {
    final Matcher m = URL_FORMAT.matcher(s);
    if (!m.find()) {
      return null;
    }

    final Enumeration<Driver> loadedDrivers = DriverManager.getDrivers();
    final Class<?> currentClass = this.getClass();
    final String realConnectionString = s.replaceFirst(m.group(1), "");
    while (loadedDrivers.hasMoreElements()) {
      final Driver d = loadedDrivers.nextElement();
      if (d.getClass().equals(currentClass)) {
        continue;
      }

      final Connection realConnection = d.connect(realConnectionString, properties);
      if (realConnection != null) {
        return new CustomConnection(realConnection);
      }
    }

    throw new SQLException("No suitable driver found for underlying connection");
  }

  @Override
  public boolean acceptsURL(final String s) throws SQLException {
    return URL_FORMAT.matcher(s).find();
  }

  @Override
  public DriverPropertyInfo[] getPropertyInfo(String s, Properties properties) throws SQLException {
    return new DriverPropertyInfo[0];
  }

  @Override
  public int getMajorVersion() {
    return 0;
  }

  @Override
  public int getMinorVersion() {
    return 0;
  }

  @Override
  public boolean jdbcCompliant() {
    return false;
  }

  @Override
  public Logger getParentLogger() throws SQLFeatureNotSupportedException {
    return null;
  }
}
