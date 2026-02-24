<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.*" %>

<%
Map<String,Object> r = (Map<String,Object>)request.getAttribute("report");
String selectedDate = (String)request.getAttribute("selectedDate");
if(selectedDate==null) selectedDate="";

// Helper function to format date from YYYY-MM-DD to DD-MMM-YYYY if needed, 
// assuming simple injection for now based on exact mock requirement.
%>

<!DOCTYPE html>
<html>
<head>
<title>Daily Report Corporate Office</title>
<style>
    body { font-family: "Arial", sans-serif; font-size: 13px; margin: 20px; }
    .report-container { max-width: 900px; margin: auto; }
    table { width: 100%; border-collapse: collapse; margin-bottom: -1px; }
    th, td { border: 1px solid #000; padding: 4px 6px; }
    th { background: #e0e0e0; font-weight: bold; text-align: center; }
    .text-left { text-align: left; }
    .text-center { text-align: center; }
    .text-right { text-align: right; }
    .fw-bold { font-weight: bold; }
    
    .header-block { display: flex; justify-content: space-between; align-items: flex-end; margin-bottom: 5px; }
    .header-title { text-align: center; flex-grow: 1; font-size: 16px; font-weight: bold; }
    .status-box { border: 1px solid #000; padding: 10px; min-height: 100px; }
    
    /* Input Form Print Hidden */
    @media print { .no-print { display: none; } }
</style>
</head>
<body>

<div class="report-container">

    <form method="get" action="dailyReport" class="no-print" style="margin-bottom: 20px; text-align:center;">
        <b>Generate Report For Date :</b>
        <input type="date" name="reportDate" required>
        <button type="submit">Generate</button>
    </form>

<% if(r!=null && !r.isEmpty()){ %>

    <div class="header-block">
        <div>IE-IF-01</div>
        <div class="header-title">दैनिक रिपोर्ट कारपोरेट कार्यालय</div>
        <div></div>
    </div>
    <div class="header-block">
        <div>ने.फ.लि. पानीपत &nbsp;&nbsp;&nbsp; <b>NFL, Panipat</b></div>
        <div>प्रातः 5 बजे को समाप्त दिनांक &nbsp;&nbsp;&nbsp; <b><%= selectedDate %></b></div>
    </div>

    <table>
        <tr>
            <th class="text-left" style="width:25%">उत्पादन &nbsp;&nbsp;&nbsp;&nbsp; Prodn</th>
            <th style="width:10%">Unit</th>
            <th style="width:15%">दिन</th>
            <th style="width:15%">माह</th>
            <th style="width:20%">वर्ष</th>
            <th style="width:15%">स्टाक</th>
        </tr>
        <tr>
            <td class="text-left">&nbsp; अमोनिया</td>
            <td class="text-center">(MT)</td>
            <td class="text-right"><%= r.get("D_AMM_PROD") %></td>
            <td class="text-right"><%= r.get("M_AMM_PROD") %></td>
            <td class="text-right"><%= r.get("Y_AMM_PROD") %></td>
            <td class="text-right"><%= r.get("D_AMM_CL_STK") %></td>
        </tr>
        <tr>
            <td class="text-left">&nbsp; यूरिया</td>
            <td class="text-center">(MT)</td>
            <td class="text-right"><%= r.get("D_UREA_PROD") %></td>
            <td class="text-right"><%= r.get("M_UREA_PROD") %></td>
            <td class="text-right"><%= r.get("Y_UREA_PROD") %></td>
            <td class="text-right"><%= r.get("D_UREA_CL_STK") %></td>
        </tr>
        <tr>
            <td class="text-left">&nbsp; बेंटोनाइट सल्फर</td>
            <td class="text-center">(MT)</td>
            <td class="text-right"><%= r.get("D_BS_PROD") %></td>
            <td class="text-right"><%= r.get("M_BS_PROD") %></td>
            <td class="text-right"><%= r.get("Y_BS_PROD") %></td>
            <td class="text-right"><%= r.get("D_BS_CL_STK") %></td>
        </tr>
        <tr>
            <td class="text-left">&nbsp; पावर उत्पादन (मेगावाट)</td>
            <td class="text-center">(MWH)</td>
            <td class="text-right"><%= r.get("D_POWER_PROD") %></td>
            <td class="text-right"><%= r.get("M_POWER_PROD") %></td>
            <td class="text-right"><%= r.get("Y_POWER_PROD") %></td>
            <td class="text-right"></td>
        </tr>
        <tr>
            <td class="text-left">&nbsp; खपत एन.जी.</td>
            <td class="text-center">(MMSM3)</td>
            <td class="text-right"><%= r.get("D_NG_CONS") %></td>
            <td class="text-right"><%= r.get("M_NG_CONS") %></td>
            <td class="text-right"><%= r.get("Y_NG_CONS") %></td>
            <td class="text-right"></td>
        </tr>
        <tr>
            <td class="text-left fw-bold">यूरिया &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; - रेल</td>
            <td class="text-center">(MT)</td>
            <td class="text-right"><%= r.get("D_UREA_RAIL") %></td>
            <td class="text-right"><%= r.get("M_UREA_RAIL") %></td>
            <td class="text-right"><%= r.get("Y_UREA_RAIL") %></td>
            <td class="text-right"></td>
        </tr>
        <tr>
            <td class="text-left fw-bold">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; - सड़क</td>
            <td class="text-center">(MT)</td>
            <td class="text-right"><%= r.get("D_UREA_ROAD") %></td>
            <td class="text-right"><%= r.get("M_UREA_ROAD") %></td>
            <td class="text-right"><%= r.get("Y_UREA_ROAD") %></td>
            <td class="text-right"></td>
        </tr>
        <tr>
            <td class="text-left fw-bold">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; - कुल</td>
            <td class="text-center">(MT)</td>
            <td class="text-right"><%= r.get("D_UREA_TOTAL") %></td>
            <td class="text-right"><%= r.get("M_UREA_TOTAL") %></td>
            <td class="text-right"><%= r.get("Y_UREA_TOTAL") %></td>
            <td class="text-right"></td>
        </tr>
        <tr>
            <td class="text-left">&nbsp; बेंटोनाइट सल्फर</td>
            <td class="text-center">(MT)</td>
            <td class="text-right"><%= r.get("D_BS_DESP") %></td>
            <td class="text-right"><%= r.get("M_BS_DESP") %></td>
            <td class="text-right"><%= r.get("Y_BS_DESP") %></td>
            <td class="text-right"></td>
        </tr>
        <tr>
            <td class="text-left">&nbsp; अमो. प्रेषण</td>
            <td class="text-center">(MT)</td>
            <td class="text-right"><%= r.get("D_AMM_DESP") %></td>
            <td class="text-right"><%= r.get("M_AMM_DESP") %></td>
            <td class="text-right"><%= r.get("Y_AMM_DESP") %></td>
            <td class="text-right"></td>
        </tr>
        <tr>
            <td class="text-left fw-bold">&nbsp; फ्लाई ऐश सेल</td>
            <td class="text-center">(MT)</td>
            <td class="text-right"><%= r.get("D_FLY_ASH") %></td>
            <td class="text-right"><%= r.get("M_FLY_ASH") %></td>
            <td class="text-right"><%= r.get("Y_FLY_ASH") %></td>
            <td class="text-right"></td>
        </tr>
        <tr>
            <td class="text-left fw-bold">एनर्जी &nbsp;&nbsp; बै.लि.अमोनिया</td>
            <td class="text-center">(MKCal/MT)</td>
            <td class="text-right"></td>
            <td class="text-right"></td>
            <td class="text-right"></td>
            <td class="text-left">लक्ष्य &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 8.392</td>
        </tr>
        <tr>
            <td class="text-left fw-bold">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; यूरिया</td>
            <td class="text-center">(MKCal/MT)</td>
            <td class="text-right"></td>
            <td class="text-right"></td>
            <td class="text-right"></td>
            <td class="text-left">लक्ष्य &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 6.500</td>
        </tr>
        <tr>
            <td class="text-left fw-bold">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; नॉन प्लांट एनर्जी</td>
            <td class="text-center">(MKCal/MT)</td>
            <td class="text-right"></td>
            <td class="text-right"></td>
            <td class="text-right"></td>
            <td class="text-right"></td>
        </tr>
        <tr>
            <td class="text-left fw-bold">प्रोडक्शन-लोस अमोनिया</td>
            <td class="text-center">(MT)</td>
            <td class="text-right">0</td>
            <td class="text-right"></td>
            <td class="text-right"></td>
            <td class="text-right"></td>
        </tr>
        <tr>
            <td class="text-left fw-bold">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; यूरिया</td>
            <td class="text-center">(MT)</td>
            <td class="text-right">0</td>
            <td class="text-right"></td>
            <td class="text-right"></td>
            <td class="text-right"></td>
        </tr>
    </table>

    <table>
        <tr>
            <th class="text-left" style="width:25%">प्राप्ति खपत एवं स्टाक</th>
            <th style="width:10%">Unit</th>
            <th style="width:15%"></th>
            <th style="width:15%">प्राप्ति</th>
            <th style="width:20%">खपत</th>
            <th style="width:15%">शेष स्टाक</th>
        </tr>
        <tr>
            <td class="text-left">&nbsp; अमोनिया</td>
            <td class="text-center">(MT)</td>
            <td class="text-center"></td>
            <td class="text-right">0</td>
            <td class="text-right"></td>
            <td class="text-right"><%= r.get("D_AMM_CL_STK") %></td>
        </tr>
        <tr>
            <td class="text-left" rowspan="2">&nbsp; कोयला</td>
            <td class="text-center" rowspan="2">(MT)</td>
            <td class="text-center" rowspan="2" style="font-size:11px;">अन्य इकाई को ट्रांसफर<br><b>0</b></td>
            <td class="text-right" rowspan="2"><%= r.get("D_COAL_RECEIPT") %></td>
            <td class="text-left">SGP &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <%= r.get("D_COAL_CONS_SGP") %></td>
            <td class="text-right" rowspan="2"><%= r.get("D_COAL_CL_STK") %></td>
        </tr>
        <tr>
            <td class="text-left">CPP &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <%= r.get("D_COAL_CONS_CPP") %></td>
        </tr>
        <tr>
            <td class="text-left">&nbsp; फीड एन जी</td>
            <td class="text-center">(MMSM3)</td>
            <td class="text-center"></td>
            <td class="text-right"></td>
            <td class="text-right"></td>
            <td class="text-right"></td>
        </tr>
        <tr>
            <td class="text-left fw-bold">जीटीजी में एनजी की खपत</td>
            <td class="text-center">(MMSM3)</td>
            <td class="text-center fw-bold"><%= r.get("D_NG_CONS_GTG") %></td>
            <td class="text-right"></td>
            <td class="text-right"></td>
            <td class="text-right"></td>
        </tr>
        <tr>
            <td class="text-left" rowspan="2">&nbsp; बॉयलर फ्यूल एनजी</td>
            <td class="text-center" rowspan="2">(MMSM3)</td>
            <td class="text-center" rowspan="2"></td>
            <td class="text-right" rowspan="2"></td>
            <td class="text-left">SGP &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 0.0450</td>
            <td class="text-right" rowspan="2"></td>
        </tr>
        <tr>
            <td class="text-left">CPP &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 0.0000</td>
        </tr>
        <tr>
            <td class="text-left">&nbsp; एचडीपीई बैग्स</td>
            <td class="text-center">(No.)</td>
            <td class="text-center"></td>
            <td class="text-right"><%= r.get("D_HDPE_RECT") %></td>
            <td class="text-right"><%= r.get("D_HDPE_CONS") %></td>
            <td class="text-right"><%= r.get("D_HDPE_STK") %></td>
        </tr>
        <tr>
            <td class="text-left fw-bold">&nbsp; नीम बैग्स</td>
            <td class="text-center">(No.)</td>
            <td class="text-center"></td>
            <td class="text-right"><%= r.get("D_NEEM_RECT") %></td>
            <td class="text-right"><%= r.get("D_NEEM_CONS") %></td>
            <td class="text-right"><%= r.get("D_NEEM_STK") %></td>
        </tr>
        <tr>
            <td class="text-left">&nbsp; पावर</td>
            <td class="text-center">(MWH)</td>
            <td class="text-center"></td>
            <td class="text-right"><%= r.get("D_POWR_IMPORT") %></td>
            <td class="text-right"><%= r.get("D_POWR_CONS_UNIT") %></td>
            <td class="text-right"></td>
        </tr>
        <tr>
            <td class="text-left fw-bold">&nbsp; नीम ऑयल</td>
            <td class="text-center">(KL)</td>
            <td class="text-center"></td>
            <td class="text-right"><%= r.get("D_NEEMOIL_RCT") %></td>
            <td class="text-right"><%= r.get("D_NEEMOIL_CONS") %></td>
            <td class="text-right"><%= r.get("D_NEEMOIL_STK") %></td>
        </tr>
    </table>

    <div class="status-box">
        <span class="fw-bold">संयंत्र की स्थिति Plant Status</span><br>
        <%= r.get("D_TX_STATUS1") %><br>
        <%= r.get("D_TX_STATUS2") %><br>
        <%= r.get("D_TX_STATUS3") %><br>
        <%= r.get("D_TX_STATUS4") %><br>
        <%= r.get("D_TX_STATUS5") %>
    </div>

<% } else if(request.getParameter("reportDate") != null) { %>
    <p style="text-align:center; color:red;">No data found for the selected date.</p>
<% } %>

</div>

</body>
</html>