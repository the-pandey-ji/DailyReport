package com.servlet;

import com.util.DBUtil;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.*;
import java.sql.*;

@WebServlet("/materialHandling")
public class MaterialHandlingServlet extends HttpServlet {

    /* ================= LOAD PAGE ================= */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        if ("details".equals(req.getParameter("action"))) {
            getDetails(req, resp);
            return;
        }

        loadInitial(req);
        req.getRequestDispatcher("/materialHandling.jsp").forward(req, resp);
    }

    /* ================= SAVE ================= */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        save(req);               // ✅ save only on submit
        loadInitial(req);
        req.getRequestDispatcher("/materialHandling.jsp").forward(req, resp);
    }

    /* ================= INITIAL LOAD ================= */
    private void loadInitial(HttpServletRequest req) {

        try (Connection con = DBUtil.getConnection();
             Statement st = con.createStatement()) {

            ResultSet rs = st.executeQuery(
                "SELECT TO_CHAR(MAX(D_DATE)+1,'DD/MM/YYYY') " +
                "FROM D_PROD_PERF_PNP WHERE D_LDOHSD_CL_STK IS NOT NULL");

            if (rs.next())
                req.setAttribute("reportdate", rs.getString(1));

            rs = st.executeQuery(
                "SELECT NVL(D_LDOHSD_CL_STK,0), NVL(D_COAL_CL_STK,0) " +
                "FROM D_PROD_PERF_PNP WHERE D_DATE=(" +
                "SELECT MAX(D_DATE) FROM D_PROD_PERF_PNP WHERE D_LDOHSD_CL_STK IS NOT NULL)");

            if (rs.next()) {
                req.setAttribute("ldohsd_op", rs.getDouble(1));
                req.setAttribute("coal_op", rs.getDouble(2));
            }

            req.setAttribute("error", "A");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* ================= AJAX DETAILS ================= */
    private void getDetails(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        resp.setContentType("text/plain");
        PrintWriter out = resp.getWriter();
        String d = req.getParameter("reportdate");

        try (Connection con = DBUtil.getConnection()) {

            PreparedStatement ps = con.prepareStatement(
                "SELECT " +
                "(SELECT NVL(D_LDOHSD_CL_STK,0) FROM D_PROD_PERF_PNP WHERE D_DATE=TO_DATE(?,'DD/MM/YYYY')-1)," +
                "NVL(D_LDOHSD_RECEIPT,0), NVL(D_LDOHSD_TRANSFER,0), NVL(D_LDOHSD_CL_STK,0)," +
                "(SELECT NVL(D_COAL_CL_STK,0) FROM D_PROD_PERF_PNP WHERE D_DATE=TO_DATE(?,'DD/MM/YYYY')-1)," +
                "NVL(D_COAL_RECEIPT,0), NVL(D_COAL_TRANSFER,0), NVL(D_COAL_TRANSFER_OUNIT,0), NVL(D_COAL_CL_STK,0) " +
                "FROM D_PROD_PERF_PNP WHERE D_DATE=TO_DATE(?,'DD/MM/YYYY')"
            );

            ps.setString(1, d);
            ps.setString(2, d);
            ps.setString(3, d);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                for (int i = 1; i <= 9; i++)
                    out.print(rs.getString(i) + "?#?");
            } else {
                out.print("N?#?");
            }

        } catch (Exception e) {
            e.printStackTrace();
            out.print("N?#?");
        }
    }

    /* ================= SAVE ================= */
    private void save(HttpServletRequest r) {

        // ✅ STOP SAVE ON PAGE LOAD / RELOAD
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

            if (rs.getInt(1) == 0) {
                insert(r, c);   // ✅ NEW DATE
            } else {
                update(r, c);   // ✅ EXISTING DATE
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* ================= INSERT ================= */
    private void insert(HttpServletRequest r, Connection c) throws Exception {

        PreparedStatement ps = c.prepareStatement(
            "INSERT INTO D_PROD_PERF_PNP (" +
            "D_DATE, D_LDOHSD_RECEIPT, D_LDOHSD_TRANSFER, D_LDOHSD_CL_STK, " +
            "D_COAL_RECEIPT, D_COAL_TRANSFER, D_COAL_TRANSFER_OUNIT, D_COAL_CL_STK) " +
            "VALUES (TO_DATE(?,'DD/MM/YYYY'),?,?,?,?,?,?,?)"
        );

        int i = 1;
        ps.setString(i++, r.getParameter("reportdate"));
        ps.setDouble(i++, num(r, "ldohsd_rct"));
        ps.setDouble(i++, num(r, "ldohsd_tfr"));
        ps.setDouble(i++, num(r, "ldohsd_cl_stk"));
        ps.setDouble(i++, num(r, "coal_rct"));
        ps.setDouble(i++, num(r, "coal_tfr"));
        ps.setDouble(i++, num(r, "coal_tfr_ounit"));
        ps.setDouble(i++, num(r, "coal_cl_stk"));

        ps.executeUpdate();
    }

    /* ================= UPDATE ================= */
    private void update(HttpServletRequest r, Connection c) throws Exception {

        PreparedStatement ps = c.prepareStatement(
            "UPDATE D_PROD_PERF_PNP SET " +
            "D_LDOHSD_RECEIPT=?, D_LDOHSD_TRANSFER=?, D_LDOHSD_CL_STK=?, " +
            "D_COAL_RECEIPT=?, D_COAL_TRANSFER=?, D_COAL_TRANSFER_OUNIT=?, D_COAL_CL_STK=? " +
            "WHERE D_DATE=TO_DATE(?,'DD/MM/YYYY')"
        );

        int i = 1;
        ps.setDouble(i++, num(r, "ldohsd_rct"));
        ps.setDouble(i++, num(r, "ldohsd_tfr"));
        ps.setDouble(i++, num(r, "ldohsd_cl_stk"));
        ps.setDouble(i++, num(r, "coal_rct"));
        ps.setDouble(i++, num(r, "coal_tfr"));
        ps.setDouble(i++, num(r, "coal_tfr_ounit"));
        ps.setDouble(i++, num(r, "coal_cl_stk"));
        ps.setString(i, r.getParameter("reportdate"));

        ps.executeUpdate();
    }

    /* ================= UTIL ================= */
    private double num(HttpServletRequest r, String p) {
        try {
            return Double.parseDouble(r.getParameter(p));
        } catch (Exception e) {
            return 0;
        }
    }
}
