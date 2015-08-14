package br.feevale.utils;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Map;

import com.esotericsoftware.yamlbeans.YamlException;
import com.esotericsoftware.yamlbeans.YamlReader;

public class Config {
	private static Config self;
	private String configFile;
	private Map<String, Object> config;
	
	public Config(String configFile) throws FileNotFoundException, YamlException {
		this.configFile = configFile;
		this.loadData();
	}
	
	public Config() throws FileNotFoundException, YamlException {
		this.configFile = "config/config.yaml";
		this.loadData();
	}
	
	public static Config getInstance() throws FileNotFoundException, YamlException {
		if (Config.self == null) {
			Config.self = new Config();
		}
		return Config.self;
	}
	
	private void loadData() throws FileNotFoundException, YamlException {
		YamlReader reader = new YamlReader(new FileReader(this.configFile));
		this.config = (Map) reader.read();
	}
	
	public Object get(String key) {
		return this.config.get(key);
	}

}
