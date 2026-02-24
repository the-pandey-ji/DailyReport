<%@ page contentType="text/html;charset=UTF-8" %>
<%
String date = (String)request.getAttribute("report_date");
%>
<!DOCTYPE html>
<html>
<head>
<title>CPP Daily Report Data Entry Form</title>

<style>
body{font-family:Arial;background:#eee}
h3{text-align:center;color:#800000;margin-top:20px}
table{width:95%;margin:auto;border-collapse:collapse}
td{padding:6px}
input{width:90px}
.header{background:#b7d8ea;padding:6px;font-weight:bold}
.footer{text-align:center;margin:20px}
</style>

<script>
function n(id){ return parseFloat(document.getElementById(id).value||0); }

function getDetail(){
  var d=document.getElementById("report_date").value;
  var x=new XMLHttpRequest();
  x.open("GET","cppDetails?report_date="+d,true);
  x.onload=function(){
    if(this.responseText.startsWith("N")){
      alert("No Data Available");
      return;
    }
    var a=this.responseText.split("?#?");
    let i=0;
    [
     "steam_production","steam_cons_in_cpp","steam_consumed_tg1","steam_consumed_tg2",
     "steam_export_to_amm","steam_venting","continuous_blowdown","fwd_for_desuper_heating",
     "bfw_export_to_sgp","return_condensate","avrg_bfw_temperature","coal_consumption",
     "fuel_cons_ng","boiler","tg_1","tg_2"
    ].forEach(id=>document.getElementById(id).value=a[i++]);
  };
  x.send();
}

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

<div class="header">
Department : IT
<span style="float:right">Date : <%=date%></span>
</div>

<h3>Offsites & Utilities Daily Report</h3>

<form method="post" action="cpp">

<table>
<tr>
<td>Report Date</td>
<td><input name="report_date" id="report_date"
 value="${report_date}" onchange="getDetail()"></td>
</tr>

<tr>
<td>Steam Production</td><td><input id="steam_production" name="steam_production"></td>
<td>Steam Consumption In CPP</td><td><input id="steam_cons_in_cpp" name="steam_cons_in_cpp"></td>
</tr>

<tr>
<td>Steam Consumed TG1</td><td><input id="steam_consumed_tg1" name="steam_consumed_tg1"></td>
<td>Steam Consumed TG2</td><td><input id="steam_consumed_tg2" name="steam_consumed_tg2"></td>
</tr>

<tr>
<td>Steam Export to Amm</td><td><input id="steam_export_to_amm" name="steam_export_to_amm"></td>
<td>Steam Venting</td><td><input id="steam_venting" name="steam_venting"></td>
</tr>

<tr>
<td>Continuous Blowdown</td><td><input id="continuous_blowdown" name="continuous_blowdown"></td>
<td>Fwd For Desuper Heating</td><td><input id="fwd_for_desuper_heating" name="fwd_for_desuper_heating"></td>
</tr>

<tr>
<td>BFW export to SGP</td><td><input id="bfw_export_to_sgp" name="bfw_export_to_sgp"></td>
<td>Return Condensate</td><td><input id="return_condensate" name="return_condensate"></td>
</tr>

<tr>
<td>Avg BFW Temperature</td><td><input id="avrg_bfw_temperature" name="avrg_bfw_temperature"></td>
<td>Coal Consumption</td><td><input id="coal_consumption" name="coal_consumption"></td>
</tr>

<tr>
<td>Fuel Consumption (NG)</td><td><input id="fuel_cons_ng" name="fuel_cons_ng"></td>
<td>Boiler Running Hours</td><td><input id="boiler" name="boiler"></td>
</tr>

<tr>
<td>TG-1 Running Hours</td><td><input id="tg_1" name="tg_1"></td>
<td>TG-2 Running Hours</td><td><input id="tg_2" name="tg_2"></td>
</tr>
</table>

<input type="hidden" name="jmethod" value="save">

<div style="text-align:center;margin-top:20px">
<button type="submit">Save</button>
<button type="button" onclick="location.href='index.jsp'">Exit</button>
</div>

</form>
</body>
</html>
