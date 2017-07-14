package dao;

import model.Join;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ProjectJoinDaoImpl implements ProjectJoinDao {

    private DaoHelper daoHelper;

    public ProjectJoinDaoImpl() {
        this.daoHelper = DaoHelperImpl.getBaseDaoInstance();
    }

    @Override
    public boolean addJoin(String userName, String projectName, String time) {
        boolean res = false;
        try {
            Connection conn = daoHelper.getConnection();
            PreparedStatement prep = conn.prepareStatement("insert into `join` values(?,?,?);");
            prep.setString(1, projectName);
            prep.setString(2, userName);
            prep.setString(3, time);
            int ret = prep.executeUpdate();
            if (ret > 0) {
                res = true;
            } else {
                res = false;
            }
            daoHelper.closeConnection(conn);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    @Override
    public boolean deleteJoin(String userName, String projectName) {
        boolean res = false;
        try {
            Connection conn = daoHelper.getConnection();
            PreparedStatement prep = conn.prepareStatement("delete from `join` where projectname=? and username=?;");
            prep.setString(1, projectName);
            prep.setString(2, userName);
            int ret = prep.executeUpdate();
            if (ret > 0) {
                res = true;
            } else {
                res = false;
            }
            daoHelper.closeConnection(conn);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    @Override
    public List<Join> getList(String column, String value) {
        List<Join> res = new ArrayList<Join>();
        try {
            Connection conn = daoHelper.getConnection();
            String sql = "select * from `join` where " + column + "=? order by jointime;";
            PreparedStatement preState = conn.prepareStatement(sql);
            preState.setString(1, value);
            ResultSet rs = preState.executeQuery();
            while (rs.next()) {
                Join j = new Join();
                j.setProjectName(rs.getString("projectname"));
                j.setUserName(rs.getString("username"));
                j.setJoinTime(rs.getString("jointime"));
                res.add(j);
            }
            rs.close();
            daoHelper.closeConnection(conn);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

}
