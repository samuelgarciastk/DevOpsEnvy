package status;

import interfaces.SonarProjStat;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import tool.Authentication;
import tool.Host;
import tool.HttpUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Author: stk
 * Date: 4/2/17
 * Time: 9:02 PM
 */
public class SonarProjStatImpl implements SonarProjStat {
    private static Logger logger = Logger.getLogger(SonarProjStatImpl.class);
    private String[] metrics = new String[]{
            "ncloc",
            "complexity",
            "duplicated_lines_density",
            "comment_lines_density",
            "sqale_index",
            "violations",
            "blocker_violations",
            "critical_violations",
            "major_violations",
            "minor_violations",
            "info_violations",
    };

    /**
     * Get project status.
     *
     * @param key Project key
     * @return Map of status: [total, changed since previous version]
     * All keys are in SonarProjStatImpl.metrics.
     * The meanings of keys: https://docs.sonarqube.org/display/SONARQUBE45/Metric+definitions
     * If there is no information then it will return null.
     */
    public Map<String, String[]> getStatus(String key) {
        StringBuilder url = new StringBuilder(Host.getSonar() + "api/measures/component?metricKeys=");
        for (String metric : metrics) {
            url.append(metric).append(",");
        }
        url.deleteCharAt(url.length() - 1);
        url.append("&componentKey=").append(key);
        String json = null;
        try {
            Object[] response = HttpUtils.sendGet(url.toString(), Authentication.getBasicAuth("sonar"));
            if (Integer.parseInt(response[0].toString()) == 200) {
                json = response[1].toString();
            }
        } catch (Exception e) {
            logger.error("Get sonar project status: GET request error.", e);
        }
        if (json == null) {
            logger.warn("Get sonar project status: response empty.");
            return null;
        }
        Map<String, Object> map = JSONObject.fromObject(json);
        Map<String, Object> component = JSONObject.fromObject(map.get("component"));
        List<Map<String, Object>> measures = JSONArray.fromObject(component.get("measures"));
        if (measures.size() == 0) {
            logger.warn("Get sonar project status: empty metrics.");
            return null;
        }
        Map<String, String[]> result = new HashMap<>();
        for (Map<String, Object> metric : measures) {
            String[] values = new String[2];
            values[0] = metric.get("value").toString();
            if (metric.containsKey("periods")) {
                List<Map<String, Object>> periods = JSONArray.fromObject(metric.get("periods"));
                values[1] = periods.get(1).get("value").toString();
            } else {
                values[1] = "0";
            }
            result.put(metric.get("metric").toString(), values);
        }
        return result;
    }

    /**
     * Get project quality gate status.
     *
     * @param key Project key
     * @return Status
     * If there is no information then it will return null.
     */
    public String getQualityGates(String key) {
        String url = Host.getSonar() + "api/qualitygates/project_status?projectKey=" + key;
        String json = null;
        try {
            Object[] response = HttpUtils.sendGet(url, Authentication.getBasicAuth("sonar"));
            if (Integer.parseInt(response[0].toString()) == 200) {
                json = response[1].toString();
            }
        } catch (Exception e) {
            logger.error("Get sonar project quality gate: GET request error.", e);
        }
        if (json == null) {
            logger.warn("Get sonar project quality gate: response empty.");
            return null;
        }
        Map<String, Object> map = JSONObject.fromObject(json);
        Map<String, Object> status = JSONObject.fromObject(map.get("projectStatus"));
        return status.get("status").toString();
    }

    /**
     * Get last analysis time.
     *
     * @param key Project key
     * @return time: yyyy-MM-dd HH:mm:ss
     * If there is no information then it will return null.
     */
    public String getAnalysisTime(String key) {
        String url = Host.getSonar() + "api/projects?versions=true&key=" + key;
        String json = null;
        try {
            Object[] response = HttpUtils.sendGet(url, Authentication.getBasicAuth("sonar"));
            if (Integer.parseInt(response[0].toString()) == 200) {
                json = response[1].toString();
            }
        } catch (Exception e) {
            logger.error("Get last analysis time: GET request error.", e);
        }
        if (json == null) {
            logger.warn("Get last analysis time: response empty.");
            return null;
        }
        System.out.println(json);
        Pattern pattern = Pattern.compile("\"d\":\"(.*?)\"");
        Matcher matcher = pattern.matcher(json);
        if (matcher.find()) {
            return matcher.group(1);
        }
        logger.warn("Get last analysis time: cannot get time.");
        return null;
    }
}
