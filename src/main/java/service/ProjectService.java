package service;

import model.Project;
import model.ProjectDetailListBean;
import model.ProjectListBean;

import java.util.List;

public interface ProjectService {

    public ProjectListBean getProjectList(String userName);

    public ProjectDetailListBean getProjectDetailList();

    public boolean createProject(String userName, String projectName, String projectKey, String repository, String artifact);

    public Project getProjectDetail(String projectName);

    public boolean joinProject(String userName, String projectName);

    public boolean quitProject(String userName, String projectName);

    public List<String> getProjectMember(String projectName);

    public List<String> getUserJoinList(String userName);

    public String getProjectKey(String projectName);

    public boolean updateProjectArtifact(String projectName, String artifact);

}
