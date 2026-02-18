package com.servlet;

import com.util.DBUtil;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.*;
import java.sql.*;

@WebServlet("/ammonia")
public class AmmoniaServlet extends HttpServlet {

    protected void doGet(HttpServletRequest r, HttpServletResponse p)
            throws ServletException, IOException {

        if("details".equals(r.getParameter("action"))){
            getDetails(r,p);
            return;
        }
        loadPage(r);
        r.getRequestDispatcher("/ammonia.jsp").forward(r,p);
    }

    protected void doPost(HttpServletRequest r, HttpServletResponse p)
            throws ServletException, IOException {

        if("saveRecords".equals(r.getParameter("saverecord")))
            save(r);

        loadPage(r);
        r.getRequestDispatcher("/ammonia.jsp").forward(r,p);
    }

    private void loadPage(HttpServletRequest r){
        try(Connection c=DBUtil.getConnection(); Statement s=c.createStatement()){
            ResultSet rs=s.executeQuery(
              "SELECT TO_CHAR(MAX(D_DATE)+1,'DD/MM/YYYY') FROM D_PROD_PERF_PNP WHERE D_AMM_CL_STK IS NOT NULL");
            if(rs.next()) r.setAttribute("reportdate",rs.getString(1));

            rs=s.executeQuery(
              "SELECT NVL(D_AMM_CL_STK,0) FROM D_PROD_PERF_PNP WHERE D_DATE=(SELECT MAX(D_DATE) FROM D_PROD_PERF_PNP)");
            if(rs.next()) r.setAttribute("prevstock",rs.getDouble(1));

            rs=s.executeQuery(
              "SELECT NVL(SUM(D_AMM_PROD),0) FROM D_PROD_PERF_PNP");
            if(rs.next()) r.setAttribute("totalammonia",rs.getDouble(1));
        }catch(Exception e){e.printStackTrace();}
    }

    private void getDetails(HttpServletRequest r, HttpServletResponse p) throws IOException {

        p.setContentType("text/plain");
        PrintWriter out = p.getWriter();
        String reportDate = r.getParameter("reportdate");

        try (Connection c = DBUtil.getConnection();
             Statement st = c.createStatement()) {

            /* ---------- FINANCIAL YEAR TOTAL ---------- */
            ResultSet rs = st.executeQuery(
                "select decode(to_char(to_date('"+reportDate+"','dd/mm/yyyy'),'mm')," +
                "'01',to_number(to_char(to_date('"+reportDate+"','dd/mm/yyyy'),'yyyy')-1)," +
                "'02',to_number(to_char(to_date('"+reportDate+"','dd/mm/yyyy'),'yyyy')-1)," +
                "'03',to_number(to_char(to_date('"+reportDate+"','dd/mm/yyyy'),'yyyy')-1)," +
                "to_number(to_char(to_date('"+reportDate+"','dd/mm/yyyy'),'yyyy'))) from dual"
            );
            rs.next();
            int yyyy = rs.getInt(1);

            rs = st.executeQuery(
                "select nvl(sum(D_AMM_PROD),0) from D_PROD_PERF_PNP " +
                "where D_DATE >= '01-APR-"+yyyy+"' and D_DATE <= to_date('"+reportDate+"','dd/mm/yyyy')"
            );
            rs.next();
            String totalAmmonia = rs.getString(1);

            /* ---------- MAIN DATA ---------- */
            rs = st.executeQuery(
                "select nvl(D_AMM_PROD,0), nvl(AMM_IMPORT,0), nvl(D_AMM_TO_UREA,0), " +
                "nvl(D_AMM_INT_CONS,0), nvl(D_AMM_TO_OTHER_UNITS,0), nvl(D_AMM_CL_STK,0), " +
                "(select nvl(D_AMM_CL_STK,0) from D_PROD_PERF_PNP where D_DATE=to_date('"+reportDate+"','dd/mm/yyyy')-1), " +
                "nvl(D_AMM_STRM_HRS,0), nvl(D_AMM_NG_CONS,0), nvl(D_BFW_EXP_AMM_TO_SGP,0), " +
                "nvl(D_AMDEA_CONS,0), nvl(D_BFW_EXP_AMM_TO_CPP,0), nvl(D_WHB_STM_AMM_TO_100K,0), " +
                "nvl(D_AMM_AVG_BFW_TEMP,0), nvl(D_CV_NG,0), nvl(D_LS_RMTLS_AMM,0), " +
                "nvl(D_LS_POWER_AMM,0), nvl(D_LS_MECH_AMM,0), nvl(D_LS_ELEC_AMM,0), " +
                "nvl(D_LS_INST_AMM,0), nvl(D_LS_PROC_AMM,0), nvl(D_LS_SD_AMM,0), " +
                "nvl(D_LS_OTHER_AMM,0), nvl(D_NG_RECEIPT,0), nvl(D_LS_AN_SD_AMM,0), " +
                "'"+totalAmmonia+"', nvl(D_NG_CONS_GTG,0) " +
                "from D_PROD_PERF_PNP where D_DATE=to_date('"+reportDate+"','dd/mm/yyyy')"
            );

            if (rs.next()) {
                for (int i = 1; i <= 27; i++)
                    out.print(rs.getString(i) + "?#?");
                return;
            }

            /* ---------- NO DATA → SEND OPENING STOCK ONLY ---------- */
            rs = st.executeQuery(
                "select nvl(D_AMM_CL_STK,0) from D_PROD_PERF_PNP " +
                "where D_DATE = to_date('"+reportDate+"','dd/mm/yyyy')-1"
            );

            if (rs.next()) {
            	out.print(
            		    "?#?" + // 1 ammonia_production
            		    "?#?" + // 2 ammonia_import
            		    "?#?" + // 3 ammonia_to_urea
            		    "?#?" + // 4 ammonia_internal_consumption
            		    "?#?" + // 5 ammonia_export_to_units
            		    "?#?" + // 6 ammonia_closing_stock
            		    rs.getString(1) + "?#?" + // 7 previous_day_ammonia_stock

            		    "?#?" + // 8 ammonia_stream_hours
            		    "?#?" + // 9 feed_consumption_ng
            		    "?#?" + //10 bfw_export_to_sgp
            		    "?#?" + //11 amdea_consumption
            		    "?#?" + //12 bfw_export_to_cpp
            		    "?#?" + //13 whb_steam_to_100k_hdr
            		    "?#?" + //14 average_bfw_temperature
            		    "?#?" + //15 cv_ng

            		    "?#?" + //16 rawmaterials
            		    "?#?" + //17 exportpower
            		    "?#?" + //18 mechanical
            		    "?#?" + //19 electrical
            		    "?#?" + //20 instrumentation
            		    "?#?" + //21 process1
            		    "?#?" + //22 shutdown
            		    "?#?" + //23 others

            		    "?#?" + //24 totalng
            		    "?#?" + //25 annualshutdown
            		    totalAmmonia + "?#?" + //26 totalammoniaproduce
            		    "?#?"   //27 ngconsgtg
            		);

                return;
            }

            out.print("N?#?");

        } catch (Exception e) {
            e.printStackTrace();
            out.print("N?#?");
        }
    }


