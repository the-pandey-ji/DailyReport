/* package com.nfl.dailyreport.dao;

import com.nfl.dailyreport.dto.UreaDTO;
import com.nfl.dailyreport.util.DBUtil;

import java.sql.*;

public class DailyReportDAO {

    public boolean exists(Connection con, String reportDate) throws Exception {
        String sql = """
            SELECT COUNT(*) 
            FROM D_PROD_PERF_PNP
            WHERE D_DATE = TO_DATE(?, 'DD/MM/YYYY')
        """;

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, reportDate);
            ResultSet rs = ps.executeQuery();
            rs.next();
            return rs.getInt(1) > 0;
        }
    }

    public void insert(Connection con, UreaDTO d) throws Exception {

        String sql = """
        INSERT INTO D_PROD_PERF_PNP
        (
          D_DATE, D_UREA_PROD, D_NEEMUREA_PROD,
          D_PLAIN_UREA_TRFR_GOLD_UREA,
          D_UREA_CO2_CONS, D_UREA_STEAM_CONS, D_UREA_STRM_HRS,
          D_LS_RMTLS_UREA, D_LS_POWER_UREA, D_LS_MECH_UREA,
          D_LS_ELEC_UREA, D_LS_INST_UREA, D_LS_PROC_UREA,
          D_LS_SD_UREA, D_LS_AN_SD_UREA, D_LS_OTHER_UREA,
          D_RAIL_DESP, D_ROAD_DESP,
          D_UREA_CL_STK, D_NEEMUREA_DESP,
          D_HDPE_BAG_RECT, D_HDPE_BAG_CONS, D_HDPE_BAG_CL_STK,
          D_NEEM_OIL_RCT, D_NEEM_OIL_CONS, D_NEEM_OIL_STK,
          D_NEEM_BAG_RECT, D_NEEM_BAG_CONS, D_NEEM_BAG_CL_STK
        )
        VALUES
        (
          TO_DATE(?, 'DD/MM/YYYY'),?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?
        )
        """;

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            int i = 1;
            ps.setString(i++, d.reportDate);
            ps.setString(i++, d.ureaProd);
            ps.setString(i++, d.neemUreaProd);
            ps.setString(i++, d.plainToGold);
            ps.setString(i++, d.co2);
            ps.setString(i++, d.steam);
            ps.setString(i++, d.steamHrs);
            ps.setString(i++, d.rawMat);
            ps.setString(i++, d.power);
            ps.setString(i++, d.mechanical);
            ps.setString(i++, d.electrical);
            ps.setString(i++, d.instrumentation);
            ps.setString(i++, d.process);
            ps.setString(i++, d.shutdown);
            ps.setString(i++, d.annualShutdown);
            ps.setString(i++, d.others);
            ps.setString(i++, d.rail);
            ps.setString(i++, d.road);
            ps.setString(i++, d.ureaCl);
            ps.setString(i++, d.neemDesp);
            ps.setString(i++, d.bagRec);
            ps.setString(i++, d.bagCons);
            ps.setString(i++, d.bagCl);
            ps.setString(i++, d.neemOilRec);
            ps.setString(i++, d.neemOilCons);
            ps.setString(i++, d.neemOilCl);
            ps.setString(i++, d.neemBagRec);
            ps.setString(i++, d.neemBagCons);
            ps.setString(i++, d.neemBagCl);

            ps.executeUpdate();
        }
    }  

	public void update(Connection con, UreaDTO dto) {
		// TODO Auto-generated method stub
		
	}

	public void log(Connection con, String reportDate, String userId, String ip) {
		// TODO Auto-generated method stub
		
	}
}
*/
