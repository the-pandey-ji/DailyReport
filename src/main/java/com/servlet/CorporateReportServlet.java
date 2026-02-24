package com.servlet;

import com.util.DBUtil;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.sql.*;
import java.util.*;

@WebServlet("/dailyReport")
public class CorporateReportServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String date = req.getParameter("reportDate");

        if (date == null || date.trim().isEmpty()) {
            req.getRequestDispatcher("CODailyReport.jsp").forward(req, resp);
            return;
        }

        Map<String, Object> report = fetchReport(date);

        req.setAttribute("report", report);
        req.setAttribute("selectedDate", date);

        req.getRequestDispatcher("CODailyReport.jsp").forward(req, resp);
    }

    private Map<String, Object> fetchReport(String date) {

        Map<String, Object> map = new HashMap<>();

        try (Connection con = DBUtil.getConnection()) {

            /* ================= MAIN QUERY (DAILY + STOCKS) ================= */
            String sql =
                "SELECT a.D_AMM_PROD, a.D_UREA_PROD, a.D_BS_PROD, " +
                "a.D_GTGPOWERGEN, a.D_AMM_NG_CONS, a.D_RAIL_DESP, a.D_ROAD_DESP, " +
                "a.D_BS_DESP, a.D_AMM_TO_OTHER_UNITS, a.D_FLY_ASH, " +
                "a.D_AMM_CL_STK, a.D_UREA_CL_STK, a.D_BS_CL_STK, " +
                "a.D_COAL_RECEIPT, a.D_COAL_CONS_SGP, a.D_COAL_CONS_CPP, a.D_COAL_CL_STK, " +
                "a.D_NG_RECEIPT, a.D_NG_CONS_GTG, " +
                "a.D_HDPE_BAG_RECT, a.D_HDPE_BAG_CONS, a.D_HDPE_BAG_CL_STK, " +
                "a.D_NEEM_BAG_RECT, a.D_NEEM_BAG_CONS, a.D_NEEM_BAG_CL_STK, " +
                "a.D_POWR_IMPORT, a.D_POWR_CONS_UNIT, " +
                "a.D_NEEM_OIL_RCT, a.D_NEEM_OIL_CONS, a.D_NEEM_OIL_STK, " +
                "b.D_TX_STATUS1, b.D_TX_STATUS2, b.D_TX_STATUS3, " +
                "b.D_TX_STATUS4, b.D_TX_STATUS5, " +
                "c.D_SIGN_NAME, c.D_SIGN_DESIGNATION " +
                "FROM PRODUCTION.D_PROD_PERF_PNP a " +
                "LEFT JOIN PRODUCTION.D_TELEX_STATUS_PNP b ON a.D_DATE = b.D_TX_DATE " +
                "LEFT JOIN PRODUCTION.D_TELEX_SIGN c ON b.D_SIGN_AUTH = c.D_SIGN_AUTH " +
                "WHERE a.D_DATE = TO_DATE(?,'YYYY-MM-DD')";

            try(PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, date);
                try(ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        // Daily Production
                        map.put("D_AMM_PROD", n(rs.getObject("D_AMM_PROD")));
                        map.put("D_UREA_PROD", n(rs.getObject("D_UREA_PROD")));
                        map.put("D_BS_PROD", n(rs.getObject("D_BS_PROD")));
                        map.put("D_POWER_PROD", n(rs.getObject("D_GTGPOWERGEN")));
                        map.put("D_NG_CONS", n(rs.getObject("D_AMM_NG_CONS"))); 
                        
                        // Daily Despatch
                        double rail = rs.getDouble("D_RAIL_DESP");
                        double road = rs.getDouble("D_ROAD_DESP");
                        map.put("D_UREA_RAIL", n(rail));
                        map.put("D_UREA_ROAD", n(road));
                        map.put("D_UREA_TOTAL", n(rail + road));
                        map.put("D_BS_DESP", n(rs.getObject("D_BS_DESP")));
                        map.put("D_AMM_DESP", n(rs.getObject("D_AMM_TO_OTHER_UNITS")));
                        map.put("D_FLY_ASH", n(rs.getObject("D_FLY_ASH")));

                        // Stocks & Consumptions
                        map.put("D_AMM_CL_STK", n(rs.getObject("D_AMM_CL_STK")));
                        map.put("D_UREA_CL_STK", n(rs.getObject("D_UREA_CL_STK")));
                        map.put("D_BS_CL_STK", n(rs.getObject("D_BS_CL_STK")));
                        
                        map.put("D_COAL_RECEIPT", n(rs.getObject("D_COAL_RECEIPT")));
                        map.put("D_COAL_CONS_SGP", n(rs.getObject("D_COAL_CONS_SGP")));
                        map.put("D_COAL_CONS_CPP", n(rs.getObject("D_COAL_CONS_CPP")));
                        map.put("D_COAL_CL_STK", n(rs.getObject("D_COAL_CL_STK")));

                        map.put("D_NG_RECEIPT", n(rs.getObject("D_NG_RECEIPT")));
                        map.put("D_NG_CONS_GTG", n(rs.getObject("D_NG_CONS_GTG")));

                        map.put("D_HDPE_RECT", n(rs.getObject("D_HDPE_BAG_RECT")));
                        map.put("D_HDPE_CONS", n(rs.getObject("D_HDPE_BAG_CONS")));
                        map.put("D_HDPE_STK", n(rs.getObject("D_HDPE_BAG_CL_STK")));
                        
                        map.put("D_NEEM_RECT", n(rs.getObject("D_NEEM_BAG_RECT")));
                        map.put("D_NEEM_CONS", n(rs.getObject("D_NEEM_BAG_CONS")));
                        map.put("D_NEEM_STK", n(rs.getObject("D_NEEM_BAG_CL_STK")));
                        
                        map.put("D_POWR_IMPORT", n(rs.getObject("D_POWR_IMPORT")));
                        map.put("D_POWR_CONS_UNIT", n(rs.getObject("D_POWR_CONS_UNIT")));
                        
                        map.put("D_NEEMOIL_RCT", n(rs.getObject("D_NEEM_OIL_RCT")));
                        map.put("D_NEEMOIL_CONS", n(rs.getObject("D_NEEM_OIL_CONS")));
                        map.put("D_NEEMOIL_STK", n(rs.getObject("D_NEEM_OIL_STK")));

                        // Status & Sign
                        map.put("D_TX_STATUS1", s(rs.getString("D_TX_STATUS1")));
                        map.put("D_TX_STATUS2", s(rs.getString("D_TX_STATUS2")));
                        map.put("D_TX_STATUS3", s(rs.getString("D_TX_STATUS3")));
                        map.put("D_TX_STATUS4", s(rs.getString("D_TX_STATUS4")));
                        map.put("D_TX_STATUS5", s(rs.getString("D_TX_STATUS5")));
                        map.put("D_SIGN_NAME", s(rs.getString("D_SIGN_NAME")));
                        map.put("D_SIGN_DESIGNATION", s(rs.getString("D_SIGN_DESIGNATION")));
                    }
                }
            }

            /* ================= MONTH CALC ================= */
            String monthSql = 
                "SELECT SUM(NVL(D_AMM_PROD,0)) M_AMM, SUM(NVL(D_UREA_PROD,0)) M_UREA, " +
                "SUM(NVL(D_BS_PROD,0)) M_BS, SUM(NVL(D_GTGPOWERGEN,0)) M_PWR, " +
                "SUM(NVL(D_AMM_NG_CONS,0)) M_NG, " +
                "SUM(NVL(D_RAIL_DESP,0)) M_RAIL, SUM(NVL(D_ROAD_DESP,0)) M_ROAD, " +
                "SUM(NVL(D_BS_DESP,0)) M_BS_DESP, SUM(NVL(D_AMM_TO_OTHER_UNITS,0)) M_AMM_DESP, " +
                "SUM(NVL(D_FLY_ASH,0)) M_FLY_ASH " +
                "FROM PRODUCTION.D_PROD_PERF_PNP " +
                "WHERE TO_CHAR(D_DATE,'MON-YYYY') = TO_CHAR(TO_DATE(?,'YYYY-MM-DD'),'MON-YYYY') " +
                "AND D_DATE <= TO_DATE(?,'YYYY-MM-DD')";
            
            try(PreparedStatement psm = con.prepareStatement(monthSql)) {
                psm.setString(1, date);
                psm.setString(2, date);
                try(ResultSet rm = psm.executeQuery()) {
                    if (rm.next()) {
                        map.put("M_AMM_PROD", n(rm.getObject("M_AMM")));
                        map.put("M_UREA_PROD", n(rm.getObject("M_UREA")));
                        map.put("M_BS_PROD", n(rm.getObject("M_BS")));
                        map.put("M_POWER_PROD", n(rm.getObject("M_PWR")));
                        map.put("M_NG_CONS", n(rm.getObject("M_NG")));
                        map.put("M_UREA_RAIL", n(rm.getObject("M_RAIL")));
                        map.put("M_UREA_ROAD", n(rm.getObject("M_ROAD")));
                        map.put("M_UREA_TOTAL", n(rm.getDouble("M_RAIL") + rm.getDouble("M_ROAD")));
                        map.put("M_BS_DESP", n(rm.getObject("M_BS_DESP")));
                        map.put("M_AMM_DESP", n(rm.getObject("M_AMM_DESP")));
                        map.put("M_FLY_ASH", n(rm.getObject("M_FLY_ASH")));
                    }
                }
            }

            /* ================= YEAR CALC (FINANCIAL YEAR: 1st April) ================= */
            String yearSql = 
                "SELECT SUM(NVL(D_AMM_PROD,0)) Y_AMM, SUM(NVL(D_UREA_PROD,0)) Y_UREA, " +
                "SUM(NVL(D_BS_PROD,0)) Y_BS, SUM(NVL(D_GTGPOWERGEN,0)) Y_PWR, " +
                "SUM(NVL(D_AMM_NG_CONS,0)) Y_NG, " +
                "SUM(NVL(D_RAIL_DESP,0)) Y_RAIL, SUM(NVL(D_ROAD_DESP,0)) Y_ROAD, " +
                "SUM(NVL(D_BS_DESP,0)) Y_BS_DESP, SUM(NVL(D_AMM_TO_OTHER_UNITS,0)) Y_AMM_DESP, " +
                "SUM(NVL(D_FLY_ASH,0)) Y_FLY_ASH " +
                "FROM PRODUCTION.D_PROD_PERF_PNP " +
                "WHERE D_DATE BETWEEN ADD_MONTHS(TRUNC(ADD_MONTHS(TO_DATE(?,'YYYY-MM-DD'), -3), 'YEAR'), 3) " +
                "AND TO_DATE(?,'YYYY-MM-DD')";
            
            try(PreparedStatement psy = con.prepareStatement(yearSql)) {
                psy.setString(1, date);
                psy.setString(2, date);
                try(ResultSet ry = psy.executeQuery()) {
                    if (ry.next()) {
                        map.put("Y_AMM_PROD", n(ry.getObject("Y_AMM")));
                        map.put("Y_UREA_PROD", n(ry.getObject("Y_UREA")));
                        map.put("Y_BS_PROD", n(ry.getObject("Y_BS")));
                        map.put("Y_POWER_PROD", n(ry.getObject("Y_PWR")));
                        map.put("Y_NG_CONS", n(ry.getObject("Y_NG")));
                        map.put("Y_UREA_RAIL", n(ry.getObject("Y_RAIL")));
                        map.put("Y_UREA_ROAD", n(ry.getObject("Y_ROAD")));
                        map.put("Y_UREA_TOTAL", n(ry.getDouble("Y_RAIL") + ry.getDouble("Y_ROAD")));
                        map.put("Y_BS_DESP", n(ry.getObject("Y_BS_DESP")));
                        map.put("Y_AMM_DESP", n(ry.getObject("Y_AMM_DESP")));
                        map.put("Y_FLY_ASH", n(ry.getObject("Y_FLY_ASH")));
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return map;
    }

    private Object n(Object o) {
        return o == null ? "0.000" : o;
    }

    private String s(String s) {
        return s == null ? "" : s;
    }
}