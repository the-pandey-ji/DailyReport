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
            req.getRequestDispatcher("CODailyReport.jsp")
                    .forward(req, resp);
            return;
        }

        Map<String, Object> report = fetchReport(date);

        req.setAttribute("report", report);
        req.setAttribute("selectedDate", date);

        req.getRequestDispatcher("CODailyReport.jsp")
                .forward(req, resp);
    }

    private Map<String, Object> fetchReport(String date) {

        Map<String, Object> map = new HashMap<>();

        try (Connection con = DBUtil.getConnection()) {

            /* ================= MAIN QUERY ================= */
            String sql =
                "SELECT a.D_AMM_PROD, a.D_UREA_PROD, " +
                "a.D_AMM_CL_STK, a.D_UREA_CL_STK, " +
                "b.D_TX_STATUS1, b.D_TX_STATUS2, b.D_TX_STATUS3, " +
                "b.D_TX_STATUS4, b.D_TX_STATUS5, " +
                "c.D_SIGN_NAME, c.D_SIGN_DESIGNATION " +
                "FROM D_PROD_PERF_PNP a " +
                "LEFT JOIN D_TELEX_STATUS_PNP b ON a.D_DATE = b.D_TX_DATE " +
                "LEFT JOIN D_TELEX_SIGN c ON b.D_SIGN_AUTH = c.D_SIGN_AUTH " +
                "WHERE a.D_DATE = TO_DATE(?,'YYYY-MM-DD')";

            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, date);
            ResultSet rs = ps.executeQuery();

            if (!rs.next())
                return map;

            map.put("D_AMM_PROD", n(rs.getObject("D_AMM_PROD")));
            map.put("D_UREA_PROD", n(rs.getObject("D_UREA_PROD")));
            map.put("D_AMM_CL_STK", n(rs.getObject("D_AMM_CL_STK")));
            map.put("D_UREA_CL_STK", n(rs.getObject("D_UREA_CL_STK")));

            map.put("D_TX_STATUS1", s(rs.getString("D_TX_STATUS1")));
            map.put("D_TX_STATUS2", s(rs.getString("D_TX_STATUS2")));
            map.put("D_TX_STATUS3", s(rs.getString("D_TX_STATUS3")));
            map.put("D_TX_STATUS4", s(rs.getString("D_TX_STATUS4")));
            map.put("D_TX_STATUS5", s(rs.getString("D_TX_STATUS5")));

            map.put("D_SIGN_NAME", s(rs.getString("D_SIGN_NAME")));
            map.put("D_SIGN_DESIGNATION", s(rs.getString("D_SIGN_DESIGNATION")));

            /* ================= MONTH CALC ================= */
            PreparedStatement psMonth = con.prepareStatement(
                "SELECT SUM(NVL(D_AMM_PROD,0)), SUM(NVL(D_UREA_PROD,0)) " +
                "FROM D_PROD_PERF_PNP " +
                "WHERE TO_CHAR(D_DATE,'MON-YYYY') = " +
                "TO_CHAR(TO_DATE(?,'YYYY-MM-DD'),'MON-YYYY') " +
                "AND D_DATE <= TO_DATE(?,'YYYY-MM-DD')"
            );
            psMonth.setString(1, date);
            psMonth.setString(2, date);

            ResultSet rm = psMonth.executeQuery();
            if (rm.next()) {
                map.put("M_AMM_PROD", n(rm.getObject(1)));
                map.put("M_UREA_PROD", n(rm.getObject(2)));
            }

            /* ================= YEAR CALC ================= */
            PreparedStatement psYear = con.prepareStatement(
                "SELECT SUM(NVL(D_AMM_PROD,0)), SUM(NVL(D_UREA_PROD,0)) " +
                "FROM D_PROD_PERF_PNP " +
                "WHERE D_DATE BETWEEN " +
                "TRUNC(TO_DATE(?,'YYYY-MM-DD'),'YEAR') " +
                "AND TO_DATE(?,'YYYY-MM-DD')"
            );
            psYear.setString(1, date);
            psYear.setString(2, date);

            ResultSet ry = psYear.executeQuery();
            if (ry.next()) {
                map.put("Y_AMM_PROD", n(ry.getObject(1)));
                map.put("Y_UREA_PROD", n(ry.getObject(2)));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return map;
    }

    private Object n(Object o) {
        return o == null ? "" : o;
    }

    private String s(String s) {
        return s == null ? "" : s;
    }
}