package com.edc.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ProjectConfigUtils {

	private static final String SystemConfigFileRelativePath = "../system.properties";

	private static Properties projectProperties = null;

	public static String getValue(String key) {
		return projectProperties.getProperty(key);
	}

	static {
		InputStream inMain = ProjectConfigUtils.class.getClassLoader()
				.getResourceAsStream(SystemConfigFileRelativePath);

		projectProperties = new Properties();
		try {
			projectProperties.load(inMain);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (inMain != null) {
					inMain.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public static String getDefaultFileShareRestorePath() {
		return projectProperties.getProperty("default.fileshare.restore.path");
	}
}
