package com.servlet;

import com.util.DBUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

@WebServlet("/offsites")
public class OffsitesUtilitiesServlet extends HttpServlet {

    /* ================= LOAD PAGE ================= */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String action = req.getParameter("action");
        if ("details".equals(action)) {
            getDetails(req, resp);
            return;
        }

        loadInitialData(req);
        req.getRequestDispatcher("/offsitesUtilities.jsp").forward(req, resp);
    }

    /* ================= SAVE ================= */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

    	 String saveFlag = req.getParameter("saverecord");

    	    if ("saveRecords".equals(saveFlag)
    	            && "POST".equalsIgnoreCase(req.getMethod())) {
    	        saveRecords(req);   // ✅ only explicit save
    	    }
    	    
        loadInitialData(req);
        req.getRequestDispatcher("/offsitesUtilities.jsp").forward(req, resp);
    }

    /* ================= INITIAL LOAD ================= */
    private void loadInitialData(HttpServletRequest req) {

        try (Connection con = DBUtil.getConnection();
             Statement st = con.createStatement()) {

            ResultSet rs = st.executeQuery(
                "SELECT TO_CHAR(MAX(D_DATE)+1,'DD/MM/YYYY') " +
                "FROM D_PROD_PERF_PNP WHERE D_H2SO4_CL_STK IS NOT NULL");

            if (rs.next())
                req.setAttribute("reportdate", rs.getString(1));

            rs = st.executeQuery(
                "SELECT NVL(D_H2SO4_CL_STK,0), NVL(D_NAOH_CL_STK,0), " +
                "NVL(D_BS_CL_STK,0), NVL(D_MS_CL_STK,0), " +
                "NVL(D_BC_CL_STK,0), NVL(D_BS_BAG_CL_STK,0) " +
                "FROM D_PROD_PERF_PNP WHERE D_DATE=(" +
                "SELECT MAX(D_DATE) FROM D_PROD_PERF_PNP " +
                "WHERE D_H2SO4_CL_STK IS NOT NULL)");

            if (rs.next()) {
                req.setAttribute("h2so4_op", rs.getDouble(1));
                req.setAttribute("naoh_op", rs.getDouble(2));
                req.setAttribute("bs_op", rs.getDouble(3));
                req.setAttribute("ms_op", rs.getDouble(4));
                req.setAttribute("bc_op", rs.getDouble(5));
                req.setAttribute("bs_bag_op", rs.getDouble(6));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* ================= AJAX DETAILS ================= */
    private void getDetails(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        resp.setContentType("text/plain");
        PrintWriter out = resp.getWriter();
        String date = req.getParameter("reportdate");

        try (Connection con = DBUtil.getConnection()) {

            PreparedStatement ps = con.prepareStatement(
                "SELECT " +
                "(SELECT NVL(D_H2SO4_CL_STK,0) FROM D_PROD_PERF_PNP WHERE D_DATE=TO_DATE(?,'DD/MM/YYYY')-1)," +
                "NVL(D_H2SO4_RECEIPT,0), NVL(D_H2SO4_CONS,0), NVL(D_H2SO4_CL_STK,0)," +

                "(SELECT NVL(D_NAOH_CL_STK,0) FROM D_PROD_PERF_PNP WHERE D_DATE=TO_DATE(?,'DD/MM/YYYY')-1)," +
                "NVL(D_NAOH_RECEIPT,0), NVL(D_NAOH_CONS,0), NVL(D_NAOH_CL_STK,0)," +

                "(SELECT NVL(D_BS_CL_STK,0) FROM D_PROD_PERF_PNP WHERE D_DATE=TO_DATE(?,'DD/MM/YYYY')-1)," +
                "NVL(D_BS_PROD,0), NVL(D_BS_DESP,0), NVL(D_BS_CL_STK,0)," +

                "(SELECT NVL(D_MS_CL_STK,0) FROM D_PROD_PERF_PNP WHERE D_DATE=TO_DATE(?,'DD/MM/YYYY')-1)," +
                "NVL(D_MS_RECT,0), NVL(D_MS_CONS,0), NVL(D_MS_CL_STK,0)," +

                "(SELECT NVL(D_BC_CL_STK,0) FROM D_PROD_PERF_PNP WHERE D_DATE=TO_DATE(?,'DD/MM/YYYY')-1)," +
                "NVL(D_BC_RECT,0), NVL(D_BC_CONS,0), NVL(D_BC_CL_STK,0)," +

                "(SELECT NVL(D_BS_BAG_CL_STK,0) FROM D_PROD_PERF_PNP WHERE D_DATE=TO_DATE(?,'DD/MM/YYYY')-1)," +
                "NVL(D_BS_BAG_RECT,0), NVL(D_BS_BAG_CONS,0), NVL(D_BS_BAG_CL_STK,0)," +
                "NVL(D_BS_STEAM_CONS,0) " +
                "FROM D_PROD_PERF_PNP WHERE D_DATE=TO_DATE(?,'DD/MM/YYYY')"
            );

            // 🔴 EXACTLY 7 BINDS — NO LOOP
            ps.setString(1, date);
            ps.setString(2, date);
            ps.setString(3, date);
            ps.setString(4, date);
            ps.setString(5, date);
            ps.setString(6, date);
            ps.setString(7, date);

            ResultSet rs = ps.executeQuery();

            if (!rs.next()) {
                // send ONLY opening stock for new date
                PreparedStatement ps2 = con.prepareStatement(
                    "SELECT " +
                    "NVL(D_H2SO4_CL_STK,0), NVL(D_NAOH_CL_STK,0), " +
                    "NVL(D_BS_CL_STK,0), NVL(D_MS_CL_STK,0), " +
                    "NVL(D_BC_CL_STK,0), NVL(D_BS_BAG_CL_STK,0) " +
                    "FROM D_PROD_PERF_PNP WHERE D_DATE=TO_DATE(?,'DD/MM/YYYY')-1"
                );
                ps2.setString(1, date);
                ResultSet rso = ps2.executeQuery();

                if (rso.next()) {
                    out.print(
                        rso.getString(1) + "?#?0?#?0?#?0?#?" +   // H2SO4
                        rso.getString(2) + "?#?0?#?0?#?0?#?" +   // NAOH
                        rso.getString(3) + "?#?0?#?0?#?0?#?" +   // BS
                        rso.getString(4) + "?#?0?#?0?#?0?#?" +   // MS
                        rso.getString(5) + "?#?0?#?0?#?0?#?" +   // BC
                        rso.getString(6) + "?#?0?#?0?#?0?#?" +   // BS BAG
                        "0?#?"                                  // STEAM (25th)
                    );
                    return;
                }


                out.print("N?#?");
                return;
            }


            StringBuilder sb = new StringBuilder();
            for (int i = 1; i <= 25; i++)
                sb.append(rs.getString(i)).append("?#?");

            out.print(sb.toString());

        } catch (Exception e) {
            e.printStackTrace();
            out.print("N?#?");
        }
    }

    /* ================= SAVE ================= */
    private void saveRecords(HttpServletRequest r) {
    	// 🔐 SAFETY CHECK – PREVENT RELOAD / INVALID SAVE
        if (r.getParameter("reportdate") == null ||
            r.getParameter("reportdate").trim().isEmpty()) {
            return;
        }

        try (Connection c = DBUtil.getConnection()) {

            PreparedStatement chk = c.prepareStatement(
                "SELECT COUNT(*) FROM D_PROD_PERF_PNP WHERE D_DATE=TO_DATE(?,'DD/MM/YYYY')");
            chk.setString(1, r.getParameter("reportdate"));
            ResultSet rs = chk.executeQuery();
            rs.next();

            if (rs.getInt(1) == 0)
                insert(r, c);
            else
                update(r, c);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }




    private void insert(HttpServletRequest r, Connection c) throws Exception {

        PreparedStatement ps = c.prepareStatement(
            "INSERT INTO D_PROD_PERF_PNP(" +
            "D_DATE," +

            "D_H2SO4_RECEIPT,D_H2SO4_CONS,D_H2SO4_CL_STK," +
            "D_AMDEA_RECEIPT,D_AMDEA_CONS,D_AMDEA_CL_STK," +
            "D_NAOH_RECEIPT,D_NAOH_CONS,D_NAOH_CL_STK," +

            "D_BS_PROD,D_BS_DESP,D_BS_CL_STK," +
            "D_MS_RECT,D_MS_CONS,D_MS_CL_STK," +
            "D_BC_RECT,D_BC_CONS,D_BC_CL_STK," +
            "D_BS_BAG_RECT,D_BS_BAG_CONS,D_BS_BAG_CL_STK," +
            "D_BS_STEAM_CONS) " +

            "VALUES (TO_DATE(?,'DD/MM/YYYY'),?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"
        );

        int i = 1;
        ps.setString(i++, r.getParameter("reportdate"));

        ps.setDouble(i++, num(r,"h2so4_rct"));
        ps.setDouble(i++, num(r,"h2so4_cons"));
        ps.setDouble(i++, num(r,"h2so4_cl"));

        ps.setDouble(i++, num(r,"amdea_rct"));
        ps.setDouble(i++, num(r,"amdea_cons"));
        ps.setDouble(i++, num(r,"amdea_cl"));

        ps.setDouble(i++, num(r,"naoh_rct"));
        ps.setDouble(i++, num(r,"naoh_cons"));
        ps.setDouble(i++, num(r,"naoh_cl"));

        ps.setDouble(i++, num(r,"bs_rct"));
        ps.setDouble(i++, num(r,"bs_cons"));
        ps.setDouble(i++, num(r,"bs_cl"));

        ps.setDouble(i++, num(r,"ms_rct"));
        ps.setDouble(i++, num(r,"ms_cons"));
        ps.setDouble(i++, num(r,"ms_cl"));

        ps.setDouble(i++, num(r,"bc_rct"));
        ps.setDouble(i++, num(r,"bc_cons"));
        ps.setDouble(i++, num(r,"bc_cl"));

        ps.setDouble(i++, num(r,"bs_bag_rct"));
        ps.setDouble(i++, num(r,"bs_bag_cons"));
        ps.setDouble(i++, num(r,"bs_bag_cl"));

        ps.setDouble(i++, num(r,"steam"));

        ps.executeUpdate();
    }


    private void update(HttpServletRequest r, Connection c) throws Exception {

        PreparedStatement ps = c.prepareStatement(
            "UPDATE D_PROD_PERF_PNP SET " +

            "D_H2SO4_RECEIPT=?,D_H2SO4_CONS=?,D_H2SO4_CL_STK=?," +
            "D_AMDEA_RECEIPT=?,D_AMDEA_CONS=?,D_AMDEA_CL_STK=?," +
            "D_NAOH_RECEIPT=?,D_NAOH_CONS=?,D_NAOH_CL_STK=?," +

            "D_BS_PROD=?,D_BS_DESP=?,D_BS_CL_STK=?," +
            "D_MS_RECT=?,D_MS_CONS=?,D_MS_CL_STK=?," +
            "D_BC_RECT=?,D_BC_CONS=?,D_BC_CL_STK=?," +
            "D_BS_BAG_RECT=?,D_BS_BAG_CONS=?,D_BS_BAG_CL_STK=?," +
            "D_BS_STEAM_CONS=? " +

            "WHERE D_DATE=TO_DATE(?,'DD/MM/YYYY')"
        );

        int i = 1;

        ps.setDouble(i++, num(r,"h2so4_rct"));
        ps.setDouble(i++, num(r,"h2so4_cons"));
        ps.setDouble(i++, num(r,"h2so4_cl"));

        ps.setDouble(i++, num(r,"amdea_rct"));
        ps.setDouble(i++, num(r,"amdea_cons"));
        ps.setDouble(i++, num(r,"amdea_cl"));

        ps.setDouble(i++, num(r,"naoh_rct"));
        ps.setDouble(i++, num(r,"naoh_cons"));
        ps.setDouble(i++, num(r,"naoh_cl"));

        ps.setDouble(i++, num(r,"bs_rct"));
        ps.setDouble(i++, num(r,"bs_cons"));
        ps.setDouble(i++, num(r,"bs_cl"));

        ps.setDouble(i++, num(r,"ms_rct"));
        ps.setDouble(i++, num(r,"ms_cons"));
        ps.setDouble(i++, num(r,"ms_cl"));

        ps.setDouble(i++, num(r,"bc_rct"));
        ps.setDouble(i++, num(r,"bc_cons"));
        ps.setDouble(i++, num(r,"bc_cl"));

        ps.setDouble(i++, num(r,"bs_bag_rct"));
        ps.setDouble(i++, num(r,"bs_bag_cons"));
        ps.setDouble(i++, num(r,"bs_bag_cl"));

        ps.setDouble(i++, num(r,"steam"));

        ps.setString(i, r.getParameter("reportdate"));

        ps.executeUpdate();
    }


    private double num(HttpServletRequest r, String p) {
        try { return Double.parseDouble(r.getParameter(p)); }
        catch (Exception e) { return 0; }
    }
}
