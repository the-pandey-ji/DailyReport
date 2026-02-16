
/*
package com.nfl.dailyreport.service;

import java.sql.Connection;

import com.nfl.dailyreport.dao.DailyReportDAO;
import com.nfl.dailyreport.dto.UreaDTO;
import com.nfl.dailyreport.util.DBUtil;

public class DailyReportService {

    private final DailyReportDAO dao = new DailyReportDAO();

    public void saveUrea(UreaDTO dto) throws Exception {
        try (Connection con = DBUtil.getConnection()) {
            con.setAutoCommit(false);

            if (dao.exists(con, dto.reportDate)) {
                dao.update(con, dto);
            } else {
                dao.insert(con, dto);
            }

            dao.log(con, dto.reportDate, dto.userId, dto.ip);
            con.commit();
        }
    }

}
*/