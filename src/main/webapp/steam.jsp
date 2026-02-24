<%@ page contentType="text/html;charset=UTF-8" %>
<%
String reportDate = (String)request.getAttribute("report_date");
if(reportDate == null) reportDate = "";
String error = request.getAttribute("error")==null?"":request.getAttribute("error").toString();
%>

<!DOCTYPE html>
<html>
<head>
<title>SGP Daily Report Data Entry Form</title>

<style>
body{font-family:Arial;background:#eee}
h3{text-align:center;color:#800000;margin-top:20px}
table{width:65%;margin:auto;border-collapse:collapse}
td{padding:6px}
input{width:90px}
.header{background:#b7d8ea;padding:6px;font-weight:bold}
.footer{text-align:center;margin:20px}
</style>

<script>
function num(el){
    if(el.value!=="" && isNaN(el.value)){
        alert("Enter numeric value only");
        el.value="";
        el.focus();
    }
}

function getDetail(){
    let d=document.getElementById("report_date").value;
    if(d===""){ alert("Enter date"); return; }

    let x=new XMLHttpRequest();
    x.open("GET","steam?action=details&report_date="+encodeURIComponent(d),true);
    x.onload=function(){
        let a=this.responseText.split("?#?");
        if(a[0]==="N"){ alert("No Data Found"); return; }

        let f=[
          "steam_production","steam_int_consumption","steam_export_to_cpp",
          "steam_export_to_amm","continuous_blowdown","steam_venting",
          "fwd_for_desuper_heating","avrg_bfw_temperature","coal_consumption",
          "fuel_cons_ng","boiler_1","boiler_2","boiler_3","fly_ash_despatch"
        ];
        for(let i=0;i<f.length;i++)
            document.getElementById(f[i]).value=a[i];
    }
    x.send();
}

function save(){
    document.SteamForm.jmethod.value="save";
    document.SteamForm.submit();
}

function setForm(){
    if("<%=error%>"==="N"){
        alert("Not Authorized");
        document.querySelectorAll("input").forEach(i=>i.disabled=true);
    }
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

<body onload="setForm()">

<div class="header">
Department : IT
<span style="float:right">Date : <%=reportDate%></span>
</div>

<h3>SGP Daily Report Data Entry Form</h3>

<form name="SteamForm" method="post" action="steam">

<input type="hidden" name="jmethod">
<input type="hidden" name="error" value="<%=error%>">

<table>
<tr>
<td><b>Report Date</b></td>
<td><input id="report_date" name="report_date" value="<%=reportDate%>" onchange="getDetail()"></td>
</tr>

<tr>
<td>Steam Production</td><td><input id="steam_production" name="steam_production" onblur="num(this)"></td>
<td>Steam Internal Consumption</td><td><input id="steam_int_consumption" name="steam_int_consumption" onblur="num(this)"></td>
</tr>

<tr>
<td>Steam Export To CPP</td><td><input id="steam_export_to_cpp" name="steam_export_to_cpp" onblur="num(this)"></td>
<td>Steam Export To Amm</td><td><input id="steam_export_to_amm" name="steam_export_to_amm" onblur="num(this)"></td>
</tr>

<tr>
<td>Continuous Blow Down</td><td><input id="continuous_blowdown" name="continuous_blowdown" onblur="num(this)"></td>
<td>Steam Venting</td><td><input id="steam_venting" name="steam_venting" onblur="num(this)"></td>
</tr>

<tr>
<td>Fwd For Desuper Heating</td><td><input id="fwd_for_desuper_heating" name="fwd_for_desuper_heating" onblur="num(this)"></td>
<td>Avg Bfw Temperature</td><td><input id="avrg_bfw_temperature" name="avrg_bfw_temperature" onblur="num(this)"></td>
</tr>

<tr>
<td>Coal Consumption</td><td><input id="coal_consumption" name="coal_consumption" onblur="num(this)"></td>
<td>Fuel Consumption (NG)</td><td><input id="fuel_cons_ng" name="fuel_cons_ng" onblur="num(this)"></td>
</tr>

<tr>
<td>Boiler 1</td><td><input id="boiler_1" name="boiler_1" onblur="num(this)"></td>
<td>Boiler 2</td><td><input id="boiler_2" name="boiler_2" onblur="num(this)"></td>
<td>Boiler 3</td><td><input id="boiler_3" name="boiler_3" onblur="num(this)"></td>
</tr>

<tr>
<td>Fly Ash Despatch</td><td><input id="fly_ash_despatch" name="fly_ash_despatch" onblur="num(this)"></td>
</tr>
</table>

<div class="footer">
<button type="button" onclick="save()">Save</button>
<button type="button" onclick="location.href='index.jsp'">Exit</button>
</div>

</form>
</body>
</html>
