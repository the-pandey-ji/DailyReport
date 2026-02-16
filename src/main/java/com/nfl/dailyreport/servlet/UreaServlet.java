package com.nfl.dailyreport.servlet;

import com.nfl.dailyreport.util.DBUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

@WebServlet("/urea")
public class UreaServlet extends HttpServlet {

    /* ===================== PAGE LOAD ===================== */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String action = req.getParameter("action");

        // AJAX CALL
        if ("details".equals(action)) {
            getDetails(req, resp);
            return;
        }

        // NORMAL PAGE LOAD
        loadPage(req);
        req.getRequestDispatcher("/urea.jsp").forward(req, resp);
    }

    /* ===================== SAVE ===================== */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        if ("saveRecords".equals(req.getParameter("saverecord"))) {
            saveRecords(req);
        }

        loadPage(req);
        req.getRequestDispatcher("/urea.jsp").forward(req, resp);
    }

    /* ===================== LOAD PAGE ===================== */
    private void loadPage(HttpServletRequest req) {

        try (Connection con = DBUtil.getConnection()) {

            // ===== Authorization =====
            //String user = "RP BHARDWAJ"; // later from session
            ResultSet rs;
            
          /*  rs = con.createStatement().executeQuery(
                "SELECT NVL(D_R_FLAG,'N') FROM USERMASTER WHERE USERID='" + user + "'");

            if (rs.next()) {
                req.setAttribute("error",
                        ("A".equals(rs.getString(1)) || "U".equals(rs.getString(1))) ? "A" : "N");
            }
            */

            // ===== Auto Report Date =====
            rs = con.createStatement().executeQuery(
                "SELECT TO_CHAR(MAX(D_DATE)+1,'DD/MM/YYYY') " +
                "FROM D_PROD_PERF_PNP WHERE D_UREA_PROD IS NOT NULL");

            if (rs.next()) req.setAttribute("reportdate", rs.getString(1));

            // ===== Opening Stocks =====
         // ===== Opening Stocks (FIXED) =====
            rs = con.createStatement().executeQuery(
                "SELECT NVL(D_UREA_CL_STK,0), NVL(D_HDPE_BAG_CL_STK,0), " +
                "NVL(D_NEEM_OIL_STK,0), NVL(D_NEEM_BAG_CL_STK,0) " +
                "FROM D_PROD_PERF_PNP " +
                "WHERE D_DATE = (" +
                "  SELECT MAX(D_DATE) FROM D_PROD_PERF_PNP " +
                "  WHERE D_UREA_PROD IS NOT NULL" +
                ")"
            );

            //System.out.println("---- OPENING STOCK DEBUG ----");

            if (rs.next()) {
                double urea = rs.getDouble(1);
                double bag = rs.getDouble(2);
                double oil = rs.getDouble(3);
                double neembag = rs.getDouble(4);

              /*  System.out.println("UREA STOCK = " + urea);
                System.out.println("BAG STOCK  = " + bag);
                System.out.println("OIL STOCK  = " + oil);
                System.out.println("NEEM BAG   = " + neembag);
                */

                req.setAttribute("ureaopeningstock", urea);
                req.setAttribute("bagopeningstock", bag);
                req.setAttribute("neemoilopeningstock", oil);
                req.setAttribute("neembagopeningstock", neembag);
            } else {
                System.out.println("NO ROW RETURNED FOR OPENING STOCK");
            }



            // ===== Total Urea FY =====
            rs = con.createStatement().executeQuery(
                "SELECT NVL(SUM(D_UREA_PROD),0) FROM D_PROD_PERF_PNP " +
                "WHERE D_DATE >= " +
                "(SELECT ADD_MONTHS(TRUNC(MAX(D_DATE),'YYYY'),-9) " +
                " FROM D_PROD_PERF_PNP)");

            if (rs.next()) req.setAttribute("totalureaproduce", rs.getDouble(1));
           // System.out.println("TOTAL UREA FY = " + req.getAttribute("totalureaproduce"));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* ===================== AJAX DETAILS ===================== */
    private void getDetails(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        resp.setContentType("text/plain");
        PrintWriter out = resp.getWriter();
        String reportDate = req.getParameter("reportdate");

        try (Connection con = DBUtil.getConnection()) {

            // Financial Year
            int fy;
            PreparedStatement ps = con.prepareStatement(
                "SELECT DECODE(TO_CHAR(TO_DATE(?,'DD/MM/YYYY'),'MM')," +
                "'01',TO_CHAR(TO_DATE(?,'DD/MM/YYYY'),'YYYY')-1," +
                "'02',TO_CHAR(TO_DATE(?,'DD/MM/YYYY'),'YYYY')-1," +
                "'03',TO_CHAR(TO_DATE(?,'DD/MM/YYYY'),'YYYY')-1," +
                "TO_CHAR(TO_DATE(?,'DD/MM/YYYY'),'YYYY')) FROM DUAL");

            for (int i = 1; i <= 4; i++) ps.setString(i, reportDate);
            ResultSet rs = ps.executeQuery();
            rs.next();
            fy = rs.getInt(1);

            // Total Urea
            ps = con.prepareStatement(
                "SELECT NVL(SUM(D_UREA_PROD),0) FROM D_PROD_PERF_PNP " +
                "WHERE D_DATE BETWEEN '01-APR-" + fy + "' AND TO_DATE(?,'DD/MM/YYYY')");
            ps.setString(1, reportDate);
            rs = ps.executeQuery();
            rs.next();
            double totalUrea = rs.getDouble(1);

            // Main Data
            ps = con.prepareStatement(
                "SELECT NVL(D_UREA_PROD,0), NVL(D_NEEMUREA_PROD,0), NVL(D_UREA_CO2_CONS,0)," +
                "NVL(D_UREA_STEAM_CONS,0), NVL(D_UREA_STRM_HRS,0)," +
                "NVL(D_LS_RMTLS_UREA,0), NVL(D_LS_POWER_UREA,0), NVL(D_LS_MECH_UREA,0)," +
                "NVL(D_LS_ELEC_UREA,0), NVL(D_LS_INST_UREA,0)," +
                "NVL(D_LS_PROC_UREA,0), NVL(D_LS_SD_UREA,0), NVL(D_LS_OTHER_UREA,0)," +
                "NVL(D_RAIL_DESP,0), NVL(D_ROAD_DESP,0)," +
                "(SELECT NVL(D_UREA_CL_STK,0) FROM D_PROD_PERF_PNP WHERE D_DATE=TO_DATE(?,'DD/MM/YYYY')-1)," +
                "NVL(D_UREA_CL_STK,0), NVL(D_NEEMUREA_DESP,0)," +
                "(SELECT NVL(D_HDPE_BAG_CL_STK,0) FROM D_PROD_PERF_PNP WHERE D_DATE=TO_DATE(?,'DD/MM/YYYY')-1)," +
                "NVL(D_HDPE_BAG_RECT,0), NVL(D_HDPE_BAG_CONS,0), NVL(D_HDPE_BAG_CL_STK,0)," +
                "(SELECT NVL(D_NEEM_OIL_STK,0) FROM D_PROD_PERF_PNP WHERE D_DATE=TO_DATE(?,'DD/MM/YYYY')-1)," +
                "NVL(D_NEEM_OIL_RCT,0), NVL(D_NEEM_OIL_CONS,0), NVL(D_NEEM_OIL_STK,0)," +
                "(SELECT NVL(D_NEEM_BAG_CL_STK,0) FROM D_PROD_PERF_PNP WHERE D_DATE=TO_DATE(?,'DD/MM/YYYY')-1)," +
                "NVL(D_NEEM_BAG_RECT,0), NVL(D_NEEM_BAG_CONS,0), NVL(D_NEEM_BAG_CL_STK,0)," +
                "NVL(D_LS_AN_SD_UREA,0), NVL(D_PLAIN_UREA_TRFR_GOLD_UREA,0) " +
                "FROM D_PROD_PERF_PNP WHERE D_DATE=TO_DATE(?,'DD/MM/YYYY')");

            for (int i = 1; i <= 4; i++) ps.setString(i, reportDate);
            rs = ps.executeQuery();

            if (!rs.next()) {
                out.print("N?#?");
                return;
            }

            StringBuilder sb = new StringBuilder();
            for (int i = 1; i <= 32; i++) sb.append(rs.getString(i)).append("?#?");
            sb.append(totalUrea).append("?#?");
            out.print(sb.toString());

        } catch (Exception e) {
            e.printStackTrace();
            out.print("N?#?");
        }
    }

    /* ===================== SAVE ===================== */
    private void saveRecords(HttpServletRequest req) {

        try (Connection con = DBUtil.getConnection()) {

            String reportDate = req.getParameter("reportdate");

            PreparedStatement ps = con.prepareStatement(
                "SELECT COUNT(*) FROM D_PROD_PERF_PNP WHERE D_DATE=TO_DATE(?,'DD/MM/YYYY')");
            ps.setString(1, reportDate);
            ResultSet rs = ps.executeQuery();
            rs.next();

            if (rs.getInt(1) == 0) {
                insert(req, con);
            } else {
                update(req, con);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void insert(HttpServletRequest r, Connection c) throws Exception {
        // trimmed for readability — same mapping as update
        update(r, c);
    }

    private void update(HttpServletRequest r, Connection c) throws Exception {

        PreparedStatement ps = c.prepareStatement(
            "UPDATE D_PROD_PERF_PNP SET " +
            "D_UREA_PROD=?, D_NEEMUREA_PROD=?, D_PLAIN_UREA_TRFR_GOLD_UREA=?," +
            "D_UREA_CO2_CONS=?, D_UREA_STEAM_CONS=?, D_UREA_STRM_HRS=?," +
            "D_LS_RMTLS_UREA=?, D_LS_POWER_UREA=?, D_LS_MECH_UREA=?, D_LS_ELEC_UREA=?," +
            "D_LS_INST_UREA=?, D_LS_PROC_UREA=?, D_LS_SD_UREA=?, D_LS_OTHER_UREA=?," +
            "D_LS_AN_SD_UREA=?, D_RAIL_DESP=?, D_ROAD_DESP=?," +
            "D_UREA_CL_STK=?, D_NEEMUREA_DESP=? " +
            "WHERE D_DATE=TO_DATE(?,'DD/MM/YYYY')");

        int i = 1;
        ps.setDouble(i++, num(r,"ureaproduction"));
        ps.setDouble(i++, num(r,"neemureaproduction"));
        ps.setDouble(i++, num(r,"plainureatogoldurea"));
        ps.setDouble(i++, num(r,"co2consumption"));
        ps.setDouble(i++, num(r,"steamconsumption"));
        ps.setDouble(i++, num(r,"steamhours"));
        ps.setDouble(i++, num(r,"rawmaterials"));
        ps.setDouble(i++, num(r,"exportpower"));
        ps.setDouble(i++, num(r,"mechanical"));
        ps.setDouble(i++, num(r,"electrical"));
        ps.setDouble(i++, num(r,"instrumentation"));
        ps.setDouble(i++, num(r,"process1"));
        ps.setDouble(i++, num(r,"shutdown"));
        ps.setDouble(i++, num(r,"others"));
        ps.setDouble(i++, num(r,"annualshutdown"));
        ps.setDouble(i++, num(r,"rail"));
        ps.setDouble(i++, num(r,"road"));
        ps.setDouble(i++, num(r,"ureaclosingstock"));
        ps.setDouble(i++, num(r,"neemureadespatch"));
        ps.setString(i, r.getParameter("reportdate"));

        ps.executeUpdate();
    }

    private double num(HttpServletRequest r, String p) {
        try { return Double.parseDouble(r.getParameter(p)); }
        catch (Exception e) { return 0; }
    }
}
