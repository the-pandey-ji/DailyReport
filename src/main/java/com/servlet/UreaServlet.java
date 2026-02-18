

package com.servlet;

import com.util.DBUtil;
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

        if ("details".equals(action)) {
            getDetails(req, resp);
            return;
        }

        loadPage(req);
        req.getRequestDispatcher("/urea.jsp").forward(req, resp);
    }

    /* ===================== SAVE ===================== */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

    	String saveFlag = req.getParameter("saverecord");

        if ("saveRecords".equals(saveFlag)
                && "POST".equalsIgnoreCase(req.getMethod())) {
            saveRecords(req);   // ✅ only explicit save
        }

        loadPage(req);
        req.getRequestDispatcher("/urea.jsp").forward(req, resp);
    }

    /* ===================== LOAD PAGE ===================== */
    private void loadPage(HttpServletRequest req) {

        try (Connection con = DBUtil.getConnection()) {

            ResultSet rs;

            rs = con.createStatement().executeQuery(
                "SELECT TO_CHAR(MAX(D_DATE)+1,'DD/MM/YYYY') " +
                "FROM D_PROD_PERF_PNP WHERE D_UREA_PROD IS NOT NULL");
            if (rs.next()) req.setAttribute("reportdate", rs.getString(1));

            rs = con.createStatement().executeQuery(
                "SELECT NVL(D_UREA_CL_STK,0), NVL(D_HDPE_BAG_CL_STK,0), " +
                "NVL(D_NEEM_OIL_STK,0), NVL(D_NEEM_BAG_CL_STK,0) " +
                "FROM D_PROD_PERF_PNP WHERE D_DATE=(" +
                "SELECT MAX(D_DATE) FROM D_PROD_PERF_PNP WHERE D_UREA_PROD IS NOT NULL)");

            if (rs.next()) {
                req.setAttribute("ureaopeningstock", rs.getDouble(1));
                req.setAttribute("bagopeningstock", rs.getDouble(2));
                req.setAttribute("neemoilopeningstock", rs.getDouble(3));
                req.setAttribute("neembagopeningstock", rs.getDouble(4));
            }

            rs = con.createStatement().executeQuery(
                "SELECT NVL(SUM(D_UREA_PROD),0) FROM D_PROD_PERF_PNP " +
                "WHERE D_DATE >= (" +
                "SELECT ADD_MONTHS(TRUNC(MAX(D_DATE),'YYYY'),-9) FROM D_PROD_PERF_PNP)");
            if (rs.next()) req.setAttribute("totalureaproduce", rs.getDouble(1));

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

            /* ---- Financial Year ---- */
            PreparedStatement ps = con.prepareStatement(
                "SELECT DECODE(TO_CHAR(TO_DATE(?,'DD/MM/YYYY'),'MM')," +
                "'01',TO_CHAR(TO_DATE(?,'DD/MM/YYYY'),'YYYY')-1," +
                "'02',TO_CHAR(TO_DATE(?,'DD/MM/YYYY'),'YYYY')-1," +
                "'03',TO_CHAR(TO_DATE(?,'DD/MM/YYYY'),'YYYY')-1," +
                "TO_CHAR(TO_DATE(?,'DD/MM/YYYY'),'YYYY')) FROM DUAL");

            ps.setString(1, reportDate);
            ps.setString(2, reportDate);
            ps.setString(3, reportDate);
            ps.setString(4, reportDate);
            ps.setString(5, reportDate);

            ResultSet rs = ps.executeQuery();
            rs.next();
            int fy = rs.getInt(1);

            /* ---- Total Urea ---- */
            ps = con.prepareStatement(
                "SELECT NVL(SUM(D_UREA_PROD),0) FROM D_PROD_PERF_PNP " +
                "WHERE D_DATE BETWEEN '01-APR-" + fy +
                "' AND TO_DATE(?,'DD/MM/YYYY')");
            ps.setString(1, reportDate);
            rs = ps.executeQuery();
            rs.next();
            double totalUrea = rs.getDouble(1);

            /* ---- Main Data ---- */
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

            ps.setString(1, reportDate);
            ps.setString(2, reportDate);
            ps.setString(3, reportDate);
            ps.setString(4, reportDate);
            ps.setString(5, reportDate);

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
    	// 🔐 SAFETY CHECK
        if (req.getParameter("reportdate") == null ||
            req.getParameter("reportdate").trim().isEmpty()) {
            return;
        }

        try (Connection con = DBUtil.getConnection()) {

            PreparedStatement ps = con.prepareStatement(
                "SELECT COUNT(*) FROM D_PROD_PERF_PNP WHERE D_DATE=TO_DATE(?,'DD/MM/YYYY')");
            ps.setString(1, req.getParameter("reportdate"));
            ResultSet rs = ps.executeQuery();
            rs.next();

            if (rs.getInt(1) == 0) insert(req, con);
            else update(req, con);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* insert() and update() remain EXACTLY same as yours */

   



    private void insert(HttpServletRequest r, Connection c) throws Exception {

        PreparedStatement ps = c.prepareStatement(
            "INSERT INTO D_PROD_PERF_PNP (" +
            "D_DATE," +

            "D_UREA_PROD, D_NEEMUREA_PROD, D_PLAIN_UREA_TRFR_GOLD_UREA," +
            "D_UREA_CO2_CONS, D_UREA_STEAM_CONS, D_UREA_STRM_HRS," +

            "D_LS_RMTLS_UREA, D_LS_POWER_UREA, D_LS_MECH_UREA, D_LS_ELEC_UREA," +
            "D_LS_INST_UREA, D_LS_PROC_UREA, D_LS_SD_UREA, D_LS_OTHER_UREA," +
            "D_LS_AN_SD_UREA," +

            "D_RAIL_DESP, D_ROAD_DESP," +
            "D_UREA_CL_STK, D_NEEMUREA_DESP," +

            "D_HDPE_BAG_RECT, D_HDPE_BAG_CONS, D_HDPE_BAG_CL_STK," +
            "D_NEEM_OIL_RCT, D_NEEM_OIL_CONS, D_NEEM_OIL_STK," +
            "D_NEEM_BAG_RECT, D_NEEM_BAG_CONS, D_NEEM_BAG_CL_STK" +
            ") VALUES (" +
            "TO_DATE(?,'DD/MM/YYYY')," +
            "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"
        );

        int i = 1;
        ps.setString(i++, r.getParameter("reportdate"));

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

        ps.setDouble(i++, num(r,"receipt"));
        ps.setDouble(i++, num(r,"consumption"));
        ps.setDouble(i++, num(r,"bagclosingstock"));

        ps.setDouble(i++, num(r,"neemoilreceipt"));
        ps.setDouble(i++, num(r,"neemoilconsumption"));
        ps.setDouble(i++, num(r,"neemoilclosingstock"));

        ps.setDouble(i++, num(r,"neemreceipt"));
        ps.setDouble(i++, num(r,"neemconsumption"));
        ps.setDouble(i++, num(r,"neembagclosingstock"));

        ps.executeUpdate();
    }


    private void update(HttpServletRequest r, Connection c) throws Exception {

        PreparedStatement ps = c.prepareStatement(
            "UPDATE D_PROD_PERF_PNP SET " +

            "D_UREA_PROD=?, D_NEEMUREA_PROD=?, D_PLAIN_UREA_TRFR_GOLD_UREA=?," +
            "D_UREA_CO2_CONS=?, D_UREA_STEAM_CONS=?, D_UREA_STRM_HRS=?," +

            "D_LS_RMTLS_UREA=?, D_LS_POWER_UREA=?, D_LS_MECH_UREA=?, D_LS_ELEC_UREA=?," +
            "D_LS_INST_UREA=?, D_LS_PROC_UREA=?, D_LS_SD_UREA=?, D_LS_OTHER_UREA=?," +
            "D_LS_AN_SD_UREA=?," +

            "D_RAIL_DESP=?, D_ROAD_DESP=?," +
            "D_UREA_CL_STK=?, D_NEEMUREA_DESP=?," +

            "D_HDPE_BAG_RECT=?, D_HDPE_BAG_CONS=?, D_HDPE_BAG_CL_STK=?," +
            "D_NEEM_OIL_RCT=?, D_NEEM_OIL_CONS=?, D_NEEM_OIL_STK=?," +
            "D_NEEM_BAG_RECT=?, D_NEEM_BAG_CONS=?, D_NEEM_BAG_CL_STK=? " +

            "WHERE D_DATE=TO_DATE(?,'DD/MM/YYYY')"
        );

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

        ps.setDouble(i++, num(r,"receipt"));
        ps.setDouble(i++, num(r,"consumption"));
        ps.setDouble(i++, num(r,"bagclosingstock"));

        ps.setDouble(i++, num(r,"neemoilreceipt"));
        ps.setDouble(i++, num(r,"neemoilconsumption"));
        ps.setDouble(i++, num(r,"neemoilclosingstock"));

        ps.setDouble(i++, num(r,"neemreceipt"));
        ps.setDouble(i++, num(r,"neemconsumption"));
        ps.setDouble(i++, num(r,"neembagclosingstock"));

        ps.setString(i, r.getParameter("reportdate"));

        ps.executeUpdate();
    }

    private double num(HttpServletRequest r, String p) {
        try { return Double.parseDouble(r.getParameter(p)); }
        catch (Exception e) { return 0; }
    }
}
