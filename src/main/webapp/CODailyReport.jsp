<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.*" %>

<%
Map<String,Object> r = (Map<String,Object>)request.getAttribute("report");
String selectedDate = (String)request.getAttribute("selectedDate");
if(selectedDate==null) selectedDate="";
%>

<!DOCTYPE html>
<html>
<head>
<title>Daily Production Report</title>
<style>
body{font-family:Times New Roman;font-size:13px}
table{width:100%;border-collapse:collapse}
th,td{border:1px solid #000;padding:4px;text-align:right}
th{background:#eee}
.left{text-align:left}
.center{text-align:center}
.header{text-align:center;font-weight:bold;margin-bottom:10px}
.status{border:1px solid #000;padding:8px;margin-top:10px}
</style>
<script>
document.addEventListener("DOMContentLoaded", function () {

	  const form = document.forms[0];

	  form.addEventListener("keydown", function (e) {

	    if (e.key === "Enter" && e.target.tagName !== "TEXTAREA") {
	      e.preventDefault();

	      const focusable = Array.from(
	        form.querySelectorAll(
	          'input:not([type=hidden]):not([readonly]):not([disabled]), select:not([disabled]), textarea:not([disabled])'
	        )
	      );

	      const index = focusable.indexOf(e.target);

	      if (index > -1 && index < focusable.length - 1) {
	        focusable[index + 1].focus();
	      }
	    }
	  });

	});
</script>
</head>

<body>

<form method="get" action="dailyReport">
Generate Report For Date :
<input type="date" name="reportDate" required>
<button type="submit">Generate</button>
</form>

<hr>

<% if(r!=null){ %>

<div class="header">
IE-IF-01 <br>
दैनिक रिपोर्ट कारपोरेट कार्यालय <br>
NFL, Panipat <br>
Date : <b><%= selectedDate %></b>
</div>

<table>
<tr>
<th class="left">Production</th>
<th>Unit</th>
<th>Day</th>
<th>Month</th>
<th>Year</th>
<th>Stock</th>
</tr>

<tr>
<td class="left">Ammonia</td>
<td>MT</td>
<td><%= r.get("D_AMM_PROD") %></td>
<td><%= r.get("M_AMM_PROD") %></td>
<td><%= r.get("Y_AMM_PROD") %></td>
<td><%= r.get("D_AMM_CL_STK") %></td>
</tr>

<tr>
<td class="left">Urea</td>
<td>MT</td>
<td><%= r.get("D_UREA_PROD") %></td>
<td><%= r.get("M_UREA_PROD") %></td>
<td><%= r.get("Y_UREA_PROD") %></td>
<td><%= r.get("D_UREA_CL_STK") %></td>
</tr>

</table>

<div class="status">
<b>Plant Status</b><br><br>
<%= r.get("D_TX_STATUS1") %><br>
<%= r.get("D_TX_STATUS2") %><br>
<%= r.get("D_TX_STATUS3") %><br>
<%= r.get("D_TX_STATUS4") %><br>
<%= r.get("D_TX_STATUS5") %><br><br>

Signed By : <br>
<%= r.get("D_SIGN_NAME") %><br>
<%= r.get("D_SIGN_DESIGNATION") %>
</div>

<% } %>

</body>
</html>