package com.servlet;

import com.util.DBUtil;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.sql.*;
import java.util.*;

@WebServlet("/pnpDailyReport")
public class PNPDailyReport extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String date = req.getParameter("reportDate");

        if (date == null || date.trim().isEmpty()) {
            req.getRequestDispatcher("pnpDailyReport.jsp").forward(req, resp);
            return;
        }

        Map<String, Object> report = fetchReport(date);

        req.setAttribute("report", report);
        req.setAttribute("selectedDate", date);

        req.getRequestDispatcher("pnpDailyReport.jsp").forward(req, resp);
    }

    private Map<String, Object> fetchReport(String date) {
        Map<String, Object> map = new HashMap<>();

        try (Connection con = DBUtil.getConnection()) {

            /* ================= MAIN QUERY (DAILY + STOCKS + LOSSES) ================= */
            String sql =
                "SELECT a.D_AMM_PROD, a.D_UREA_PROD, a.D_STM_PROD_SGP, a.D_STM_PROD_CPP, " +
                "a.D_BS_PROD, a.D_GTGPOWERGEN, a.D_POWR_IMPORT, a.D_NG_RECEIPT, " +
                "a.D_RAIL_DESP, a.D_ROAD_DESP, a.D_AMM_TO_OTHER_UNITS, a.D_BS_DESP, " +
                "a.D_AMM_NG_CONS, a.D_AMM_TO_UREA, a.D_UREA_CO2_CONS, a.D_UREA_STEAM_CONS, " +
                "a.D_COAL_CONS_SGP, a.D_FUEL_NG_CONS_SGP, a.D_NG_CONSM_HRSG, " +
                "a.D_NG_CONSM_GTG, a.D_STM_CONS_TG1_CPP, a.D_STM_CONS_TG2_CPP, " +
                "a.D_AMM_CL_STK, a.D_UREA_CL_STK, a.D_COAL_CL_STK, a.D_HDPE_BAG_CL_STK, " +
                "a.D_H2SO4_CL_STK, a.D_AMDEA_CL_STK, a.D_NAOH_CL_STK, a.D_NEEM_OIL_STK, a.D_BS_CL_STK, " +
                "a.D_LS_RMTLS_AMM, a.D_LS_POWER_AMM, a.D_LS_MECH_AMM, a.D_LS_ELEC_AMM, " +
                "a.D_LS_INST_AMM, a.D_LS_PROC_AMM, a.D_LS_SD_AMM, a.D_LS_AN_SD_AMM, a.D_LS_OTHER_AMM, " +
                "a.D_LS_RMTLS_UREA, a.D_LS_POWER_UREA, a.D_LS_MECH_UREA, a.D_LS_ELEC_UREA, " +
                "a.D_LS_INST_UREA, a.D_LS_PROC_UREA, a.D_LS_SD_UREA, a.D_LS_AN_SD_UREA, a.D_LS_OTHER_UREA, " +
                "b.D_TX_STATUS1, b.D_TX_STATUS2, b.D_TX_STATUS3, b.D_TX_STATUS4, b.D_TX_STATUS5 " +
                "FROM PRODUCTION.D_PROD_PERF_PNP a " +
                "LEFT JOIN PRODUCTION.D_TELEX_STATUS_PNP b ON a.D_DATE = b.D_TX_DATE " +
                "WHERE a.D_DATE = TO_DATE(?,'YYYY-MM-DD')";

            try(PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, date);
                try(ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        ResultSetMetaData metaData = rs.getMetaData();
                        int columnCount = metaData.getColumnCount();
                        for (int i = 1; i <= columnCount; i++) {
                            map.put(metaData.getColumnName(i), n(rs.getObject(i)));
                        }
                        
                        // Calculated Daily Totals
                        double ureaRail = rs.getDouble("D_RAIL_DESP");
                        double ureaRoad = rs.getDouble("D_ROAD_DESP");
                        map.put("D_UREA_TOTAL_DESP", n(ureaRail + ureaRoad));
                        
                        // Daily Losses Totals
                        map.put("D_LS_TOT_AMM", sumDailyLosses(rs, "AMM"));
                        map.put("D_LS_TOT_UREA", sumDailyLosses(rs, "UREA"));
                    }
                }
            }

            /* ================= MONTH CALC ================= */
            String monthSql = 
                "SELECT SUM(NVL(D_AMM_PROD,0)) M_AMM_PROD, SUM(NVL(D_UREA_PROD,0)) M_UREA_PROD, " +
                "SUM(NVL(D_STM_PROD_SGP,0)) M_STM_SGP, SUM(NVL(D_STM_PROD_CPP,0)) M_STM_CPP, " +
                "SUM(NVL(D_BS_PROD,0)) M_BS_PROD, SUM(NVL(D_GTGPOWERGEN,0)) M_PWR_GEN, " +
                "SUM(NVL(D_POWR_IMPORT,0)) M_PWR_IMP, SUM(NVL(D_NG_RECEIPT,0)) M_NG_REC, " +
                "SUM(NVL(D_RAIL_DESP,0)) M_RAIL, SUM(NVL(D_ROAD_DESP,0)) M_ROAD, " +
                "SUM(NVL(D_AMM_TO_OTHER_UNITS,0)) M_AMM_DESP, SUM(NVL(D_BS_DESP,0)) M_BS_DESP, " +
                "SUM(NVL(D_AMM_NG_CONS,0)) M_AMM_NG_CONS, SUM(NVL(D_AMM_TO_UREA,0)) M_AMM_TO_UREA, " +
                "SUM(NVL(D_UREA_CO2_CONS,0)) M_UREA_CO2, SUM(NVL(D_UREA_STEAM_CONS,0)) M_UREA_STM, " +
                "SUM(NVL(D_COAL_CONS_SGP,0)) M_COAL_SGP, SUM(NVL(D_FUEL_NG_CONS_SGP,0)) M_FNG_SGP, " +
                "SUM(NVL(D_NG_CONSM_HRSG,0)) M_NG_HRSG, SUM(NVL(D_NG_CONSM_GTG,0)) M_NG_GTG, " +
                "SUM(NVL(D_LS_RMTLS_AMM,0)) M_RMTL_A, SUM(NVL(D_LS_POWER_AMM,0)) M_PWR_A, " +
                "SUM(NVL(D_LS_MECH_AMM,0)) M_MEC_A, SUM(NVL(D_LS_ELEC_AMM,0)) M_ELE_A, " +
                "SUM(NVL(D_LS_INST_AMM,0)) M_INS_A, SUM(NVL(D_LS_PROC_AMM,0)) M_PRC_A, " +
                "SUM(NVL(D_LS_SD_AMM,0)) M_SD_A, SUM(NVL(D_LS_AN_SD_AMM,0)) M_ASD_A, SUM(NVL(D_LS_OTHER_AMM,0)) M_OTH_A, " +
                "SUM(NVL(D_LS_RMTLS_UREA,0)) M_RMTL_U, SUM(NVL(D_LS_POWER_UREA,0)) M_PWR_U, " +
                "SUM(NVL(D_LS_MECH_UREA,0)) M_MEC_U, SUM(NVL(D_LS_ELEC_UREA,0)) M_ELE_U, " +
                "SUM(NVL(D_LS_INST_UREA,0)) M_INS_U, SUM(NVL(D_LS_PROC_UREA,0)) M_PRC_U, " +
                "SUM(NVL(D_LS_SD_UREA,0)) M_SD_U, SUM(NVL(D_LS_AN_SD_UREA,0)) M_ASD_U, SUM(NVL(D_LS_OTHER_UREA,0)) M_OTH_U " +
                "FROM PRODUCTION.D_PROD_PERF_PNP " +
                "WHERE TO_CHAR(D_DATE,'MON-YYYY') = TO_CHAR(TO_DATE(?,'YYYY-MM-DD'),'MON-YYYY') " +
                "AND D_DATE <= TO_DATE(?,'YYYY-MM-DD')";
            
            try(PreparedStatement psm = con.prepareStatement(monthSql)) {
                psm.setString(1, date);
                psm.setString(2, date);
                try(ResultSet rm = psm.executeQuery()) {
                    if (rm.next()) {
                        map.put("M_AMM_PROD", n(rm.getObject("M_AMM_PROD")));
                        map.put("M_UREA_PROD", n(rm.getObject("M_UREA_PROD")));
                        map.put("M_STM_SGP", n(rm.getObject("M_STM_SGP")));
                        map.put("M_STM_CPP", n(rm.getObject("M_STM_CPP")));
                        map.put("M_BS_PROD", n(rm.getObject("M_BS_PROD")));
                        map.put("M_PWR_GEN", n(rm.getObject("M_PWR_GEN")));
                        map.put("M_PWR_IMP", n(rm.getObject("M_PWR_IMP")));
                        map.put("M_NG_REC", n(rm.getObject("M_NG_REC")));
                        
                        double mRail = rm.getDouble("M_RAIL");
                        double mRoad = rm.getDouble("M_ROAD");
                        map.put("M_RAIL", n(mRail));
                        map.put("M_ROAD", n(mRoad));
                        map.put("M_UREA_TOTAL_DESP", n(mRail + mRoad));
                        
                        map.put("M_AMM_DESP", n(rm.getObject("M_AMM_DESP")));
                        map.put("M_BS_DESP", n(rm.getObject("M_BS_DESP")));
                        
                        map.put("M_AMM_NG_CONS", n(rm.getObject("M_AMM_NG_CONS")));
                        map.put("M_AMM_TO_UREA", n(rm.getObject("M_AMM_TO_UREA")));
                        map.put("M_UREA_CO2", n(rm.getObject("M_UREA_CO2")));
                        map.put("M_UREA_STM", n(rm.getObject("M_UREA_STM")));
                        map.put("M_COAL_SGP", n(rm.getObject("M_COAL_SGP")));
                        map.put("M_FNG_SGP", n(rm.getObject("M_FNG_SGP")));
                        map.put("M_NG_HRSG", n(rm.getObject("M_NG_HRSG")));
                        map.put("M_NG_GTG", n(rm.getObject("M_NG_GTG")));

                        // Month Losses
                        map.put("M_LS_RMTLS_AMM", n(rm.getObject("M_RMTL_A")));
                        map.put("M_LS_POWER_AMM", n(rm.getObject("M_PWR_A")));
                        map.put("M_LS_MECH_AMM", n(rm.getObject("M_MEC_A")));
                        map.put("M_LS_ELEC_AMM", n(rm.getObject("M_ELE_A")));
                        map.put("M_LS_INST_AMM", n(rm.getObject("M_INS_A")));
                        map.put("M_LS_PROC_AMM", n(rm.getObject("M_PRC_A")));
                        map.put("M_LS_SD_AMM", n(rm.getObject("M_SD_A")));
                        map.put("M_LS_AN_SD_AMM", n(rm.getObject("M_ASD_A")));
                        map.put("M_LS_OTHER_AMM", n(rm.getObject("M_OTH_A")));
                        map.put("M_LS_TOT_AMM", n(sumLossesWithPrefix(rm, "M", "_A")));

                        map.put("M_LS_RMTLS_UREA", n(rm.getObject("M_RMTL_U")));
                        map.put("M_LS_POWER_UREA", n(rm.getObject("M_PWR_U")));
                        map.put("M_LS_MECH_UREA", n(rm.getObject("M_MEC_U")));
                        map.put("M_LS_ELEC_UREA", n(rm.getObject("M_ELE_U")));
                        map.put("M_LS_INST_UREA", n(rm.getObject("M_INS_U")));
                        map.put("M_LS_PROC_UREA", n(rm.getObject("M_PRC_U")));
                        map.put("M_LS_SD_UREA", n(rm.getObject("M_SD_U")));
                        map.put("M_LS_AN_SD_UREA", n(rm.getObject("M_ASD_U")));
                        map.put("M_LS_OTHER_UREA", n(rm.getObject("M_OTH_U")));
                        map.put("M_LS_TOT_UREA", n(sumLossesWithPrefix(rm, "M", "_U")));
                    }
                }
            }

            /* ================= YEAR CALC (FINANCIAL YEAR: 1st April) ================= */
            String yearSql = 
                "SELECT SUM(NVL(D_AMM_PROD,0)) Y_AMM_PROD, SUM(NVL(D_UREA_PROD,0)) Y_UREA_PROD, " +
                "SUM(NVL(D_STM_PROD_SGP,0)) Y_STM_SGP, SUM(NVL(D_STM_PROD_CPP,0)) Y_STM_CPP, " +
                "SUM(NVL(D_BS_PROD,0)) Y_BS_PROD, SUM(NVL(D_GTGPOWERGEN,0)) Y_PWR_GEN, " +
                "SUM(NVL(D_POWR_IMPORT,0)) Y_PWR_IMP, SUM(NVL(D_NG_RECEIPT,0)) Y_NG_REC, " +
                "SUM(NVL(D_RAIL_DESP,0)) Y_RAIL, SUM(NVL(D_ROAD_DESP,0)) Y_ROAD, " +
                "SUM(NVL(D_AMM_TO_OTHER_UNITS,0)) Y_AMM_DESP, SUM(NVL(D_BS_DESP,0)) Y_BS_DESP, " +
                "SUM(NVL(D_AMM_NG_CONS,0)) Y_AMM_NG_CONS, SUM(NVL(D_AMM_TO_UREA,0)) Y_AMM_TO_UREA, " +
                "SUM(NVL(D_UREA_CO2_CONS,0)) Y_UREA_CO2, SUM(NVL(D_UREA_STEAM_CONS,0)) Y_UREA_STM, " +
                "SUM(NVL(D_COAL_CONS_SGP,0)) Y_COAL_SGP, SUM(NVL(D_FUEL_NG_CONS_SGP,0)) Y_FNG_SGP, " +
                "SUM(NVL(D_NG_CONSM_HRSG,0)) Y_NG_HRSG, SUM(NVL(D_NG_CONSM_GTG,0)) Y_NG_GTG, " +
                "SUM(NVL(D_LS_RMTLS_AMM,0)) Y_RMTL_A, SUM(NVL(D_LS_POWER_AMM,0)) Y_PWR_A, " +
                "SUM(NVL(D_LS_MECH_AMM,0)) Y_MEC_A, SUM(NVL(D_LS_ELEC_AMM,0)) Y_ELE_A, " +
                "SUM(NVL(D_LS_INST_AMM,0)) Y_INS_A, SUM(NVL(D_LS_PROC_AMM,0)) Y_PRC_A, " +
                "SUM(NVL(D_LS_SD_AMM,0)) Y_SD_A, SUM(NVL(D_LS_AN_SD_AMM,0)) Y_ASD_A, SUM(NVL(D_LS_OTHER_AMM,0)) Y_OTH_A, " +
                "SUM(NVL(D_LS_RMTLS_UREA,0)) Y_RMTL_U, SUM(NVL(D_LS_POWER_UREA,0)) Y_PWR_U, " +
                "SUM(NVL(D_LS_MECH_UREA,0)) Y_MEC_U, SUM(NVL(D_LS_ELEC_UREA,0)) Y_ELE_U, " +
                "SUM(NVL(D_LS_INST_UREA,0)) Y_INS_U, SUM(NVL(D_LS_PROC_UREA,0)) Y_PRC_U, " +
                "SUM(NVL(D_LS_SD_UREA,0)) Y_SD_U, SUM(NVL(D_LS_AN_SD_UREA,0)) Y_ASD_U, SUM(NVL(D_LS_OTHER_UREA,0)) Y_OTH_U " +
                "FROM PRODUCTION.D_PROD_PERF_PNP " +
                "WHERE D_DATE BETWEEN ADD_MONTHS(TRUNC(ADD_MONTHS(TO_DATE(?,'YYYY-MM-DD'), -3), 'YEAR'), 3) " +
                "AND TO_DATE(?,'YYYY-MM-DD')";
            
            try(PreparedStatement psy = con.prepareStatement(yearSql)) {
                psy.setString(1, date);
                psy.setString(2, date);
                try(ResultSet ry = psy.executeQuery()) {
                    if (ry.next()) {
                        map.put("Y_AMM_PROD", n(ry.getObject("Y_AMM_PROD")));
                        map.put("Y_UREA_PROD", n(ry.getObject("Y_UREA_PROD")));
                        map.put("Y_STM_SGP", n(ry.getObject("Y_STM_SGP")));
                        map.put("Y_STM_CPP", n(ry.getObject("Y_STM_CPP")));
                        map.put("Y_BS_PROD", n(ry.getObject("Y_BS_PROD")));
                        map.put("Y_PWR_GEN", n(ry.getObject("Y_PWR_GEN")));
                        map.put("Y_PWR_IMP", n(ry.getObject("Y_PWR_IMP")));
                        map.put("Y_NG_REC", n(ry.getObject("Y_NG_REC")));
                        
                        double yRail = ry.getDouble("Y_RAIL");
                        double yRoad = ry.getDouble("Y_ROAD");
                        map.put("Y_RAIL", n(yRail));
                        map.put("Y_ROAD", n(yRoad));
                        map.put("Y_UREA_TOTAL_DESP", n(yRail + yRoad));
                        
                        map.put("Y_AMM_DESP", n(ry.getObject("Y_AMM_DESP")));
                        map.put("Y_BS_DESP", n(ry.getObject("Y_BS_DESP")));

                        map.put("Y_AMM_NG_CONS", n(ry.getObject("Y_AMM_NG_CONS")));
                        map.put("Y_AMM_TO_UREA", n(ry.getObject("Y_AMM_TO_UREA")));
                        map.put("Y_UREA_CO2", n(ry.getObject("Y_UREA_CO2")));
                        map.put("Y_UREA_STM", n(ry.getObject("Y_UREA_STM")));
                        map.put("Y_COAL_SGP", n(ry.getObject("Y_COAL_SGP")));
                        map.put("Y_FNG_SGP", n(ry.getObject("Y_FNG_SGP")));
                        map.put("Y_NG_HRSG", n(ry.getObject("Y_NG_HRSG")));
                        map.put("Y_NG_GTG", n(ry.getObject("Y_NG_GTG")));

                        // Year Losses
                        map.put("Y_LS_RMTLS_AMM", n(ry.getObject("Y_RMTL_A")));
                        map.put("Y_LS_POWER_AMM", n(ry.getObject("Y_PWR_A")));
                        map.put("Y_LS_MECH_AMM", n(ry.getObject("Y_MEC_A")));
                        map.put("Y_LS_ELEC_AMM", n(ry.getObject("Y_ELE_A")));
                        map.put("Y_LS_INST_AMM", n(ry.getObject("Y_INS_A")));
                        map.put("Y_LS_PROC_AMM", n(ry.getObject("Y_PRC_A")));
                        map.put("Y_LS_SD_AMM", n(ry.getObject("Y_SD_A")));
                        map.put("Y_LS_AN_SD_AMM", n(ry.getObject("Y_ASD_A")));
                        map.put("Y_LS_OTHER_AMM", n(ry.getObject("Y_OTH_A")));
                        map.put("Y_LS_TOT_AMM", n(sumLossesWithPrefix(ry, "Y", "_A")));

                        map.put("Y_LS_RMTLS_UREA", n(ry.getObject("Y_RMTL_U")));
                        map.put("Y_LS_POWER_UREA", n(ry.getObject("Y_PWR_U")));
                        map.put("Y_LS_MECH_UREA", n(ry.getObject("Y_MEC_U")));
                        map.put("Y_LS_ELEC_UREA", n(ry.getObject("Y_ELE_U")));
                        map.put("Y_LS_INST_UREA", n(ry.getObject("Y_INS_U")));
                        map.put("Y_LS_PROC_UREA", n(ry.getObject("Y_PRC_U")));
                        map.put("Y_LS_SD_UREA", n(ry.getObject("Y_SD_U")));
                        map.put("Y_LS_AN_SD_UREA", n(ry.getObject("Y_ASD_U")));
                        map.put("Y_LS_OTHER_UREA", n(ry.getObject("Y_OTH_U")));
                        map.put("Y_LS_TOT_UREA", n(sumLossesWithPrefix(ry, "Y", "_U")));
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return map;
    }

    private Object n(Object o) {
        return o == null ? "0" : o; 
    }

    private double sumDailyLosses(ResultSet rs, String suffix) throws SQLException {
        return rs.getDouble("D_LS_RMTLS_" + suffix) + rs.getDouble("D_LS_POWER_" + suffix) +
               rs.getDouble("D_LS_MECH_" + suffix) + rs.getDouble("D_LS_ELEC_" + suffix) +
               rs.getDouble("D_LS_INST_" + suffix) + rs.getDouble("D_LS_PROC_" + suffix) +
               rs.getDouble("D_LS_SD_" + suffix) + rs.getDouble("D_LS_AN_SD_" + suffix) +
               rs.getDouble("D_LS_OTHER_" + suffix);
    }

    private double sumLossesWithPrefix(ResultSet rs, String prefix, String suffix) throws SQLException {
        return rs.getDouble(prefix + "_RMTL" + suffix) +
               rs.getDouble(prefix + "_PWR" + suffix) +
               rs.getDouble(prefix + "_MEC" + suffix) +
               rs.getDouble(prefix + "_ELE" + suffix) +
               rs.getDouble(prefix + "_INS" + suffix) +
               rs.getDouble(prefix + "_PRC" + suffix) +
               rs.getDouble(prefix + "_SD" + suffix) +
               rs.getDouble(prefix + "_ASD" + suffix) +
               rs.getDouble(prefix + "_OTH" + suffix);
    }
}