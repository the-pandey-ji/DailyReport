package com.servlet;

import com.util.DBUtil;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.*;
import java.sql.*;

@WebServlet("/steam")
public class SteamServlet extends HttpServlet {

    protected void doGet(HttpServletRequest r, HttpServletResponse p)
            throws ServletException, IOException {

        if ("details".equals(r.getParameter("action"))) {
            getDetails(r,p);
            return;
        }
        load(r);
        r.getRequestDispatcher("steam.jsp").forward(r,p);
    }

    protected void doPost(HttpServletRequest r, HttpServletResponse p)
            throws ServletException, IOException {

        if("save".equals(r.getParameter("jmethod")))
            save(r);

        load(r);
        r.getRequestDispatcher("steam.jsp").forward(r,p);
    }

    private void load(HttpServletRequest r){
        try(Connection c=DBUtil.getConnection();
            Statement s=c.createStatement()){
            ResultSet rs=s.executeQuery(
              "SELECT TO_CHAR(MAX(D_DATE)+1,'DD/MM/YYYY') "+
              "FROM D_PROD_PERF_PNP WHERE D_STM_PROD_SGP IS NOT NULL");
            if(rs.next()) r.setAttribute("report_date",rs.getString(1));
        }catch(Exception e){e.printStackTrace();}
    }

    private void getDetails(HttpServletRequest r,HttpServletResponse p) throws IOException{
        p.setContentType("text/plain");
        PrintWriter o=p.getWriter();

        try(Connection c=DBUtil.getConnection()){
            PreparedStatement ps=c.prepareStatement(
              "SELECT NVL(D_STM_PROD_SGP,0),NVL(D_STM_INT_CONS_SGP,0),"+
              "NVL(D_STM_EXP_SGP_TO_CPP,0),NVL(D_STM_EXP_SGP_TO_AMM,0),"+
              "NVL(D_SGP_CBD,0),NVL(D_SGP_STM_VENT,0),NVL(D_SGP_FW_DESHEAT,0),"+
              "NVL(D_SGP_AVG_BFW_TEMP,0),NVL(D_COAL_CONS_SGP,0),NVL(D_FUEL_NG_CONS_SGP,0),"+
              "NVL(D_SGP_BLR1_RUNHRS,0),NVL(D_SGP_BLR2_RUNHRS,0),NVL(D_SGP_BLR3_RUNHRS,0),"+
              "NVL(D_FLY_ASH,0) FROM D_PROD_PERF_PNP WHERE D_DATE=TO_DATE(?,'DD/MM/YYYY')");
            ps.setString(1,r.getParameter("report_date"));
            ResultSet rs=ps.executeQuery();
            if(!rs.next()){o.print("N?#?");return;}
            for(int i=1;i<=14;i++) o.print(rs.getString(i)+"?#?");
        }catch(Exception e){e.printStackTrace();o.print("N?#?");}
    }

    private void save(HttpServletRequest r) {

        String sql =
            "MERGE INTO D_PROD_PERF_PNP t " +
            "USING (SELECT ? AS D_DATE FROM dual) s " +
            "ON (t.D_DATE = s.D_DATE) " +
            "WHEN MATCHED THEN UPDATE SET " +
            "  D_STM_PROD_SGP=?, D_STM_INT_CONS_SGP=?, D_STM_EXP_SGP_TO_CPP=?, " +
            "  D_STM_EXP_SGP_TO_AMM=?, D_SGP_CBD=?, D_SGP_STM_VENT=?, " +
            "  D_SGP_FW_DESHEAT=?, D_SGP_AVG_BFW_TEMP=?, " +
            "  D_COAL_CONS_SGP=?, D_FUEL_NG_CONS_SGP=?, " +
            "  D_SGP_BLR1_RUNHRS=?, D_SGP_BLR2_RUNHRS=?, " +
            "  D_SGP_BLR3_RUNHRS=?, D_FLY_ASH=? " +
            "WHEN NOT MATCHED THEN INSERT ( " +
            "  D_DATE, D_STM_PROD_SGP, D_STM_INT_CONS_SGP, D_STM_EXP_SGP_TO_CPP, " +
            "  D_STM_EXP_SGP_TO_AMM, D_SGP_CBD, D_SGP_STM_VENT, " +
            "  D_SGP_FW_DESHEAT, D_SGP_AVG_BFW_TEMP, " +
            "  D_COAL_CONS_SGP, D_FUEL_NG_CONS_SGP, " +
            "  D_SGP_BLR1_RUNHRS, D_SGP_BLR2_RUNHRS, " +
            "  D_SGP_BLR3_RUNHRS, D_FLY_ASH " +
            ") VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            java.sql.Date date = new java.sql.Date(
                new java.text.SimpleDateFormat("dd/MM/yyyy")
                    .parse(r.getParameter("report_date")).getTime()
            );

            String[] f = {
                "steam_production","steam_int_consumption","steam_export_to_cpp",
                "steam_export_to_amm","continuous_blowdown","steam_venting",
                "fwd_for_desuper_heating","avrg_bfw_temperature",
                "coal_consumption","fuel_cons_ng",
                "boiler_1","boiler_2","boiler_3","fly_ash_despatch"
            };

            int i = 1;

            // 1️⃣ MERGE source date
            ps.setDate(i++, date);

            // 2️⃣ UPDATE values (14)
            for (String x : f) {
                ps.setDouble(i++, parseDoubleSafe(r.getParameter(x)));
            }

            // 3️⃣ INSERT values (15)
            ps.setDate(i++, date);   // insert date
            for (String x : f) {
                ps.setDouble(i++, parseDoubleSafe(r.getParameter(x)));
            }

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private double parseDoubleSafe(String v) {
        try {
            return (v == null || v.trim().isEmpty()) ? 0.0 : Double.parseDouble(v);
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }
}
