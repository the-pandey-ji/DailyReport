package com.servlet;

import com.util.DBUtil;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.*;
import java.sql.*;
import java.util.*;

@WebServlet("/telex")
public class TelexStatusServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String action=req.getParameter("action");
        if("fetch".equals(action)){
            fetchData(req,resp);
            return;
        }
        if("sign".equals(action)){
            fetchSign(req,resp);
            return;
        }

        loadInitial(req);
        req.getRequestDispatcher("/telexStatus.jsp").forward(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        save(req);
        loadInitial(req);
        req.getRequestDispatcher("/telexStatus.jsp").forward(req,resp);
    }

    private void loadInitial(HttpServletRequest req){
        try(Connection c=DBUtil.getConnection()){
            Statement st=c.createStatement();

            ResultSet rs=st.executeQuery(
                "SELECT TO_CHAR(SYSDATE,'DD/MM/YYYY') FROM dual");
            if(rs.next())
                req.setAttribute("tx_date",rs.getString(1));

            rs=st.executeQuery(
                "SELECT D_SIGN_AUTH,D_SIGN_NAME,D_SIGN_DESIGNATION FROM D_TELEX_SIGN");
            List<String[]> list=new ArrayList<>();
            while(rs.next())
                list.add(new String[]{rs.getString(1),rs.getString(2),rs.getString(3)});
            req.setAttribute("signList",list);

        }catch(Exception e){e.printStackTrace();}
    }

    private void fetchSign(HttpServletRequest req,HttpServletResponse resp)throws IOException{
        resp.setContentType("text/plain");
        try(Connection c=DBUtil.getConnection()){
            PreparedStatement ps=c.prepareStatement(
                "SELECT D_SIGN_NAME,D_SIGN_DESIGNATION FROM D_TELEX_SIGN WHERE D_SIGN_AUTH=?");
            ps.setString(1,req.getParameter("sign_auth"));
            ResultSet rs=ps.executeQuery();
            if(rs.next())
                resp.getWriter().print(rs.getString(1)+"?#?"+rs.getString(2));
        }catch(Exception e){e.printStackTrace();}
    }

    private void fetchData(HttpServletRequest req,HttpServletResponse resp)throws IOException{
        resp.setContentType("text/plain");
        try(Connection c=DBUtil.getConnection()){
            PreparedStatement ps=c.prepareStatement(
                "SELECT * FROM D_TELEX_STATUS_PNP WHERE D_TX_DATE=TO_DATE(?,'DD/MM/YYYY')");
            ps.setString(1,req.getParameter("tx_date"));
            ResultSet rs=ps.executeQuery();
            if(!rs.next()){
                resp.getWriter().print("N");
                return;
            }

            StringBuilder sb=new StringBuilder();
            sb.append(rs.getString("D_SIGN_AUTH")).append("?#?");
            PreparedStatement ps2=c.prepareStatement(
                "SELECT D_SIGN_NAME,D_SIGN_DESIGNATION FROM D_TELEX_SIGN WHERE D_SIGN_AUTH=?");
            ps2.setString(1,rs.getString("D_SIGN_AUTH"));
            ResultSet rs2=ps2.executeQuery();
            if(rs2.next())
                sb.append(rs2.getString(1)).append("?#?").append(rs2.getString(2)).append("?#?");

            for (int i = 1; i <= 16; i++) {
                String val = rs.getString("D_TX_STATUS" + i);
                sb.append(val == null ? "" : val).append("?#?");
            }
            
            
            resp.getWriter().print(sb.toString());

        }catch(Exception e){
            e.printStackTrace();
            resp.getWriter().print("N");
        }
    }

    private void save(HttpServletRequest r){
        try(Connection c=DBUtil.getConnection()){
            PreparedStatement chk=c.prepareStatement(
                "SELECT COUNT(*) FROM D_TELEX_STATUS_PNP WHERE D_TX_DATE=TO_DATE(?,'DD/MM/YYYY')");
            chk.setString(1,r.getParameter("tx_date"));
            ResultSet rs=chk.executeQuery(); rs.next();

            if(rs.getInt(1)==0)
                insert(r,c);
            else
                update(r,c);

        }catch(Exception e){e.printStackTrace();}
    }

    private void insert(HttpServletRequest r, Connection c) throws Exception {

        String sql =
            "INSERT INTO D_TELEX_STATUS_PNP (" +
            "D_TX_DATE, " +
            "D_TX_STATUS1, D_TX_STATUS2, D_TX_STATUS3, D_TX_STATUS4, " +
            "D_TX_STATUS5, D_TX_STATUS6, D_TX_STATUS7, D_TX_STATUS8, " +
            "D_TX_STATUS9, D_TX_STATUS10, D_TX_STATUS11, D_TX_STATUS12, " +
            "D_TX_STATUS13, D_TX_STATUS14, D_TX_STATUS15, D_TX_STATUS16, " +
            "D_SIGN_AUTH) " +
            "VALUES (TO_DATE(?,'DD/MM/YYYY'),?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

        PreparedStatement ps = c.prepareStatement(sql);

        int idx = 1;

        ps.setString(idx++, r.getParameter("tx_date"));

        for (int i = 1; i <= 16; i++) {
            String val = r.getParameter("s" + i);
            ps.setString(idx++, (val == null ? "" : val)); // avoid NULL
        }

        ps.setInt(idx, Integer.parseInt(r.getParameter("sign_auth")));

        ps.executeUpdate();
    }


    private void update(HttpServletRequest r,Connection c)throws Exception{
        StringBuilder sb=new StringBuilder("UPDATE D_TELEX_STATUS_PNP SET ");
        for(int i=1;i<=16;i++)
            sb.append("D_TX_STATUS").append(i).append("=?,");
        sb.append("D_SIGN_AUTH=? WHERE D_TX_DATE=TO_DATE(?,'DD/MM/YYYY')");

        PreparedStatement ps=c.prepareStatement(sb.toString());
        int idx=1;
        for(int i=1;i<=16;i++)
            ps.setString(idx++,r.getParameter("s"+i));
        ps.setInt(idx++,Integer.parseInt(r.getParameter("sign_auth")));
        ps.setString(idx,r.getParameter("tx_date"));
        ps.executeUpdate();
    }
}
