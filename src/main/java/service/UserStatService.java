package service;

import model.UserStat;

public interface UserStatService {

    public UserStat getUserStatistics(String userName);

    public UserStat getUserStatisticsQuick(String userName);

}
