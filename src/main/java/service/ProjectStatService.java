package service;

import model.BuildStatus;

import java.util.List;
import java.util.Map;

public interface ProjectStatService {

    public List<BuildStatus> getBuildStatistics(Map<String, String> buildConditions);

}
