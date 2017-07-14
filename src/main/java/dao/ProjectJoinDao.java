package dao;

import model.Join;

import java.util.List;

public interface ProjectJoinDao {

    public boolean addJoin(String userName, String projectName, String time);

    public boolean deleteJoin(String userName, String projectName);

    public List<Join> getList(String column, String value);

}
