package interfaces;

import java.util.Map;

/**
 * Author: stk
 * Date: 3/24/17
 * Time: 8:03 PM
 */
public interface JenkinsProjStat {
    /**
     * Get the last build status.
     *
     * @param name Project name
     * @return Map of statuses
     * Key: result(build succeeded of failed), timestamp(begin time), duration(build duration).
     * If there is no information then it will return null.
     */
    Map<String, String> getLastBuild(String name);

    /**
     * Get the build frequency of the last five builds.
     *
     * @param name Project name
     * @return Build frequency: day HH:mm:ss
     * If the number of build is less than two, then it will return null.
     */
    String getFrequency(String name);

    /**
     * Get the build success rate of the last ten builds.
     *
     * @param name Project name
     * @return Success rate
     * If the number of build is less than two, then it will return -1.
     */
    double getSuccessRate(String name);

    /**
     * Get build result of the last ten builds.
     *
     * @param name Project name
     * @return Map: <time, result>
     * If no build then return null.
     */
    Map<String, String> getBuildResult(String name);
}
