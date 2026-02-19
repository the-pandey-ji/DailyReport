package com.servlet;

import com.util.DBUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

@WebServlet("/power")
public class PowerServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        if ("details".equals(req.getParameter("action"))) {
            getDetails(req, resp);
            return;
        }

        loadInitial(req);
        req.getRequestDispatcher("/power.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        save(req);
        loadInitial(req);
        req.getRequestDispatcher("/power.jsp").forward(req, resp);
    }

    private void loadInitial(HttpServletRequest req) {

        try (Connection con = DBUtil.getConnection();
             Statement st = con.createStatement()) {

            ResultSet rs = st.executeQuery(
                "SELECT TO_CHAR(MAX(D_DATE)+1,'DD/MM/YYYY') " +
                "FROM PRODUCTION.D_PROD_PERF_PNP " +
                "WHERE D_POWR_IMPORT IS NOT NULL"
            );

            if (rs.next())
                req.setAttribute("report_date", rs.getString(1));

            req.setAttribute("error", "A");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getDetails(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        resp.setContentType("text/plain");
        PrintWriter out = resp.getWriter();

        try (Connection con = DBUtil.getConnection()) {

            PreparedStatement ps = con.prepareStatement(
                "SELECT " +
                "NVL(D_POWR_IMPORT,0), " +
                "NVL(D_CPP_POWR_GEN_TG1,0), " +
                "NVL(D_CPP_POWR_GEN_TG2,0), " +
                "NVL(D_POWR_CONS_UNIT,0), " +
                "NVL(D_POWR_CONS_CPP,0), " +
                "NVL(D_AMM_POWER,0), " +
                "NVL(D_GTGPOWERGEN,0), " +
                "NVL(D_GTGPOWERCONS,0), " +
                "NVL(D_POWER_EXPORT,0) " +
                "FROM PRODUCTION.D_PROD_PERF_PNP " +
                "WHERE D_DATE = TO_DATE(?,'DD/MM/YYYY')"
            );

            ps.setString(1, req.getParameter("reportdate"));
            ResultSet rs = ps.executeQuery();

            if (!rs.next()) {
                out.print("N?#?");
                return;
            }

            for (int i = 1; i <= 9; i++)
                out.print(rs.getString(i) + "?#?");

        } catch (Exception e) {
            e.printStackTrace();
            out.print("N?#?");
        }
    }

    private void save(HttpServletRequest r) {

        if (r.getParameter("reportdate") == null ||
            r.getParameter("reportdate").trim().isEmpty()) {
            return;
        }

        try (Connection con = DBUtil.getConnection()) {

            PreparedStatement chk = con.prepareStatement(
                "SELECT COUNT(*) FROM PRODUCTION.D_PROD_PERF_PNP " +
                "WHERE D_DATE = TO_DATE(?,'DD/MM/YYYY')"
            );
            chk.setString(1, r.getParameter("reportdate"));
            ResultSet rs = chk.executeQuery();
            rs.next();

            if (rs.getInt(1) == 0)
                insert(r, con);
            else
                update(r, con);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void insert(HttpServletRequest r, Connection con) throws Exception {

        PreparedStatement ps = con.prepareStatement(
            "INSERT INTO PRODUCTION.D_PROD_PERF_PNP (" +
            "D_DATE, D_POWR_IMPORT, D_POWR_CONS_UNIT, " +
            "D_CPP_POWR_GEN_TG1, D_CPP_POWR_GEN_TG2, " +
            "D_POWR_CONS_CPP, D_AMM_POWER, " +
            "D_GTGPOWERGEN, D_GTGPOWERCONS, D_POWER_EXPORT" +
            ") VALUES (TO_DATE(?,'DD/MM/YYYY'),?,?,?,?,?,?,?,?,?)"
        );

        int i = 1;
        ps.setString(i++, r.getParameter("reportdate"));
        ps.setDouble(i++, num(r,"powerimported"));
        ps.setDouble(i++, num(r,"powerinunit"));
        ps.setDouble(i++, num(r,"TG1"));
        ps.setDouble(i++, num(r,"TG2"));
        ps.setDouble(i++, num(r,"powerincpp"));
        ps.setDouble(i++, num(r,"ammoniapower"));
        ps.setDouble(i++, num(r,"gtgpowergen"));
        ps.setDouble(i++, num(r,"gtgpowercons"));
        ps.setDouble(i++, num(r,"gtgpowerexp"));

        ps.executeUpdate();
    }

    private void update(HttpServletRequest r, Connection con) throws Exception {

        PreparedStatement ps = con.prepareStatement(
            "UPDATE PRODUCTION.D_PROD_PERF_PNP SET " +
            "D_POWR_IMPORT=?, D_POWR_CONS_UNIT=?, " +
            "D_CPP_POWR_GEN_TG1=?, D_CPP_POWR_GEN_TG2=?, " +
            "D_POWR_CONS_CPP=?, D_AMM_POWER=?, " +
            "D_GTGPOWERGEN=?, D_GTGPOWERCONS=?, D_POWER_EXPORT=? " +
            "WHERE D_DATE = TO_DATE(?,'DD/MM/YYYY')"
        );

        int i = 1;
        ps.setDouble(i++, num(r,"powerimported"));
        ps.setDouble(i++, num(r,"powerinunit"));
        ps.setDouble(i++, num(r,"TG1"));
        ps.setDouble(i++, num(r,"TG2"));
        ps.setDouble(i++, num(r,"powerincpp"));
        ps.setDouble(i++, num(r,"ammoniapower"));
        ps.setDouble(i++, num(r,"gtgpowergen"));
        ps.setDouble(i++, num(r,"gtgpowercons"));
        ps.setDouble(i++, num(r,"gtgpowerexp"));
        ps.setString(i, r.getParameter("reportdate"));

        ps.executeUpdate();
    }

    private double num(HttpServletRequest r, String p) {
        try { return Double.parseDouble(r.getParameter(p)); }
        catch (Exception e) { return 0; }
    }
}
