package com.servlet;

import com.util.DBUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

@WebServlet("/gtg")
public class GTGServlet extends HttpServlet {

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
        req.getRequestDispatcher("/gtg.jsp").forward(req, resp);
    }

    /* ================= SAVE ================= */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        saveRecords(req);
        loadInitialData(req);
        req.getRequestDispatcher("/gtg.jsp").forward(req, resp);
    }

    /* ================= INITIAL LOAD ================= */
    private void loadInitialData(HttpServletRequest req) {
        try (Connection con = DBUtil.getConnection();
             Statement st = con.createStatement()) {

            ResultSet rs = st.executeQuery(
                "SELECT TO_CHAR(MAX(D_DATE)+1,'DD/MM/YYYY') " +
                "FROM D_PROD_PERF_PNP WHERE D_NG_CONSM_GTG IS NOT NULL"
            );

            if (rs.next())
                req.setAttribute("report_date", rs.getString(1));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* ================= AJAX DETAILS ================= */
    private void getDetails(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        resp.setContentType("text/plain");
        PrintWriter out = resp.getWriter();
        String date = req.getParameter("report_date");

        try (Connection con = DBUtil.getConnection()) {

            PreparedStatement ps = con.prepareStatement(
                "SELECT " +
                "NVL(D_NG_CONSM_GTG,0), " +          // 1
                "NVL(D_NG_CONSM_HRSG,0), " +         // 2
                "NVL(D_NG_CONSM_GTG_HRSG,0), " +     // 3
                "NVL(D_BFW_IMP_HRSG,0), " +          // 4
                "NVL(D_STM_EXP_100K_AMM,0), " +      // 5
                "NVL(D_STM_EXP_35,0), " +            // 6
                "NVL(D_STM_EXP_75,0), " +            // 7
                "NVL(D_GTG_RUNNING_HRS,0), " +       // 8
                "NVL(D_HRSG_RUNNING_HRS,0) " +       // 9
                "FROM D_PROD_PERF_PNP " +
                "WHERE D_DATE = TO_DATE(?,'DD/MM/YYYY')"
            );

            ps.setString(1, date);
            ResultSet rs = ps.executeQuery();

            if (!rs.next()) {
                out.print("N?#?");
                return;
            }

            StringBuilder sb = new StringBuilder();
            for (int i = 1; i <= 9; i++)
                sb.append(rs.getString(i)).append("?#?");

            out.print(sb.toString());

        } catch (Exception e) {
            e.printStackTrace();
            out.print("N?#?");
        }
    }

    private void saveRecords(HttpServletRequest r) {

        // ✅ STOP SAVE ON PAGE LOAD / RELOAD
        if (r.getParameter("report_date") == null ||
            r.getParameter("report_date").trim().isEmpty()) {
            return;
        }

        try (Connection c = DBUtil.getConnection()) {

            PreparedStatement chk = c.prepareStatement(
                "SELECT COUNT(*) FROM D_PROD_PERF_PNP WHERE D_DATE=TO_DATE(?,'DD/MM/YYYY')");
            chk.setString(1, r.getParameter("report_date"));
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


    /* ================= INSERT ================= */
    private void insert(HttpServletRequest r, Connection c) throws Exception {

        PreparedStatement ps = c.prepareStatement(
            "INSERT INTO D_PROD_PERF_PNP(" +
            "D_DATE, D_NG_CONSM_GTG, D_NG_CONSM_HRSG, D_NG_CONSM_GTG_HRSG, " +
            "D_BFW_IMP_HRSG, D_STM_EXP_100K_AMM, D_STM_EXP_35, D_STM_EXP_75, " +
            "D_GTG_RUNNING_HRS, D_HRSG_RUNNING_HRS) " +
            "VALUES(TO_DATE(?,'DD/MM/YYYY'),?,?,?,?,?,?,?,?,?)"
        );

        int i = 1;
        ps.setString(i++, r.getParameter("report_date"));
        ps.setDouble(i++, num(r,"d_ng_consm_gtg"));
        ps.setDouble(i++, num(r,"d_ng_consm_hrsg"));
        ps.setDouble(i++, num(r,"d_ng_consm_gtg_hrsg"));
        ps.setDouble(i++, num(r,"d_bfw_imp_hrsg"));
        ps.setDouble(i++, num(r,"d_stm_exp_100k_amm"));
        ps.setDouble(i++, num(r,"d_stm_exp_35"));
        ps.setDouble(i++, num(r,"d_stm_exp_75"));
        ps.setDouble(i++, num(r,"d_gtg_running_hrs"));
        ps.setDouble(i++, num(r,"d_hrsg_running_hrs"));

        ps.executeUpdate();
    }

    /* ================= UPDATE ================= */
    private void update(HttpServletRequest r, Connection c) throws Exception {

        PreparedStatement ps = c.prepareStatement(
            "UPDATE D_PROD_PERF_PNP SET " +
            "D_NG_CONSM_GTG=?, D_NG_CONSM_HRSG=?, D_NG_CONSM_GTG_HRSG=?, " +
            "D_BFW_IMP_HRSG=?, D_STM_EXP_100K_AMM=?, D_STM_EXP_35=?, " +
            "D_STM_EXP_75=?, D_GTG_RUNNING_HRS=?, D_HRSG_RUNNING_HRS=? " +
            "WHERE D_DATE = TO_DATE(?,'DD/MM/YYYY')"
        );

        int i = 1;
        ps.setDouble(i++, num(r,"d_ng_consm_gtg"));
        ps.setDouble(i++, num(r,"d_ng_consm_hrsg"));
        ps.setDouble(i++, num(r,"d_ng_consm_gtg_hrsg"));
        ps.setDouble(i++, num(r,"d_bfw_imp_hrsg"));
        ps.setDouble(i++, num(r,"d_stm_exp_100k_amm"));
        ps.setDouble(i++, num(r,"d_stm_exp_35"));
        ps.setDouble(i++, num(r,"d_stm_exp_75"));
        ps.setDouble(i++, num(r,"d_gtg_running_hrs"));
        ps.setDouble(i++, num(r,"d_hrsg_running_hrs"));
        ps.setString(i, r.getParameter("report_date"));

        ps.executeUpdate();
    }

    private double num(HttpServletRequest r, String p) {
        try { return Double.parseDouble(r.getParameter(p)); }
        catch (Exception e) { return 0; }
    }
}
