package com.servlet;

import com.util.DBUtil;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.*;
import java.sql.*;

@WebServlet({"/cpp","/cppDetails"})
public class CaptivePowerServlet extends HttpServlet {

    /* ===== LOAD ===== */
    protected void doGet(HttpServletRequest r, HttpServletResponse p)
            throws ServletException, IOException {

        if (r.getServletPath().contains("Details")) {
            getDetails(r, p);
            return;
        }

        try (Connection c = DBUtil.getConnection();
             Statement s = c.createStatement()) {

            ResultSet rs = s.executeQuery(
              "SELECT TO_CHAR(MAX(D_DATE)+1,'DD/MM/YYYY') " +
              "FROM D_PROD_PERF_PNP WHERE D_STM_PROD_CPP IS NOT NULL");

            if (rs.next())
                r.setAttribute("report_date", rs.getString(1));

        } catch (Exception e) { e.printStackTrace(); }

        r.getRequestDispatcher("/captivePower.jsp").forward(r, p);
    }

    /* ===== SAVE ===== */
    protected void doPost(HttpServletRequest r, HttpServletResponse p)
            throws ServletException, IOException {

        try (Connection c = DBUtil.getConnection()) {

            PreparedStatement ps = c.prepareStatement(
              "MERGE INTO D_PROD_PERF_PNP d USING dual ON " +
              "(d.D_DATE=TO_DATE(?,'DD/MM/YYYY')) " +
              "WHEN MATCHED THEN UPDATE SET " +
              "D_STM_PROD_CPP=?,D_CPP_STM_CONS_BLR=?,D_STM_CONS_TG1_CPP=?,D_STM_CONS_TG2_CPP=?," +
              "D_STM_EXP_CPP_TO_AMM=?,D_CPP_STM_VENT=?,D_CPP_CBD=?,D_CPP_FW_DESHEAT=?," +
              "D_BFW_EXP_CPP_TO_SGP=?,D_RETURN_COND_CPP=?,D_CPP_AVG_BFW_TEMP=?," +
              "D_COAL_CONS_CPP=?,D_FUEL_NG_CONS_CPP=?,D_CPP_BLR1_RUNHRS=?,D_CPP_TG1_RUNHRS=?,D_CPP_TG2_RUNHRS=? " +
              "WHEN NOT MATCHED THEN INSERT VALUES " +
              "(TO_DATE(?,'DD/MM/YYYY'),?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"
            );

            int i=1;
            ps.setString(i++, r.getParameter("report_date"));
            for(String f:new String[]{
              "steam_production","steam_cons_in_cpp","steam_consumed_tg1","steam_consumed_tg2",
              "steam_export_to_amm","steam_venting","continuous_blowdown","fwd_for_desuper_heating",
              "bfw_export_to_sgp","return_condensate","avrg_bfw_temperature","coal_consumption",
              "fuel_cons_ng","boiler","tg_1","tg_2"}) {
                ps.setDouble(i++, num(r,f));
            }
            ps.setString(i++, r.getParameter("report_date"));
            for(String f:new String[]{
              "steam_production","steam_cons_in_cpp","steam_consumed_tg1","steam_consumed_tg2",
              "steam_export_to_amm","steam_venting","continuous_blowdown","fwd_for_desuper_heating",
              "bfw_export_to_sgp","return_condensate","avrg_bfw_temperature","coal_consumption",
              "fuel_cons_ng","boiler","tg_1","tg_2"}) {
                ps.setDouble(i++, num(r,f));
            }

            ps.executeUpdate();

        } catch (Exception e) { e.printStackTrace(); }

        doGet(r,p);
    }

    /* ===== AJAX ===== */
    private void getDetails(HttpServletRequest r, HttpServletResponse p)
            throws IOException {

        p.setContentType("text/plain");
        PrintWriter out = p.getWriter();

        try (Connection c = DBUtil.getConnection()) {

            PreparedStatement ps = c.prepareStatement(
              "SELECT NVL(D_STM_PROD_CPP,0),NVL(D_CPP_STM_CONS_BLR,0)," +
              "NVL(D_STM_CONS_TG1_CPP,0),NVL(D_STM_CONS_TG2_CPP,0)," +
              "NVL(D_STM_EXP_CPP_TO_AMM,0),NVL(D_CPP_STM_VENT,0),NVL(D_CPP_CBD,0)," +
              "NVL(D_CPP_FW_DESHEAT,0),NVL(D_BFW_EXP_CPP_TO_SGP,0),NVL(D_RETURN_COND_CPP,0)," +
              "NVL(D_CPP_AVG_BFW_TEMP,0),NVL(D_COAL_CONS_CPP,0),NVL(D_FUEL_NG_CONS_CPP,0)," +
              "NVL(D_CPP_BLR1_RUNHRS,0),NVL(D_CPP_TG1_RUNHRS,0),NVL(D_CPP_TG2_RUNHRS,0) " +
              "FROM D_PROD_PERF_PNP WHERE D_DATE=TO_DATE(?,'DD/MM/YYYY')"
            );

            ps.setString(1, r.getParameter("report_date"));
            ResultSet rs = ps.executeQuery();

            if (!rs.next()) { out.print("N?#?"); return; }

            for(int i=1;i<=16;i++)
                out.print(rs.getString(i)+"?#?");

        } catch (Exception e) { out.print("N?#?"); }
    }

    private double num(HttpServletRequest r,String p){
        try{ return Double.parseDouble(r.getParameter(p)); }
        catch(Exception e){ return 0; }
    }
}
