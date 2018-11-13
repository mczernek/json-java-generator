package pl.mczernek.jsonjavagenerator;

import pl.mczernek.annotation.JsonFile;

@JsonFile(path = "../config.json")
public class Config {

    public String test() {
        return new JsonFile_Config().toString();
    }

}