    private void save(HttpServletRequest r) {

        try (Connection c = DBUtil.getConnection()) {

            PreparedStatement ps = c.prepareStatement(
                "MERGE INTO D_PROD_PERF_PNP d USING dual " +
                "ON (d.D_DATE = TO_DATE(?,'DD/MM/YYYY')) " +

                "WHEN MATCHED THEN UPDATE SET " +
                "D_AMM_PROD=?, AMM_IMPORT=?, D_AMM_TO_UREA=?, D_AMM_INT_CONS=?, " +
                "D_AMM_TO_OTHER_UNITS=?, D_AMM_CL_STK=?, " +
                "D_AMM_STRM_HRS=?, D_AMM_NG_CONS=?, D_BFW_EXP_AMM_TO_SGP=?, " +
                "D_AMDEA_CONS=?, D_BFW_EXP_AMM_TO_CPP=?, D_WHB_STM_AMM_TO_100K=?, " +
                "D_AMM_AVG_BFW_TEMP=?, D_CV_NG=?, " +
                "D_LS_RMTLS_AMM=?, D_LS_POWER_AMM=?, D_LS_MECH_AMM=?, D_LS_ELEC_AMM=?, " +
                "D_LS_INST_AMM=?, D_LS_PROC_AMM=?, D_LS_SD_AMM=?, D_LS_OTHER_AMM=?, " +
                "D_NG_RECEIPT=?, D_LS_AN_SD_AMM=?, D_NG_CONS_GTG=? " +

                "WHEN NOT MATCHED THEN INSERT ( " +
                "D_DATE, D_AMM_PROD, AMM_IMPORT, D_AMM_TO_UREA, D_AMM_INT_CONS, " +
                "D_AMM_TO_OTHER_UNITS, D_AMM_CL_STK, D_AMM_STRM_HRS, D_AMM_NG_CONS, " +
                "D_BFW_EXP_AMM_TO_SGP, D_AMDEA_CONS, D_BFW_EXP_AMM_TO_CPP, " +
                "D_WHB_STM_AMM_TO_100K, D_AMM_AVG_BFW_TEMP, D_CV_NG, " +
                "D_LS_RMTLS_AMM, D_LS_POWER_AMM, D_LS_MECH_AMM, D_LS_ELEC_AMM, " +
                "D_LS_INST_AMM, D_LS_PROC_AMM, D_LS_SD_AMM, D_LS_OTHER_AMM, " +
                "D_NG_RECEIPT, D_LS_AN_SD_AMM, D_NG_CONS_GTG ) " +

                "VALUES (TO_DATE(?,'DD/MM/YYYY'),?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"
            );

            int i = 1;

            /* ---------- MERGE KEY ---------- */
            ps.setString(i++, r.getParameter("reportdate"));

            /* ---------- UPDATE VALUES ---------- */
            ps.setDouble(i++, n(r,"ammonia_production"));
            ps.setDouble(i++, n(r,"ammonia_import"));
            ps.setDouble(i++, n(r,"ammonia_to_urea"));
            ps.setDouble(i++, n(r,"ammonia_internal_consumption"));
            ps.setDouble(i++, n(r,"ammonia_export_to_units"));
            ps.setDouble(i++, n(r,"ammonia_closing_stock"));

            ps.setDouble(i++, n(r,"ammonia_stream_hours"));
            ps.setDouble(i++, n(r,"feed_consumption_ng"));
            ps.setDouble(i++, n(r,"bfw_export_to_sgp"));
            ps.setDouble(i++, n(r,"amdea_consumption"));
            ps.setDouble(i++, n(r,"bfw_export_to_cpp"));
            ps.setDouble(i++, n(r,"whb_steam_to_100k_hdr"));
            ps.setDouble(i++, n(r,"average_bfw_temperature"));
            ps.setDouble(i++, n(r,"cv_ng"));

            ps.setDouble(i++, n(r,"rawmaterials"));
            ps.setDouble(i++, n(r,"exportpower"));
            ps.setDouble(i++, n(r,"mechanical"));
            ps.setDouble(i++, n(r,"electrical"));
            ps.setDouble(i++, n(r,"instrumentation"));
            ps.setDouble(i++, n(r,"process1"));
            ps.setDouble(i++, n(r,"shutdown"));
            ps.setDouble(i++, n(r,"others"));

            ps.setDouble(i++, n(r,"totalng"));
            ps.setDouble(i++, n(r,"annualshutdown"));
            ps.setDouble(i++, n(r,"ngconsgtg"));

            /* ---------- INSERT DATE AGAIN ---------- */
            ps.setString(i++, r.getParameter("reportdate"));

            /* ---------- INSERT VALUES (same order) ---------- */
            ps.setDouble(i++, n(r,"ammonia_production"));
            ps.setDouble(i++, n(r,"ammonia_import"));
            ps.setDouble(i++, n(r,"ammonia_to_urea"));
            ps.setDouble(i++, n(r,"ammonia_internal_consumption"));
            ps.setDouble(i++, n(r,"ammonia_export_to_units"));
            ps.setDouble(i++, n(r,"ammonia_closing_stock"));

            ps.setDouble(i++, n(r,"ammonia_stream_hours"));
            ps.setDouble(i++, n(r,"feed_consumption_ng"));
            ps.setDouble(i++, n(r,"bfw_export_to_sgp"));
            ps.setDouble(i++, n(r,"amdea_consumption"));
            ps.setDouble(i++, n(r,"bfw_export_to_cpp"));
            ps.setDouble(i++, n(r,"whb_steam_to_100k_hdr"));
            ps.setDouble(i++, n(r,"average_bfw_temperature"));
            ps.setDouble(i++, n(r,"cv_ng"));

            ps.setDouble(i++, n(r,"rawmaterials"));
            ps.setDouble(i++, n(r,"exportpower"));
            ps.setDouble(i++, n(r,"mechanical"));
            ps.setDouble(i++, n(r,"electrical"));
            ps.setDouble(i++, n(r,"instrumentation"));
            ps.setDouble(i++, n(r,"process1"));
            ps.setDouble(i++, n(r,"shutdown"));
            ps.setDouble(i++, n(r,"others"));

            ps.setDouble(i++, n(r,"totalng"));
            ps.setDouble(i++, n(r,"annualshutdown"));
            ps.setDouble(i++, n(r,"ngconsgtg"));

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private double n(HttpServletRequest r,String p){
        try{return Double.parseDouble(r.getParameter(p));}
        catch(Exception e){return 0;}
    }
}
