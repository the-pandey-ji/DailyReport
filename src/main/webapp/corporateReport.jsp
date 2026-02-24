<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.*" %>
<%
Map<String,Object> r = (Map<String,Object>)request.getAttribute("report");
String err = (String)request.getAttribute("error");
%>

<!DOCTYPE html>
<html>
<head>
<title>Corporate Daily Report</title>
<style>
body{font-family:"Times New Roman";font-size:12px}
h2{text-align:center;margin:0}
table{width:100%;border-collapse:collapse}
th,td{border:1px solid #000;padding:3px}
th{background:#e6e6e6}
.left{text-align:left}
.center{text-align:center}
.right{text-align:right}
.section{background:#d0d0d0;font-weight:bold}
</style>
</head>
<body>

<h2>दैनिक रिपोर्ट कारपोरेट कार्यालय</h2>
<div class="center">
NFL, Panipat<br>
प्रातः 5 बजे को समाप्त दिनांक : <b><%=r.get("REPORT_DATE")%></b>
</div>

<br>

<table>
<tr class="section">
<th class="left">उत्पादन</th>
<th>Unit</th>
<th>दिन</th>
<th>माह</th>
<th>वर्ष</th>
<th>स्टॉक</th>
</tr>

<tr>
<td class="left">अमोनिया</td>
<td>MT</td>
<td class="right"><%=r.get("D_AMM_PROD")%></td>
<td class="right"><%=r.get("D_AMM_TO_UREA")%></td>
<td class="right"><%=r.get("D_AMM_TO_OTHER_UNITS")%></td>
<td class="right"><%=r.get("D_AMM_CL_STK")%></td>
</tr>

<tr>
<td class="left">यूरिया</td>
<td>MT</td>
<td class="right"><%=r.get("D_UREA_PROD")%></td>
<td class="right"><%=r.get("D_RAIL_DESP")%></td>
<td class="right"><%=r.get("BS_YEAR")%></td>
<td class="right"><%=r.get("D_UREA_CL_STK")%></td>
</tr>

<tr>
<td class="left">पावर उत्पादन</td>
<td>MWH</td>
<td class="right"><%=r.get("D_CPP_POWR_GEN_TG1")%></td>
<td class="right"><%=r.get("D_CPP_POWR_GEN_TG2")%></td>
<td class="right"><%=r.get("D_POWR_CONS_CPP")%></td>
<td></td>
</tr>

</table>

<br>

<table>
<tr class="section">
<th>Plant Status</th>
</tr>
<tr><td><%=r.get("D_TX_STATUS1")%></td></tr>
<tr><td><%=r.get("D_TX_STATUS2")%></td></tr>
<tr><td><%=r.get("D_TX_STATUS3")%></td></tr>
<tr><td><%=r.get("D_TX_STATUS4")%></td></tr>
<tr><td><%=r.get("D_TX_STATUS5")%></td></tr>
<tr><td><%=r.get("D_TX_STATUS6")%></td></tr>
<tr><td><%=r.get("D_TX_STATUS7")%></td></tr>
<tr><td><%=r.get("D_TX_STATUS8")%></td></tr>
<tr><td><%=r.get("D_TX_STATUS9")%></td></tr>
<tr><td><%=r.get("D_TX_STATUS10")%></td></tr>
<tr><td><%=r.get("D_TX_STATUS11")%></td></tr>
<tr><td><%=r.get("D_TX_STATUS12")%></td></tr>
<tr><td><%=r.get("D_TX_STATUS13")%></td></tr>
<tr><td><%=r.get("D_TX_STATUS14")%></td></tr>
<tr><td><%=r.get("D_TX_STATUS15")%></td></tr>
<tr><td><%=r.get("D_TX_STATUS16")%></td></tr>
</table>

<br>

<table>
<tr>
<td><b>Signed By:</b><br>
<%=r.get("D_SIGN_NAME")%><br>
<%=r.get("D_SIGN_DESIGNATION")%>
</td>
</tr>
</table>

</body>
</html>