package org.firstinspires.ftc.teamcode.Helpers;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class JsonIO {
    JSONObject jsonObject;
    JSONParser parser;

    public JsonIO() {
        this.jsonObject = new JSONObject();
        this.parser = new JSONParser();
    }

    /**
     * (For Auto) Add key-value pair to JSON object, does not write
     * @param key
     * @param value
     */
    public void addJSON(String key, String value) {
        try {
            jsonObject.put(key, value);
        } catch (Exception e) {
            System.out.println("Unable to put to JSON");
        }
    }

    /**
     * (For Auto) Write the JSON Object to the robot
     */
    public void writeJSON() {
        try {
            FileWriter file = new FileWriter("robotState.json");
            file.write(jsonObject.toJSONString());
            file.flush();
            file.close();
        } catch (IOException e) {
            System.out.println("Failed to write JSON to file");
        }
    }

    /**
     * (For TeleOp) Reads JSON file on the robot, does not throw error if the read fails
     * @return If the read was successful
     */
    public boolean readJSON() {
        JSONParser parser = new JSONParser();
        try {
            jsonObject = (JSONObject) parser.parse(new FileReader("robotState.json"));

        } catch (Exception e) {
            System.out.println(e);
            return false;
        }
        return true;
    }

    /**
     * (For TeleOp) Get value from JSON by key after reading
     * @param key
     * @return Value, if not successful returns null
     */
    public String getJSON(String key) {
        try {
            return (String) jsonObject.get(key);
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }
}
