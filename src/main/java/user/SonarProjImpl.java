package user;

import interfaces.SonarProj;
import net.sf.json.JSONArray;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;
import tool.Authentication;
import tool.Host;
import tool.HttpUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author: stk
 * Date: 3/20/17
 * Time: 8:05 PM
 */
public class SonarProjImpl implements SonarProj {
    private static Logger logger = Logger.getLogger(SonarProjImpl.class);

    /**
     * Get all projects in SonarQube.
     *
     * @return List of project names and keys: [name, key]
     * If the list is empty then it will return null.
     */
    public List<String[]> getAllProject() {
        String url = Host.getSonar() + "api/projects/index";
        String json = null;
        try {
            Object[] response = HttpUtils.sendGet(url, Authentication.getBasicAuth("sonar"));
            if (Integer.parseInt(response[0].toString()) == 200) {
                json = response[1].toString();
            }
        } catch (Exception e) {
            logger.error("Get sonar project list: GET request error.", e);
        }
        if (json == null) {
            logger.warn("Get sonar project list: response empty.");
            return null;
        }
        List<Map<String, Object>> projects = JSONArray.fromObject(json);
        if (projects.size() == 0) {
            logger.warn("Get sonar project list: empty list.");
            return null;
        }
        List<String[]> result = new ArrayList<>();
        for (Map<String, Object> oneProject : projects) {
            result.add(new String[]{oneProject.get("nm").toString(), oneProject.get("k").toString()});
        }
        return result;
    }

    /**
     * Create a project in SonarQube.
     *
     * @param name Project name
     * @param key  Project key(project identifier)
     * @return Success or fail
     */
    public boolean createProject(String name, String key) {
        List<NameValuePair> param = new ArrayList<>();
        param.add(new BasicNameValuePair("name", name));
        param.add(new BasicNameValuePair("key", key));
        Map<String, String> props = new HashMap<>();
        props.put("Authorization", Authentication.getBasicAuth("sonar"));
        String url = Host.getSonar() + "api/projects/create";
        try {
            Object[] response = HttpUtils.sendPost(url, param, props);
            if (Integer.parseInt(response[0].toString()) == 200) return true;
        } catch (Exception e) {
            logger.error("Create sonar project: POST request error.", e);
        }
        return false;
    }
}
