package com.nfl.dailyreport.dao;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class UreaDAO {

    public Map<String, String> loadInitialData(Connection con, String userId) throws Exception {

        Map<String, String> data = new HashMap<>();
        Statement st = con.createStatement();
        ResultSet rs;
        // 🔹 Authorization
		/*
		 * rs = st.executeQuery(
		 * "select nvl(D_R_FLAG,'N') from usermaster where userid='" + userId + "'" );
		 * if (rs.next()) { data.put("error", rs.getString(1)); }
		 */

        // 🔹 Next report date
        rs = st.executeQuery(
            "select to_char(max(D_DATE)+1,'dd/mm/yyyy') " +
            "from D_PROD_PERF_PNP where D_UREA_PROD is not null"
        );
        if (rs.next()) {
            data.put("reportdate", rs.getString(1));
        }

        // 🔹 Financial year calculation
        rs = st.executeQuery(
            "select decode(to_char(max(D_DATE)+1,'mm')," +
            "'01',to_number(to_char(max(D_DATE)+1,'yyyy')-1)," +
            "'02',to_number(to_char(max(D_DATE)+1,'yyyy')-1)," +
            "'03',to_number(to_char(max(D_DATE)+1,'yyyy')-1)," +
            "to_number(to_char(max(D_DATE)+1,'yyyy'))) yyyy " +
            "from D_PROD_PERF_PNP where D_UREA_PROD is not null"
        );
        rs.next();
        int yyyy = rs.getInt(1);

        // 🔹 Total urea produced
        rs = st.executeQuery(
            "select nvl(sum(D_UREA_PROD),0) from D_PROD_PERF_PNP " +
            "where D_DATE >= '01-APR-" + yyyy + "' and D_DATE <= '31-MAR-" + (yyyy+1) + "'"
        );
        rs.next();
        data.put("totalureaproduce", rs.getString(1));

        // 🔹 Opening stocks (previous day)
        rs = st.executeQuery(
            "select D_UREA_CL_STK, D_HDPE_BAG_CL_STK, " +
            "nvl(D_NEEM_OIL_STK,0), nvl(D_NEEM_BAG_CL_STK,0) " +
            "from D_PROD_PERF_PNP " +
            "where D_DATE = (select max(D_DATE) from D_PROD_PERF_PNP)"
        );
        if (rs.next()) {
            data.put("ureaopeningstock", rs.getString(1));
            data.put("bagopeningstock", rs.getString(2));
            data.put("neemoilopeningstock", rs.getString(3));
            data.put("neembagopeningstock", rs.getString(4));
        }

        rs.close();
        st.close();
        return data;
    }
}
